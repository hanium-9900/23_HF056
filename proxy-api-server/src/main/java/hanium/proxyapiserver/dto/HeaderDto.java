package hanium.proxyapiserver.dto;

import hanium.proxyapiserver.entity.Header;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeaderDto {

    private Long id;
    private String description;
    private String key;
    private String type;
    private int required;

    public static HeaderDto toDto(Header header) {
        HeaderDto headerDto = new HeaderDto();
        headerDto.setId(header.getId());
        headerDto.setDescription(header.getDescription());
        headerDto.setKey(header.getKey());
        headerDto.setType(header.getType());
        headerDto.setRequired(header.getRequired());

        return headerDto;
    }
}
