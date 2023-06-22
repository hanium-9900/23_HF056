package hanium.apiplatform.entity;

import hanium.apiplatform.dto.PurchaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String key;

    public static Purchase toEntity(PurchaseDto purchaseDto){
        Purchase purchase = new Purchase();

        purchase.setId(purchaseDto.getId());
        purchase.setService(purchaseDto.getService());
        purchase.setUser(purchaseDto.getUser());
        purchase.setKey(purchase.getKey());

        return purchase;
    }
}
