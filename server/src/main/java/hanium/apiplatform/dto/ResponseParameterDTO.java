package hanium.apiplatform.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseParameterDTO {

    private String description;
    private String key;
    private int required;

}
