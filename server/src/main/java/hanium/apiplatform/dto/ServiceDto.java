package hanium.apiplatform.dto;

import hanium.apiplatform.entity.Api;
import hanium.apiplatform.entity.Service;
import java.util.ArrayList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ServiceDto {

    private Long id;
    private String title;
    private String description;
    private int price;
    private String key;
    private ArrayList<ApiDto> apis = new ArrayList<>();

    public static ServiceDto toDto(Service service) {
        ServiceDto serviceDto = new ServiceDto();
        serviceDto.setId(service.getId());
        serviceDto.setTitle(service.getTitle());
        serviceDto.setDescription(service.getDescription());
        serviceDto.setPrice(service.getPrice());
        serviceDto.setKey(service.getKey());

        for (Api api : service.getApis()) {
            serviceDto.apis.add(ApiDto.toDto(api));
        }

        return serviceDto;
    }
}
