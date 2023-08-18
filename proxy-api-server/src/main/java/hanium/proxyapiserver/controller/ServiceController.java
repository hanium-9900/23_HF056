package hanium.proxyapiserver.controller;

import com.mysql.cj.conf.ConnectionUrlParser;
import hanium.proxyapiserver.dto.ApiDto;
import hanium.proxyapiserver.dto.UserServiceKeyDto;
import hanium.proxyapiserver.entity.Api;
import hanium.proxyapiserver.entity.ApiUsage;
import hanium.proxyapiserver.entity.UserServiceKey;
import hanium.proxyapiserver.exception.ConnectionRefusedException;
import hanium.proxyapiserver.exception.DuplicateServiceKeyException;
import hanium.proxyapiserver.exception.KeyNotFoundException;
import hanium.proxyapiserver.repository.ApiUsageRepository;
import hanium.proxyapiserver.repository.UserServiceKeyRepository;
import hanium.proxyapiserver.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin()
@RequiredArgsConstructor
@RequestMapping("/services")
public class ServiceController {

    private final ApiService apiService;

    private final UserServiceKeyRepository userServiceKeyRepository;
    private final ApiUsageRepository apiUsageRepository;

    // proxy api 요청 처리
    @GetMapping("/{serviceiD}/{apiName}")
    public ResponseEntity getDataThroughProxyApi(
            @PathVariable("serviceiD") long serviceId,
            @PathVariable("apiName") String apiName,
            @RequestParam HashMap<String, String> paramMap) throws IOException {

        // 헤더에 json 정보 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; utf-8");
        headers.set("Accept", "application/json");

        String apiKey = paramMap.get("key");

        // Search 'UserServiceKey' by service key from user request
        List<UserServiceKey> serviceKeys = userServiceKeyRepository.findByKey(apiKey);
        // key가 존재하지 않는 경우
        if (serviceKeys.size() == 0) {
            throw new KeyNotFoundException();
        }
        // key가 2개 이상인 경우
        else if (serviceKeys.size() > 1) {
            throw new DuplicateServiceKeyException();
        }
        // 정상적으로 1개의 key가 발견되면 proxy api를 통해 응답 요청
        else {
            UserServiceKeyDto userServiceKeyDto = UserServiceKeyDto.toDto(serviceKeys.get(0));

            // key에 연결된 servie, api 경로가 올바른지 검증
            ConnectionUrlParser.Pair<Boolean, ApiDto> pathVerificationResult = apiService.verifyPathAndUsage(userServiceKeyDto, "GET", serviceId, apiName);
            boolean isPathAndKeyVarified = pathVerificationResult.left;
            ApiDto verifiedApiDto = pathVerificationResult.right;
            if (!isPathAndKeyVarified) {
                throw new ConnectionRefusedException();
            }

            // send request to original api
            HashMap<String, String> requestParamMap = paramMap;
            requestParamMap.remove("key");
            ConnectionUrlParser.Pair<Integer, String> requestResult = apiService.requestFromProxyApi
                    (verifiedApiDto.getMethod().toUpperCase(), verifiedApiDto.getHost(), verifiedApiDto.getPath(), requestParamMap,
                            userServiceKeyDto.getService().getKey());

            int responseCode = requestResult.left;
            String response = requestResult.right;

            ApiUsage apiUsage = new ApiUsage();
            apiUsage.setUser(serviceKeys.get(0).getUser());
            for (Api api : serviceKeys.get(0).getService().getApis()) {
                if (api.getId().equals(verifiedApiDto.getId())) {
                    apiUsage.setApi(api);
                }
            }
            apiUsage.setResponseCode(responseCode);
            apiUsageRepository.save(apiUsage);

            if (responseCode >= 200 && responseCode < 300) {
                return new ResponseEntity<>(response, headers, HttpStatus.ACCEPTED);
                //return new String(response.getBytes(), "euc-kr");
            }

            return new ResponseEntity<>(Integer.toString(responseCode), headers, HttpStatus.BAD_REQUEST);
        }
    }
}
