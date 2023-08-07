package hanium.apiplatform.controller;

import com.mysql.cj.conf.ConnectionUrlParser.Pair;
import hanium.apiplatform.config.JwtTokenProvider;
import hanium.apiplatform.dto.ApiDto;
import hanium.apiplatform.dto.ServiceDto;
import hanium.apiplatform.dto.UserDto;
import hanium.apiplatform.dto.UserServiceKeyDto;
import hanium.apiplatform.entity.*;
import hanium.apiplatform.exception.*;
import hanium.apiplatform.repository.ApiUsageRepository;
import hanium.apiplatform.repository.ServiceRepository;
import hanium.apiplatform.repository.UserRepository;
import hanium.apiplatform.repository.UserServiceKeyRepository;
import hanium.apiplatform.service.ApiService;
import hanium.apiplatform.service.KeyIssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

    private final EntityManager entityManager;

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

    @GetMapping("/registered")
    public List<ServiceDto> getRegisteredService(HttpServletRequest request) {
        String userToken = jwtTokenProvider.resolveToken(request);
        User user = userRepository.findByEmail(jwtTokenProvider.getUserPk(userToken)).orElseThrow(() -> new UserNotFoundException());
        List<Service> services = serviceRepository.findServicesByUserId(user.getId());

        return services.stream().map(ServiceDto::toDto).collect(Collectors.toList());
    }

    // 서비스 id로 조회
    @GetMapping("/{id}")
    public ServiceDto getServiceById(@PathVariable("id") Long id) {
        Service service = serviceRepository.findById(id).orElseThrow(() -> new ServiceNotFoundException());
        return ServiceDto.toDto(service);
    }

    // 전체 서비스 조회 or 카테고리별 조회
    @GetMapping()
    public List<ServiceDto> getServices(@RequestParam(required = false) String category) {
        List<Service> services;
        if (category == null) {
            services = serviceRepository.findAll();

        } else {
            services = serviceRepository.findByCategory(category);
        }

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
            if (userServiceKeyRepository.findByService_IdAndUser_Id(ServiceDto.toDto(service).getId(), UserDto.toDto(user).getId()).size()
                    > 0) {
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
            UserDto userDto = UserDto.toDto(
                    userRepository.findByEmail(jwtTokenProvider.getUserPk(userToken)).orElseThrow(() -> new UserNotFoundException()));

            // request param에서 service id 추출
            ServiceDto serviceDto = ServiceDto.toDto(
                    serviceRepository.findById(servicId).orElseThrow(() -> new ServiceNotFoundException()));

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
            Pair<Boolean, ApiDto> pathVerificationResult = apiService.verifyPathAndUsage(userServiceKeyDto, "GET", serviceId, apiName);
            boolean isPathAndKeyVarified = pathVerificationResult.left;
            ApiDto verifiedApiDto = pathVerificationResult.right;
            if (!isPathAndKeyVarified) {
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

    // 서비스 월별 통계 조회
    @GetMapping("/{id}/statistics")
    public List<Statistics> getStatisticsByIdAndMonth(@PathVariable("id") Long id, @RequestParam int month) {
        String sql = "select api_id, api.method, api.path, month(creation_timestamp) as month, day(creation_timestamp) as day, " +
                "response_code, count(*) as count from (service join api on service.id = api.service_id) " +
                "join api_usage on api.id = api_usage.api_id where service_id = :id group by api_id, month(creation_timestamp), day(creation_timestamp) having month = :month ;";
        return (List<Statistics>) entityManager.createNativeQuery(sql, Statistics.class)
                .setParameter("id", id)
                .setParameter("month", month)
                .getResultList();
    }

    // 서비스 사용률 조회
    @GetMapping("/{id}/usage-rate")
    public List<UsageRate> getUsageRates(@PathVariable("id") Long id, @RequestParam int month, @RequestParam int day) {
        String sql = "select api.id, api.method, api.path, (count(*) /(api.limitation * (select count(distinct user.email) from user " +
                "join api_usage on user.id = api_usage.user_id where month(creation_timestamp) = :month and day(creation_timestamp) = :day))) as usage_rate, limitation from service join api on service.id = api.service_id "
                + "join api_usage on api.id = api_usage.api_id where service.id = :id and month(creation_timestamp) = :month and day(creation_timestamp) = :day group by api.id;";
        return (List<UsageRate>) entityManager.createNativeQuery(sql, UsageRate.class)
                .setParameter("id", id).setParameter("month", month)
                .setParameter("day", day)
                .getResultList();

    }

    // 서비스 에러 로그 조회
    @GetMapping("/{id}/error-log")
    public List<ErrorLog> getErrorLogs(@PathVariable("id") Long id, @RequestParam int limit) {
        String sql = "select api.id, api.method, api.path, api_usage.response_code, api_usage.creation_timestamp from service " +
                "join api on service.id = api.service_id join api_usage on api.id = api_usage.api_id where service.id = :id and response_code >= 400 order by creation_timestamp desc limit :limit ;";

        return (List<ErrorLog>) entityManager.createNativeQuery(sql, ErrorLog.class)
                .setParameter("id", id)
                .setParameter("limit", limit)
                .getResultList();
    }
}
