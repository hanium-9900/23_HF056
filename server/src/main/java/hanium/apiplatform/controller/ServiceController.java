package hanium.apiplatform.controller;

import com.mysql.cj.conf.ConnectionUrlParser.Pair;
import hanium.apiplatform.config.JwtTokenProvider;
import hanium.apiplatform.dto.ApiDto;
import hanium.apiplatform.dto.ReportDto;
import hanium.apiplatform.dto.ServiceDto;
import hanium.apiplatform.dto.UserDto;
import hanium.apiplatform.dto.UserServiceKeyDto;
import hanium.apiplatform.entity.Api;
import hanium.apiplatform.entity.ApiUsage;
import hanium.apiplatform.entity.ErrorLog;
import hanium.apiplatform.entity.Service;
import hanium.apiplatform.entity.Statistics;
import hanium.apiplatform.entity.UsageRate;
import hanium.apiplatform.entity.User;
import hanium.apiplatform.entity.UserServiceKey;
import hanium.apiplatform.exception.ConnectionRefusedException;
import hanium.apiplatform.exception.DuplicateServiceKeyException;
import hanium.apiplatform.exception.DuplicatedReportException;
import hanium.apiplatform.exception.KeyNotFoundException;
import hanium.apiplatform.exception.NotAuthorizedException;
import hanium.apiplatform.exception.NotValidException;
import hanium.apiplatform.exception.ReportWithoutReasonException;
import hanium.apiplatform.exception.ServiceNotFoundException;
import hanium.apiplatform.exception.UserNotFoundException;
import hanium.apiplatform.repository.ApiUsageRepository;
import hanium.apiplatform.repository.ServiceRepository;
import hanium.apiplatform.repository.UserRepository;
import hanium.apiplatform.repository.UserServiceKeyRepository;
import hanium.apiplatform.service.ApiService;
import hanium.apiplatform.service.KeyIssueService;
import hanium.apiplatform.service.ReportService;
import hanium.apiplatform.service.ServiceService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin()
@RequiredArgsConstructor
@RequestMapping("/services")
public class ServiceController { // API 제공 서비스를 처리하는 컨트롤러

    private final ApiService apiService;
    private final KeyIssueService keyIssueService;
    private final ServiceService serviceService;
    private final ReportService reportService;

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
        User user = userRepository.findByEmail(jwtTokenProvider.getUserPk(userToken)).orElseThrow(UserNotFoundException::new);
        service.setUser(user);
        serviceRepository.save(service);

