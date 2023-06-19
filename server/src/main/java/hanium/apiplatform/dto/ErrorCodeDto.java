package hanium.apiplatform.dto;

import hanium.apiplatform.entity.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorCodeDto {

    private Long id;
    private String description;
    private String key;

    public static ErrorCodeDto toDto(ErrorCode errorCode) {
        ErrorCodeDto errorCodeDto = new ErrorCodeDto();
        errorCodeDto.setId(errorCode.getId());
        errorCodeDto.setDescription(errorCode.getDescription());
        errorCodeDto.setKey(errorCode.getKey());

        return errorCodeDto;
    }
}
