package hanium.apiplatform.controller;

import hanium.apiplatform.config.JwtTokenProvider;
import hanium.apiplatform.dto.ApiDto;
import hanium.apiplatform.dto.ServiceDto;
import hanium.apiplatform.dto.UserDto;
import hanium.apiplatform.dto.UserServiceKeyDto;
import hanium.apiplatform.entity.Service;
import hanium.apiplatform.exception.NotValidException;
import hanium.apiplatform.exception.ServiceNotFoundException;
import hanium.apiplatform.entity.User;
import hanium.apiplatform.entity.UserServiceKey;
import hanium.apiplatform.exception.*;
import hanium.apiplatform.repository.ServiceRepository;
import hanium.apiplatform.repository.UserRepository;
import hanium.apiplatform.repository.UserServiceKeyRepository;
import hanium.apiplatform.service.ApiService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hanium.apiplatform.service.KeyIssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin()
@RequiredArgsConstructor
@RequestMapping("/services")
public class ServiceController { // API 제공 서비스를 처리하는 컨트롤러

    private final ApiService apiService;
    private final KeyIssueService keyIssueService;

    private final ServiceRepository serviceRepository;
    private final UserServiceKeyRepository userServiceKeyRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    // 데이터 판매자가 API를 등록할 시 호출되는 메소드
    @PostMapping()
    public ServiceDto addService(@RequestBody ServiceDto serviceDto) throws IOException {

        ArrayList<ApiDto> apiDtos = serviceDto.getApis(); // 데이터 판매자가 등록한 API를 읽는다.

        for (ApiDto apiDto : apiDtos) { // 데이터 판매자가 등록한 모든 API에 대하여
            if (!apiService.verifyApi(apiDto, serviceDto.getKey())) { // API를 검수하는 메소드를 호출하여 유효성 여부를 검증한다.
                throw new NotValidException(); // API를 검수한 결과가 실패하면 브라우저에 예외를 전달한다.
            }
        }

        // API가 성공적으로 검수되면 등록된 서비스 객체를 브라우저에 전달한다.
        return ServiceDto.toDto(serviceRepository.save(Service.toEntity(serviceDto)));
    }

    @GetMapping()
    public ServiceDto getServiceById(@RequestParam Long id) {
        Service found = serviceRepository.findById(id).orElseThrow(() -> new ServiceNotFoundException());
        return ServiceDto.toDto(found);
    }

    @PostMapping()
    public ServiceDto addService(@RequestBody ServiceDto serviceDto) {
        ArrayList<ApiDto> apiDtos = serviceDto.getApis();

        for (ApiDto apiDto : apiDtos) {
            if (!apiService.verifyApi(apiDto)) {
                throw new NotValidException();
            }
        }

        return ServiceDto.toDto(serviceRepository.save(Service.toEntity(serviceDto)));
    }

    // 구매 요청 처리
    @PostMapping("/purchase")
    public boolean purchaseService(@RequestParam("id") Long servicId, HttpServletRequest header){
        // 헤더에서 JWT를 받아온다.
        String userToken = jwtTokenProvider.resolveToken(header);
        // 유효한 토큰인지 확인한다.
        if(userToken != null && jwtTokenProvider.validateToken(userToken)){
            // 유효한 토큰이면 user data 추출
            User user = userRepository.findByEmail(jwtTokenProvider.getUserPk(userToken)).orElseThrow(() -> new UserNotFoundException());
            // request param에서 service id 추출
            Service service = serviceRepository.findById(servicId).orElseThrow(() -> new ServiceNotFoundException());

            // user와 service를 이용해 key가 이미 존재하는지 검증
            if(userServiceKeyRepository.findByServiceAndUser(service, user).size() > 0){
                throw new DuplicateServiceKeyException();
            }
            else{
                // user와 service를 이용해 user를 위한 service key 생성
                String userServiceKey = keyIssueService.issueServiceKey(ServiceDto.toDto(service), UserDto.toDto(user));
                userServiceKeyRepository.save(UserServiceKey.toEntity(new UserServiceKeyDto(null, service, user, userServiceKey)));
                return true;
            }
        }
        else{
            throw new NotValidException();
        }
    }

    // proxy service key 요청 처리
    // TODO: TEST
    @GetMapping("/key")
    public String getProxyServiceKey(@RequestParam("id") Long servicId, HttpServletRequest header){
        // 헤더에서 JWT를 받아온다.
        String userToken = jwtTokenProvider.resolveToken(header);
        // 유효한 토큰인지 확인한다.
        if(userToken != null && jwtTokenProvider.validateToken(userToken)){
            // 유효한 토큰이면 user data 추출
            User user = userRepository.findByEmail(jwtTokenProvider.getUserPk(userToken)).orElseThrow(() -> new UserNotFoundException());
            // request param에서 service id 추출
            Service service = serviceRepository.findById(servicId).orElseThrow(() -> new ServiceNotFoundException());

            // user와 service를 이용해 key 탐색
            List<UserServiceKey> serviceKeys = userServiceKeyRepository.findByServiceAndUser(service, user);
            if(serviceKeys.size() == 0){
                throw new KeyNotFoundException();
            }
            // key가 2개 이상인 경우
            else if(serviceKeys.size() > 1){
                throw new DuplicateServiceKeyException();
            }
            // 정상적으로 1개의 key가 발견되면 client로 반환
            else{
                return serviceKeys.get(0).getKey();
            }
        }
        else{
            throw new NotValidException();
        }
    }
}
