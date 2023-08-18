package hanium.proxyapiserver.dto;

import hanium.proxyapiserver.entity.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
        apiDto.setLimitation((api.getLimitation()));

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

    public static ArrayList<ApiDto> toDto(List<Api> apis) {
        ArrayList<ApiDto> apiDtos = new ArrayList<>(apis.size());

        for (Api api :
            apis) {
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

            apiDtos.add(apiDto);
        }

        return apiDtos;
    }
}
