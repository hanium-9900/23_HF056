package hanium.apiplatform.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Parameter {

    private String description;
    private String key;
    private int required;

}
