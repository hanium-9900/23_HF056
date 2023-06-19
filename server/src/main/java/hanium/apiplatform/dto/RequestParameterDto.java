package hanium.apiplatform.dto;

import hanium.apiplatform.entity.Header;
import hanium.apiplatform.entity.RequestParameter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestParameterDto {

    private Long id;
    private String description;
    private String key;
    private String type;
    private int required;

    public static RequestParameterDto toDto(RequestParameter requestParameter) {
        RequestParameterDto requestParameterDto = new RequestParameterDto();
        requestParameterDto.setId(requestParameter.getId());
        requestParameterDto.setDescription(requestParameter.getDescription());
        requestParameterDto.setKey(requestParameter.getKey());
        requestParameterDto.setType(requestParameter.getType());
        requestParameterDto.setRequired(requestParameter.getRequired());

        return requestParameterDto;
    }
}