        return ServiceDto.toDto(serviceRepository.save(service));
    }

    @GetMapping("/registered")
    public List<ServiceDto> getRegisteredService(HttpServletRequest request) {
        String userToken = jwtTokenProvider.resolveToken(request);
        User user = userRepository.findByEmail(jwtTokenProvider.getUserPk(userToken)).orElseThrow(UserNotFoundException::new);
        List<Service> services = serviceRepository.findServicesByUserId(user.getId());

        return services.stream().map(ServiceDto::toDto).collect(Collectors.toList());
    }

    /**
     * 구매한 서비스 목록 조회
     */
    @GetMapping("/purchased")
    public List<ServiceDto> getPurchasedService(HttpServletRequest request) {
        String userToken = jwtTokenProvider.resolveToken(request);
        User user = userRepository.findByEmail(jwtTokenProvider.getUserPk(userToken)).orElseThrow(UserNotFoundException::new);
        List<UserServiceKey> userServiceKeys = userServiceKeyRepository.findByUser_Id(user.getId());

        List<ServiceDto> serviceDtos = new ArrayList<>();
        for (UserServiceKey userServiceKey : userServiceKeys) {
            serviceDtos.add(
                ServiceDto.toDto(userServiceKey.getService())
            );
        }

        return serviceDtos;
    }

    /**
     * 서비스 신고
     */
    @PostMapping("/{id}/report")
    public ReportDto reportServiceById(@PathVariable("id") Long id, @RequestBody ReportDto reportDto, HttpServletRequest request) {
        if (reportDto.getReason() == null) {
            throw new ReportWithoutReasonException();
        }

        // 헤더에서 JWT를 받아온다.
        String userToken = jwtTokenProvider.resolveToken(request);
        // 유효한 토큰인지 확인한다.
        if (userToken != null && jwtTokenProvider.validateToken(userToken)) {
            // 유효한 토큰이면 user data 추출
            User user = userRepository.findByEmail(jwtTokenProvider.getUserPk(userToken))
                .orElseThrow(UserNotFoundException::new);
            UserDto userDto = UserDto.toDto(user);
            reportDto.setUser(userDto);

            // request param에서 service id 추출
            Service service = serviceRepository.findById(id)
                .orElseThrow(ServiceNotFoundException::new);
            ServiceDto serviceDto = ServiceDto.toDto(service);
            reportDto.setService(serviceDto);

            // user와 service를 이용해 key 탐색
            List<UserServiceKey> serviceKeys = userServiceKeyRepository.findByService_IdAndUser_Id(serviceDto.getId(), userDto.getId());
            if (serviceKeys.size() == 0) {
                throw new KeyNotFoundException();
            }
            // key가 2개 이상인 경우
            else if (serviceKeys.size() > 1) {
                throw new DuplicateServiceKeyException();
            }
            // 정상적으로 1개의 key가 발견되면 신고 가능
            else {
                if (reportService.isReportDuplicated(reportDto)) {
                    throw new DuplicatedReportException();
                } else {
                    ReportDto savedReport = reportService.saveReport(reportDto, user, service);
                    System.out.println(savedReport);
                    return savedReport;
                }
            }
        } else {
            throw new NotValidException();
        }
    }

    // 서비스 id로 조회
    @GetMapping("/{id}")
    public ServiceDto getServiceById(@PathVariable("id") Long id) {
        Service service = serviceRepository.findById(id).orElseThrow(ServiceNotFoundException::new);
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

        return services.stream().map(ServiceDto::toDto).collect(Collectors.toList());
    }

    // 서비스 수정
    @PutMapping("/{id}")
    public ServiceDto updateServiceById(@PathVariable("id") Long id, @RequestBody ServiceDto serviceDto, HttpServletRequest header) {
        String userToken = jwtTokenProvider.resolveToken(header);
        User user = userRepository.findByEmail(jwtTokenProvider.getUserPk(userToken)).orElseThrow(() -> new UserNotFoundException());
        serviceDto.setId(id);

        return ServiceDto.toDto(serviceService.updateServiceInfo(UserDto.toDto(user), serviceDto));
    }

    @DeleteMapping("/{id}")
    public Long deleteServiceById(@PathVariable("id") Long id, HttpServletRequest request) {
        String userToken = jwtTokenProvider.resolveToken(request);
        User user = userRepository.findByEmail(jwtTokenProvider.getUserPk(userToken)).orElseThrow(UserNotFoundException::new);

        Service result = serviceRepository.findServiceByIdAndUserId(id, user.getId()).orElseThrow(NotAuthorizedException::new);

        serviceRepository.deleteById(id);
        return id;
    }

    // 구매 요청 처리
    @PostMapping("/{id}/purchase")
    public boolean purchaseService(@PathVariable("id") Long serviceId, HttpServletRequest header) {
        // 헤더에서 JWT를 받아온다.
        String userToken = jwtTokenProvider.resolveToken(header);
        // 유효한 토큰인지 확인한다.
        if (userToken != null && jwtTokenProvider.validateToken(userToken)) {
            // 유효한 토큰이면 user data 추출
            User user = userRepository.findByEmail(jwtTokenProvider.getUserPk(userToken)).orElseThrow(UserNotFoundException::new);
            // request param에서 service id 추출
            Service service = serviceRepository.findById(serviceId).orElseThrow(ServiceNotFoundException::new);

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
    @GetMapping("/{id}/key")
    public String getProxyServiceKey(@PathVariable("id") Long servicId, HttpServletRequest header) {
        // 헤더에서 JWT를 받아온다.
        String userToken = jwtTokenProvider.resolveToken(header);
        // 유효한 토큰인지 확인한다.
        if (userToken != null && jwtTokenProvider.validateToken(userToken)) {
            // 유효한 토큰이면 user data 추출
            UserDto userDto = UserDto.toDto(
                userRepository.findByEmail(jwtTokenProvider.getUserPk(userToken)).orElseThrow(UserNotFoundException::new));

            // request param에서 service id 추출
            ServiceDto serviceDto = ServiceDto.toDto(
                serviceRepository.findById(servicId).orElseThrow(ServiceNotFoundException::new));

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

    /**
     * 서비스 상태 확인 responseCode가 400 이상이면 false 미만이면 true
     */
    @GetMapping("/{id}/status")
    public boolean getServiceStatus(@PathVariable("id") Long id) {
        return serviceService.serviceStatus(
            ServiceDto.toDto(
                serviceRepository.findById(id).orElseThrow(ServiceNotFoundException::new)
            ));
    }
}
