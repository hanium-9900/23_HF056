package hanium.apiplatform.service;

import com.mysql.cj.conf.ConnectionUrlParser.Pair;
import hanium.apiplatform.dto.ApiDto;
import hanium.apiplatform.dto.ServiceDto;
import hanium.apiplatform.dto.UserDto;
import hanium.apiplatform.entity.User;
import hanium.apiplatform.dto.ServiceDto;
import hanium.apiplatform.dto.UserDto;
import hanium.apiplatform.entity.User;
import hanium.apiplatform.dto.HeaderDto;
import hanium.apiplatform.dto.RequestParameterDto;
import hanium.apiplatform.dto.ResponseParameterDto;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
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
}
