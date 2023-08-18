package hanium.apiplatform.service;

import hanium.apiplatform.dto.ApiDto;
import hanium.apiplatform.dto.ServiceDto;
import hanium.apiplatform.dto.UserDto;
import hanium.apiplatform.entity.Api;
import hanium.apiplatform.exception.NotPermittedUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceService {
    private final EntityManager em;

    private final ApiService apiService;

    public hanium.apiplatform.entity.Service updateServiceInfo(UserDto userDto, ServiceDto serviceDto){
        hanium.apiplatform.entity.Service service = em.find(hanium.apiplatform.entity.Service.class, serviceDto.getId());

        if(userDto.getEmail().equals(service.getUser().getEmail())){
            service.setTitle(serviceDto.getTitle());
            service.setDescription(serviceDto.getDescription());
            service.setCategory(serviceDto.getCategory());
            service.setPrice(serviceDto.getPrice());
            service.setKey(serviceDto.getKey());

            apiService.updateApiInfos(serviceDto.getApis());
        }
        else{
            throw new NotPermittedUserException();
        }

        return service;
    }

    public boolean serviceStatus(ServiceDto serviceDto) {
        String serviceKey = serviceDto.getKey();
        for(ApiDto apiDto : serviceDto.getApis()){
            int responseCode = 0;
            responseCode = apiService.apiResponseCode(apiDto, serviceKey);
            if(responseCode < 400) return true;
        }

        return false;
    }
}
