/*package hanium.apiplatform.service;

import hanium.apiplatform.dto.ApiDto;
import hanium.apiplatform.dto.ServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceService {
    private final EntityManager em;

    private final ApiService apiService;

    public hanium.apiplatform.entity.Service updateServiceInfo(ServiceDto serviceDto){
        hanium.apiplatform.entity.Service service = em.find(hanium.apiplatform.entity.Service.class, serviceDto.getId());

        service.setTitle(service.getTitle());
        service.setDescription(service.getDescription());
        service.setPrice(service.getPrice());
        service.setKey(service.getKey());

        for (ApiDto apiDto : serviceDto.getApis()) {
            apiService.updateApiInfo(apiDto);
        }

        return service;
    }
}
*/