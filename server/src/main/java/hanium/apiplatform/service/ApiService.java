package hanium.apiplatform.service;

import com.mysql.cj.conf.ConnectionUrlParser.Pair;
import hanium.apiplatform.dto.*;
import hanium.apiplatform.exception.ConnectionRefusedException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class ApiService {

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

    public Pair<Boolean, ApiDto> verifyPath(UserServiceKeyDto userServiceKeyDto,
                              String method,
                              long serviceId,
                              long apiId){
        // 접근한 경로가 올바른지 Service.id와 serviceId 비교
        ServiceDto serviceDto = userServiceKeyDto.getService();
        if(!serviceDto.getId().equals(serviceId)){
            throw new ConnectionRefusedException();
        }

        // 접근 경로가 올바른지 Service.apis.id로 검증
        List<ApiDto> apiDtos = serviceDto.getApis();
        ApiDto verifiedApiDto = new ApiDto();
        boolean isApiExist = false;
        for (ApiDto apiDto : apiDtos) {
            if(apiDto.getId().equals(apiId)
                && apiDto.getMethod().equals(method.toUpperCase())) {
                isApiExist = true;
                verifiedApiDto = apiDto;
            }
        }
        if(!isApiExist){
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

    public Pair<Integer, String> requestFromProxyApi
            (String method, String host, String path,
             HashMap<String, String> paramMap, String apiKey)
            throws IOException {

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
}
