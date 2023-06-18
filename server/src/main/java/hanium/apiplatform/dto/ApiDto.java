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
            HeaderDto headerDTO = new HeaderDto();
            headerDTO.setDescription(header.getDescription());
            headerDTO.setKey(header.getKey());
            headerDTO.setRequired(header.getRequired());

            System.out.println(headerDTO.getDescription());
            System.out.println(headerDTO.getKey());
            System.out.println(headerDTO.getRequired());

            apiDto.headers.add(headerDTO);
        }

        for (RequestParameter requestParameter : api.getRequestParameters()) {
            RequestParameterDto requestParameterDTO = new RequestParameterDto();
            requestParameterDTO.setDescription(requestParameter.getDescription());
            requestParameterDTO.setKey(requestParameter.getKey());
            requestParameterDTO.setRequired(requestParameter.getRequired());

            apiDto.requestParameters.add(requestParameterDTO);
        }

        for (ResponseParameter responseParameter : api.getResponseParameters()) {
            ResponseParameterDto responseParameterDTO = new ResponseParameterDto();
            responseParameterDTO.setDescription(responseParameter.getDescription());
            responseParameterDTO.setKey(responseParameter.getKey());
            responseParameterDTO.setRequired(responseParameter.getRequired());

            apiDto.responseParameters.add(responseParameterDTO);
        }

        for (ErrorCode errorCode : api.getErrorCodes()) {
            ErrorCodeDto errorCodeDTO = new ErrorCodeDto();
            errorCodeDTO.setDescription(errorCode.getDescription());
            errorCodeDTO.setKey(errorCode.getKey());

            apiDto.errorCodes.add(errorCodeDTO);
        }

        return apiDto;
    }
}
