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
public class ServiceDTO {

    private String title;
    private String description;
    private int price;
    private String key;
    private ArrayList<ApiDTO> apis = new ArrayList<>();

    public ServiceDTO(Service service) {
        setTitle(service.getTitle());
        setDescription(service.getDescription());
        setPrice(service.getPrice());
        setKey(service.getKey());

        for (Api api : service.getApis()) {
            apis.add(new ApiDTO(api));
        }
    }
}
