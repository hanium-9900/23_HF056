package hanium.apiplatform.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestParameterDto {

    private String description;
    private String key;
    private String type;
    private int required;
}
