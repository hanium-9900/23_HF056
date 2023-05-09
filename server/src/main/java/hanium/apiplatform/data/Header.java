package hanium.apiplatform.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Header {

    private String description;
    private String key;
    private int required;
}
