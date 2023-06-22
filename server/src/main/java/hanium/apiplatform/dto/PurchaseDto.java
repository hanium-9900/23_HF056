package hanium.apiplatform.dto;

import hanium.apiplatform.entity.Purchase;
import hanium.apiplatform.entity.Service;
import hanium.apiplatform.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseDto {
    private Long id;
    private Service service;
    private User user;
    private String key;

    public static PurchaseDto toDto(Purchase purchase){
        PurchaseDto purchaseDto = new PurchaseDto();
        purchaseDto.setId(purchase.getId());
        purchaseDto.setService(purchase.getService());
        purchaseDto.setUser(purchase.getUser());
        purchaseDto.setKey(purchase.getKey());

        return purchaseDto;
    }
}
