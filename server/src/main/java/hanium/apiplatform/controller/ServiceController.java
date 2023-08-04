package hanium.apiplatform.controller;

import hanium.apiplatform.config.JwtTokenProvider;
import hanium.apiplatform.dto.*;
import hanium.apiplatform.entity.*;
import hanium.apiplatform.exception.*;
import hanium.apiplatform.repository.ApiUsageRepository;
import hanium.apiplatform.repository.ServiceRepository;
import hanium.apiplatform.repository.UserRepository;
import hanium.apiplatform.repository.UserServiceKeyRepository;
import hanium.apiplatform.service.ApiService;
import hanium.apiplatform.service.KeyIssueService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import com.mysql.cj.conf.ConnectionUrlParser.Pair;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin()
@RequiredArgsConstructor
@RequestMapping("/services")
public class ServiceController { // API 제공 서비스를 처리하는 컨트롤러

    private final ApiService apiService;
    private final KeyIssueService keyIssueService;

    private final ServiceRepository serviceRepository;
    private final UserServiceKeyRepository userServiceKeyRepository;
    private final ApiUsageRepository apiUsageRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    // 데이터 판매자가 API를 등록할 시 호출되는 메소드
    @PostMapping()
    public ServiceDto addService(@RequestBody ServiceDto serviceDto, HttpServletRequest header) throws IOException {

        ArrayList<ApiDto> apiDtos = serviceDto.getApis(); // 데이터 판매자가 등록한 API를 읽는다.

        for (ApiDto apiDto : apiDtos) { // 데이터 판매자가 등록한 모든 API에 대하여
            if (!apiService.verifyApi(apiDto, serviceDto.getKey())) { // API를 검수하는 메소드를 호출하여 유효성 여부를 검증한다.
                throw new NotValidException(); // API를 검수한 결과가 실패하면 브라우저에 예외를 전달한다.
            }
        }

        // API가 성공적으로 검수되면 등록된 서비스 객체를 브라우저에 전달한다.
        Service service = Service.toEntity(serviceDto);
        String userToken = jwtTokenProvider.resolveToken(header);
        User user = userRepository.findByEmail(jwtTokenProvider.getUserPk(userToken)).orElseThrow(() -> new UserNotFoundException());
        service.setUser(user);

        return ServiceDto.toDto(serviceRepository.save(service));
    }

    // 서비스 id로 조회
    @GetMapping("/{id}")
    public ServiceDto getServiceById(@PathVariable("id") Long id) {
        Service service = serviceRepository.findById(id).orElseThrow(() -> new ServiceNotFoundException());
        return ServiceDto.toDto(service);
    }

    // 전체 서비스 조회
    @GetMapping()
    public List<ServiceDto> getServices() {
        List<Service> services = serviceRepository.findAll();
        ArrayList<ServiceDto> result = new ArrayList<>();

        for (Service service : services) {
            result.add(ServiceDto.toDto(service));
        }
        return result;
    }

    // TODO
    /*@PutMapping("/{id}")
    public ServiceDto updateServiceById(@PathVariable("id") Long id, @RequestBody ServiceDto serviceDto, HttpServletRequest header) {
        String userToken = jwtTokenProvider.resolveToken(header);
        User user = userRepository.findByEmail(jwtTokenProvider.getUserPk(userToken)).orElseThrow(() -> new UserNotFoundException());

    }*/

    @DeleteMapping("/{id}")
    public Long deleteServiceById(@PathVariable("id") Long id) {
        serviceRepository.deleteById(id);
        return id;
    }

