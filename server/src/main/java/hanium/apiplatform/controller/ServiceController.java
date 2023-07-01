package hanium.apiplatform.controller;

import hanium.apiplatform.dto.ApiDto;
import hanium.apiplatform.dto.ServiceDto;
import hanium.apiplatform.entity.Service;
import hanium.apiplatform.exception.NotValidException;
import hanium.apiplatform.exception.ServiceNotFoundException;
import hanium.apiplatform.repository.ServiceRepository;
import hanium.apiplatform.service.ApiService;
import java.io.IOException;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin()
@RequiredArgsConstructor
@RequestMapping("/services")
public class ServiceController { // API 제공 서비스를 처리하는 컨트롤러

    private final ApiService apiService; // API와 관련된 메소드를 제공하는 서비스 객체
    private final ServiceRepository serviceRepository; // API 제공 서비스 엔티티의 데이터베이스 접근을 위한 객체

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
}
