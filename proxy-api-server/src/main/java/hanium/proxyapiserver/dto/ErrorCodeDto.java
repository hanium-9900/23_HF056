package hanium.proxyapiserver.dto;

import hanium.proxyapiserver.entity.ErrorCode;
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
    private int statusCode;

    public static ErrorCodeDto toDto(ErrorCode errorCode) {
        ErrorCodeDto errorCodeDto = new ErrorCodeDto();
        errorCodeDto.setId(errorCode.getId());
        errorCodeDto.setDescription(errorCode.getDescription());
        errorCodeDto.setStatusCode(errorCode.getStatusCode());

        return errorCodeDto;
    }
}
