package hanium.apiplatform.data;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiRegisterForm {

    private String title;
    private String description;
    private int price;
    private String key;
    private ArrayList<Api> apis;
}
