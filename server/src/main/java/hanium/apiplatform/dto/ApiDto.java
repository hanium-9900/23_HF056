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
        apiDto.host = api.getHost();
        apiDto.description = api.getDescription();
        apiDto.method = api.getMethod();
        apiDto.path = api.getPath();

        for (Header header : api.getHeaders()) {
            HeaderDto headerDto = new HeaderDto();
            headerDto.setDescription(header.getDescription());
            headerDto.setKey(header.getKey());
            headerDto.setRequired(header.getRequired());

            System.out.println(headerDto.getDescription());
            System.out.println(headerDto.getKey());
            System.out.println(headerDto.getRequired());

            apiDto.headers.add(headerDto);
        }

        for (RequestParameter requestParameter : api.getRequestParameters()) {
            RequestParameterDto requestParameterDto = new RequestParameterDto();
            requestParameterDto.setDescription(requestParameter.getDescription());
            requestParameterDto.setKey(requestParameter.getKey());
            requestParameterDto.setRequired(requestParameter.getRequired());

            apiDto.requestParameters.add(requestParameterDto);
        }

        for (ResponseParameter responseParameter : api.getResponseParameters()) {
            ResponseParameterDto responseParameterDto = new ResponseParameterDto();
            responseParameterDto.setDescription(responseParameter.getDescription());
            responseParameterDto.setKey(responseParameter.getKey());
            responseParameterDto.setRequired(responseParameter.getRequired());

            apiDto.responseParameters.add(responseParameterDto);
        }

        for (ErrorCode errorCode : api.getErrorCodes()) {
            ErrorCodeDto errorCodeDto = new ErrorCodeDto();
            errorCodeDto.setDescription(errorCode.getDescription());
            errorCodeDto.setKey(errorCode.getKey());

            apiDto.errorCodes.add(errorCodeDto);
        }

        return apiDto;
    }
}