    // 구매 요청 처리
    @PostMapping("/{id}/purchase")
    public boolean purchaseService(@PathVariable("id") Long servicId, HttpServletRequest header) {
        // 헤더에서 JWT를 받아온다.
        String userToken = jwtTokenProvider.resolveToken(header);
        // 유효한 토큰인지 확인한다.
        if (userToken != null && jwtTokenProvider.validateToken(userToken)) {
            // 유효한 토큰이면 user data 추출
            User user = userRepository.findByEmail(jwtTokenProvider.getUserPk(userToken)).orElseThrow(() -> new UserNotFoundException());
            // request param에서 service id 추출
            Service service = serviceRepository.findById(servicId).orElseThrow(() -> new ServiceNotFoundException());

            // user와 service를 이용해 key가 이미 존재하는지 검증
            if (userServiceKeyRepository.findByService_IdAndUser_Id(ServiceDto.toDto(service).getId(), UserDto.toDto(user).getId()).size() > 0) {
                throw new DuplicateServiceKeyException();
            } else {
                // user와 service를 이용해 user를 위한 service key 생성
                String userServiceKey = keyIssueService.issueServiceKey(ServiceDto.toDto(service), UserDto.toDto(user));

                userServiceKeyRepository.save(new UserServiceKey(null, service, user, userServiceKey));
                return true;
            }
        } else {
            throw new NotValidException();
        }
    }

    // proxy service key 요청 처리
    // TODO: TEST
    @GetMapping("/{id}/key")
    public String getProxyServiceKey(@PathVariable("id") Long servicId, HttpServletRequest header) {
        // 헤더에서 JWT를 받아온다.
        String userToken = jwtTokenProvider.resolveToken(header);
        // 유효한 토큰인지 확인한다.
        if (userToken != null && jwtTokenProvider.validateToken(userToken)) {
            // 유효한 토큰이면 user data 추출
            UserDto userDto = UserDto.toDto(userRepository.findByEmail(jwtTokenProvider.getUserPk(userToken)).orElseThrow(() -> new UserNotFoundException()));
            // request param에서 service id 추출
            ServiceDto serviceDto = ServiceDto.toDto(serviceRepository.findById(servicId).orElseThrow(() -> new ServiceNotFoundException()));

            // user와 service를 이용해 key 탐색
            List<UserServiceKey> serviceKeys = userServiceKeyRepository.findByService_IdAndUser_Id(serviceDto.getId(), userDto.getId());
            if (serviceKeys.size() == 0) {
                throw new KeyNotFoundException();
            }
            // key가 2개 이상인 경우
            else if (serviceKeys.size() > 1) {
                throw new DuplicateServiceKeyException();
            }
            // 정상적으로 1개의 key가 발견되면 client로 반환
            else {
                return serviceKeys.get(0).getKey();
            }
        } else {
            throw new NotValidException();
        }
    }

    // proxy api 요청 처리
    // TODO test
    @GetMapping("/{serviceiD}/{apiName}")
    public String getDataThroughProxyApi(
            @PathVariable("serviceiD") long serviceId,
            @PathVariable("apiName") String apiName,
            @RequestParam HashMap<String, String> paramMap) throws IOException {

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
            Pair<Boolean, ApiDto> pathVerificationResult = apiService.verifyPath(userServiceKeyDto, "GET", serviceId, apiName);
            boolean isPathAndKeyVarified =pathVerificationResult.left;
            ApiDto verifiedApiDto = pathVerificationResult.right;
            if(!isPathAndKeyVarified){
                throw new ConnectionRefusedException();
            }

            // send request to original api
            HashMap<String, String> requestParamMap = paramMap;
            requestParamMap.remove("key");
            Pair<Integer, String> requestResult = apiService.requestFromProxyApi
                    (verifiedApiDto.getMethod().toUpperCase(), verifiedApiDto.getHost(), verifiedApiDto.getPath(), requestParamMap,
                            userServiceKeyDto.getService().getKey());

            int responseCode = requestResult.left;
            String response = requestResult.right;

            ApiUsage apiUsage = new ApiUsage();
            apiUsage.setUser(serviceKeys.get(0).getUser());
            for(Api api : serviceKeys.get(0).getService().getApis()){
                if(api.getId().equals(verifiedApiDto.getId())){
                    apiUsage.setApi(api);
                }
            }
            apiUsage.setResponseCode(responseCode);
            apiUsageRepository.save(apiUsage);

            if (responseCode >= 200 && responseCode < 300){
                return response;
                //return new String(response.getBytes(), "euc-kr");
            }

            return Integer.toString(responseCode);
        }
    }
}
