package hanium.apiplatform.service;

import com.mysql.cj.conf.ConnectionUrlParser.Pair;
import hanium.apiplatform.dto.*;
import hanium.apiplatform.entity.Api;
import hanium.apiplatform.entity.RequestParameter;
import hanium.apiplatform.exception.ApiNotFoundException;
import hanium.apiplatform.exception.ConnectionRefusedException;
import hanium.apiplatform.exception.OverLimitException;
import hanium.apiplatform.repository.ApiRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ApiService {

    private final EntityManager entityManager;
    private final ApiRepository apiRepository;
    private final HeaderService headerService;
    private final RequestParameterService requestParameterService;
    private final ResponseParameterService responseParameterService;
    private final ErrorCodeService errorCodeService;

    private final EntityManager em;

    public ApiDto updateApiInfo(ApiDto apiDto){
        Api api = em.find(Api.class, apiDto.getId());

        api.setHost(api.getHost());
        api.setDescription(api.getDescription());
        api.setMethod(api.getMethod());
        api.setPath(api.getPath());

        return ApiDto.toDto(api);
    }

    public ArrayList<ApiDto> updateApiInfos(ArrayList<ApiDto> apiDtos){
        ArrayList<ApiDto> updatedApiDtos = new ArrayList<>();

        for(ApiDto apiDto : apiDtos){
            Api api = em.find(Api.class, apiDto.getId());

            api.setHost(apiDto.getHost());
            api.setDescription(apiDto.getDescription());
            api.setMethod(apiDto.getMethod());
            api.setPath(apiDto.getPath());

            List<HeaderDto> updatedHeaderDtos = headerService.updateHeaderInfos(apiDto.getHeaders());
            List<RequestParameterDto> updatedRequestParameterDtos =
                    requestParameterService.updateRequestParameters
                            (apiDto.getRequestParameters());
            List<ResponseParameterDto> updatedResponseParameterDtos =
                    responseParameterService.updateResponseParameters
                            (apiDto.getResponseParameters());
            List<ErrorCodeDto> updatedErrorCodeDtos =
                    errorCodeService.updateErrorCodes(apiDto.getErrorCodes());

            api.setLimitation(apiDto.getLimitation());

            ApiDto updatedDto = new ApiDto();
            updatedDto.setHost(api.getHost());
            updatedDto.setDescription(api.getDescription());
            updatedDto.setMethod(api.getMethod());
            updatedDto.setPath(api.getPath());
            updatedDto.setHeaders((ArrayList<HeaderDto>) updatedHeaderDtos);
            updatedDto.setRequestParameters((ArrayList<RequestParameterDto>) updatedRequestParameterDtos);
            updatedDto.setResponseParameters((ArrayList<ResponseParameterDto>) updatedResponseParameterDtos);
            updatedDto.setErrorCodes((ArrayList<ErrorCodeDto>) updatedErrorCodeDtos);
            updatedDto.setLimitation(api.getLimitation());

            updatedApiDtos.add(ApiDto.toDto(api));
        }

        return updatedApiDtos;
    }

    private Pair<Integer, String> requestApi(String method, String host, String path, ArrayList<HeaderDto> headers,
                                             ArrayList<RequestParameterDto> requestParameters, String apiKey) throws IOException {

        int responseCode = 0;
        String response = null;

        StringBuilder requestUrlBuilder = new StringBuilder("http://" + host + path);

        try {
            switch (method) {
                case "GET":
                    if (!headers.isEmpty()) {
                        // TODO
                    }

                    if (!requestParameters.isEmpty()) {
                        requestUrlBuilder.append("?");

                        for (RequestParameterDto requestParameter : requestParameters) {
                            requestUrlBuilder.append(requestParameter.getKey());
                            requestUrlBuilder.append('=');

                            if (requestParameter.getType().equals("number")) {
                                requestUrlBuilder.append(50);
                            } else if (requestParameter.getType().equals("string")) {
                                // TODO
                            }

                            requestUrlBuilder.append('&');
                        }

                        requestUrlBuilder.deleteCharAt(requestUrlBuilder.length() - 1);
                    }

                    String requestUrl = requestUrlBuilder.toString();

                    URL url = new URL(requestUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestProperty("Content-Type", "application/json; utf-8");
                    connection.setRequestProperty("Accept", "application/json");

                    connection.setRequestMethod(method);
                    connection.setRequestProperty("X-API-KEY", apiKey);

                    responseCode = connection.getResponseCode();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder stringBuffer = new StringBuilder();
                    String inputLine;

                    while ((inputLine = bufferedReader.readLine()) != null) {
                        stringBuffer.append(inputLine);
                    }
                    bufferedReader.close();

                    response = stringBuffer.toString();

                    break;

                case "POST":
                    // TODO

                    break;
            }
        } catch (Exception exception) {
            throw new ConnectionRefusedException();
        }

        return new Pair<>(responseCode, response);
    }

    public boolean verifyApi(ApiDto apiDto, String apiKey) throws IOException, JSONException {
        String host = apiDto.getHost();
        String path = apiDto.getPath();
        String method = apiDto.getMethod();
        ArrayList<HeaderDto> headers = apiDto.getHeaders();
        ArrayList<RequestParameterDto> requestParameters = apiDto.getRequestParameters();

        Pair<Integer, String> result = requestApi(method, host, path, headers, requestParameters, apiKey);

        int responseCode = result.left;
        String response = result.right;

        JSONObject jsonObject = new JSONObject(response);

        ArrayList<ResponseParameterDto> responseParameters = apiDto.getResponseParameters();

        if (responseCode >= 200 && responseCode < 300) {
            for (ResponseParameterDto responseParameter : responseParameters) {
                Iterator<String> itr = jsonObject.keys();
                boolean match = false;
                while (itr.hasNext()) {
                    String key = itr.next();
                    Object value = jsonObject.get(key);
                    if (value instanceof Number) {
                        if (responseParameter.getKey().equals(key) && responseParameter.getType().equals("number")) {
                            match = true;
                            break;
                        }
                    } else if (value instanceof String) {
                        if (responseParameter.getKey().equals(key) && responseParameter.getType().equals("string")) {
                            match = true;
                            break;
                        }
                    }
                }
                if (!match) {
                    return false;
                }
            }
        }
        return true;
    }

    public Pair<Boolean, ApiDto> verifyPathAndUsage(UserServiceKeyDto userServiceKeyDto, String method, long serviceId, String apiName) {
        // 접근한 경로가 올바른지 Service.id와 serviceId 비교
        ServiceDto serviceDto = userServiceKeyDto.getService();
        if (!serviceDto.getId().equals(serviceId)) {
            throw new ConnectionRefusedException();
        }

        // 접근 경로가 올바른지 Service.apis.id로 검증
        List<ApiDto> apiDtos = serviceDto.getApis();
        ApiDto verifiedApiDto = new ApiDto();
        boolean isApiExist = false;
        for (ApiDto apiDto : apiDtos) {
            if (apiDto.getPath().equals("/" + apiName) && apiDto.getMethod().equals(method.toUpperCase())) {
                isApiExist = true;
                verifiedApiDto = apiDto;

                // 사용량 제한 검증
                LocalDate now = LocalDate.now();

                String sql =
                        "select count(*) as count from api_usage where api_id = :apiId and user_id = :userId and year(creation_timestamp) = :year "
                                + "and month(creation_timestamp) = :month and day(creation_timestamp) = :day ;";

                int usage = ((BigInteger) entityManager.createNativeQuery(sql)
                        .setParameter("apiId", apiDto.getId())
                        .setParameter("userId", userServiceKeyDto.getUser().getId())
                        .setParameter("year", now.getYear())
                        .setParameter("month", now.getMonthValue())
                        .setParameter("day", now.getDayOfMonth())
                        .getSingleResult()).intValue();

                Api api = apiRepository.findById(apiDto.getId()).orElseThrow(ApiNotFoundException::new);

                if (usage > api.getLimitation()) {
                    throw new OverLimitException();
                }
            }
        }
        if (!isApiExist) {
            throw new ConnectionRefusedException();
        }

        // 필수 request parameters 존재
        /*List<RequestParameterDto> requestParameterDtos = verifiedApiDto.getRequestParameters();
        for (RequestParameterDto requestParameterDto : requestParameterDtos) {
            if(requestParameterDto.getRequired() == 1
            && !paramMap.containsKey(requestParameterDto.getKey())){
                throw new ConnectionRefusedException();
            }
        }*/

        return new Pair<>(true, verifiedApiDto);
    }

    public Pair<Integer, String> requestFromProxyApi(String method, String host, String path, HashMap<String, String> paramMap,
                                                     String apiKey) throws IOException {

        int responseCode = 0;
        String response = null;

        StringBuilder requestUrlBuilder = new StringBuilder("http://" + host + path);

        try {
            switch (method) {
                case "GET":
                    /*if (!headers.isEmpty()) {
                        // TODO
                    }*/

                    if (!paramMap.isEmpty()) {
                        requestUrlBuilder.append("?");

                        for (String paramKey : paramMap.keySet()) {
                            requestUrlBuilder.append(paramKey);
                            requestUrlBuilder.append('=');
                            requestUrlBuilder.append(paramMap.get(paramKey));

                            requestUrlBuilder.append('&');
                        }

                        requestUrlBuilder.deleteCharAt(requestUrlBuilder.length() - 1);
                    }

                    String requestUrl = requestUrlBuilder.toString();

                    URL url = new URL(requestUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestProperty("Content-Type", "application/json; utf-8");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setDoOutput(true);

                    connection.setRequestMethod(method);
                    connection.setRequestProperty("X-API-KEY", apiKey);

                    responseCode = connection.getResponseCode();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder stringBuffer = new StringBuilder();
                    String inputLine;

                    while ((inputLine = bufferedReader.readLine()) != null) {
                        stringBuffer.append(inputLine);
                    }
                    bufferedReader.close();

                    response = stringBuffer.toString();

                    break;

                case "POST":
                    // TODO

                    break;
            }
        } catch (Exception exception) {
            throw new ConnectionRefusedException();
        }

        return new Pair<>(responseCode, response);
    }

    public int apiResponseCode(ApiDto apiDto, String apiKey) {
        try{
            String host = apiDto.getHost();
            String path = apiDto.getPath();
            String method = apiDto.getMethod();
            ArrayList<HeaderDto> headers = apiDto.getHeaders();
            ArrayList<RequestParameterDto> requestParameters = apiDto.getRequestParameters();

            Pair<Integer, String> result = requestApi(method, host, path, headers, requestParameters, apiKey);

            return result.left;
        }
        catch (ConnectionRefusedException | IOException exception){
            return 400;
        }
    }
}
