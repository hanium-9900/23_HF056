package hanium.apiplatform.controller;

import hanium.apiplatform.dto.ServiceDto;
import hanium.apiplatform.entity.Service;
import hanium.apiplatform.exception.ServiceNotFoundException;
import hanium.apiplatform.repository.ServiceRepository;
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
public class ServiceController {

    private final ServiceRepository serviceRepository;

    @GetMapping()
    public ServiceDto getServiceById(@RequestParam Long id) {
        Service found = serviceRepository.findById(id).orElseThrow(() -> new ServiceNotFoundException());
        return ServiceDto.toDto(found);
    }

    @PostMapping()
    public ServiceDto addService(@RequestBody ServiceDto serviceDto) {
        return ServiceDto.toDto(serviceRepository.save(Service.toEntity(serviceDto)));
    }
}
