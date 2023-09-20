package hanium.apiplatform.dto;

import hanium.apiplatform.entity.Api;
import hanium.apiplatform.entity.ErrorCode;
import hanium.apiplatform.entity.Header;
import hanium.apiplatform.entity.RequestParameter;
import hanium.apiplatform.entity.ResponseParameter;
import java.util.ArrayList;
import java.util.List;

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
    private int limitation;
    private ArrayList<HeaderDto> headers = new ArrayList<>();
    private String requestParameters;
    private String responseParameters;
    private ArrayList<ErrorCodeDto> errorCodes = new ArrayList<>();

    public String getRequestParameter() {
        return requestParameters;
    }

    public String getResponseParameter() {
        return responseParameters;
    }

    public static ApiDto toDto(Api api) {
        ApiDto apiDto = new ApiDto();
        apiDto.setId(api.getId());
        apiDto.setHost(api.getHost());
        apiDto.setDescription(api.getDescription());
        apiDto.setMethod(api.getMethod());
        apiDto.setPath(api.getPath());
        apiDto.setLimitation(api.getLimitation());

        for (Header header : api.getHeaders()) {
            apiDto.headers.add(HeaderDto.toDto(header));
        }

        apiDto.setRequestParameters(api.getRequestParameters());
        apiDto.setResponseParameters(api.getResponseParameters());

        for (ErrorCode errorCode : api.getErrorCodes()) {
            apiDto.errorCodes.add(ErrorCodeDto.toDto(errorCode));
        }

        return apiDto;
    }

    public static ArrayList<ApiDto> toDto(List<Api> apis) {
        ArrayList<ApiDto> apiDtos = new ArrayList<>(apis.size());

        for (Api api : apis) {
            ApiDto apiDto = new ApiDto();
            apiDto.setId(api.getId());
            apiDto.setHost(api.getHost());
            apiDto.setDescription(api.getDescription());
            apiDto.setMethod(api.getMethod());
            apiDto.setPath(api.getPath());

            for (Header header : api.getHeaders()) {
                apiDto.headers.add(HeaderDto.toDto(header));
            }

            apiDto.setRequestParameters(api.getRequestParameters());
            apiDto.setResponseParameters(api.getResponseParameters());

            for (ErrorCode errorCode : api.getErrorCodes()) {
                apiDto.errorCodes.add(ErrorCodeDto.toDto(errorCode));
            }

            apiDtos.add(apiDto);
        }

        return apiDtos;
    }
}