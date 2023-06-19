package hanium.apiplatform.dto;

import hanium.apiplatform.entity.Api;
import hanium.apiplatform.entity.ErrorCode;
import hanium.apiplatform.entity.Header;
import hanium.apiplatform.entity.RequestParameter;
import hanium.apiplatform.entity.ResponseParameter;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiDto {

    private Long id;
    private String host;
    private String description;
    private String method;
    private String path;
    private ArrayList<HeaderDto> headers = new ArrayList<>();
    private ArrayList<RequestParameterDto> requestParameters = new ArrayList<>();
    private ArrayList<ResponseParameterDto> responseParameters = new ArrayList<>();
    private ArrayList<ErrorCodeDto> errorCodes = new ArrayList<>();

    public static ApiDto toDto(Api api) {
        ApiDto apiDto = new ApiDto();
        apiDto.setId(api.getId());
        apiDto.setHost(api.getHost());
        apiDto.setDescription(api.getDescription());
        apiDto.setMethod(api.getMethod());
        apiDto.setPath(api.getPath());

        for (Header header : api.getHeaders()) {
            apiDto.headers.add(HeaderDto.toDto(header));
        }

        for (RequestParameter requestParameter : api.getRequestParameters()) {
            apiDto.requestParameters.add(RequestParameterDto.toDto(requestParameter));
        }

        for (ResponseParameter responseParameter : api.getResponseParameters()) {
            apiDto.responseParameters.add(ResponseParameterDto.toDto(responseParameter));
        }

        for (ErrorCode errorCode : api.getErrorCodes()) {
            apiDto.errorCodes.add(ErrorCodeDto.toDto(errorCode));
        }

        return apiDto;
    }
}
