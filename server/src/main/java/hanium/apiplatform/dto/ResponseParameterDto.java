package hanium.apiplatform.dto;

import hanium.apiplatform.entity.RequestParameter;
import hanium.apiplatform.entity.ResponseParameter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseParameterDto {

    private Long id;
    private String description;
    private String key;
    private String type;
    private int required;

    public static ResponseParameterDto toDto(ResponseParameter responseParameter) {
        ResponseParameterDto responseParameterDto = new ResponseParameterDto();
        responseParameterDto.setId(responseParameter.getId());
        responseParameterDto.setDescription(responseParameter.getDescription());
        responseParameterDto.setKey(responseParameter.getKey());
        responseParameterDto.setType(responseParameter.getType());
        responseParameterDto.setRequired(responseParameter.getRequired());

        return responseParameterDto;
    }
}
