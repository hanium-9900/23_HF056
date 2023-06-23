package hanium.apiplatform.service;

import hanium.apiplatform.dto.ApiDto;
import hanium.apiplatform.dto.HeaderDto;
import hanium.apiplatform.dto.RequestParameterDto;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

@Service
public class ApiService {

    private String requestApi(String method, String host, String path, ArrayList<HeaderDto> headers,
        ArrayList<RequestParameterDto> requestParameters, String apiKey) throws IOException {

        StringBuilder requestUrlBuilder = new StringBuilder("http://" + host + path);

        if (method.equals("GET")) {
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

            int responseCode = connection.getResponseCode();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuffer = new StringBuilder();
            String inputLine;

            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuffer.append(inputLine);
            }
            bufferedReader.close();

            String response = stringBuffer.toString();

            System.out.println(responseCode);
            System.out.println(response);
        }

        if (method.equals("POST")) {
            // TODO
        }

        return null;
    }

    public boolean verifyApi(ApiDto apiDto, String apiKey) throws IOException {
        // TODO: API 검증 로직 추가

        String host = apiDto.getHost();
        String path = apiDto.getPath();
        String method = apiDto.getMethod();
        ArrayList<HeaderDto> headers = apiDto.getHeaders();
        ArrayList<RequestParameterDto> requestParameters = apiDto.getRequestParameters();

        requestApi(method, host, path, headers, requestParameters, apiKey);

        return true;
    }
}
