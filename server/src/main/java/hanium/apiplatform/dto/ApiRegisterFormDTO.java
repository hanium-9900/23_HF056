package hanium.apiplatform.dto;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiRegisterFormDTO {

    private String title;
    private String description;
    private int price;
    private String key;
    private ArrayList<ApiDTO> apis;
}
