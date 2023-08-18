package hanium.proxyapiserver.entity;

import hanium.proxyapiserver.dto.ApiDto;
import hanium.proxyapiserver.dto.ServiceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String category;

    @Column
    private int price;

    @Column
    private String key;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<Api> apis = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public static Service toEntity(ServiceDto serviceDto) {
        Service service = new Service();
        service.setTitle(serviceDto.getTitle());
        service.setDescription(serviceDto.getDescription());
        service.setPrice(serviceDto.getPrice());
        service.setKey(serviceDto.getKey());
        service.setCategory(serviceDto.getCategory());

        for (ApiDto apiDto : serviceDto.getApis()) {
            service.addApi(Api.toEntity(apiDto));
        }

        return service;
    }

    // TODO
//    public void update(ServiceDto serviceDto) {
//        this.title = serviceDto.getTitle();
//        this.description = serviceDto.getDescription();
//        this.price = serviceDto.getPrice();
//        this.key = serviceDto.getKey();
//
//        for (Api api : this.apis) {
//            api.
//        }
//    }

    public void addApi(Api api) {
        this.apis.add(api);
        api.updateService(this);
    }
}
