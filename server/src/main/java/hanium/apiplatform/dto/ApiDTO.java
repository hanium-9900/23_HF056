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
public class ApiDTO {

    private String host;
    private String description;
    private String method;
    private String path;
    private ArrayList<HeaderDTO> headers = new ArrayList<>();
    private ArrayList<RequestParameterDTO> requestParameters = new ArrayList<>();
    private ArrayList<ResponseParameterDTO> responseParameters = new ArrayList<>();
    private ArrayList<ErrorCodeDTO> errorCodes = new ArrayList<>();

    public ApiDTO(Api api) {
        this.host = api.getHost();
        this.description = api.getDescription();
        this.method = api.getMethod();
        this.path = api.getPath();

        for (Header header : api.getHeaders()) {
            HeaderDTO headerDTO = new HeaderDTO();
            headerDTO.setDescription(header.getDescription());
            headerDTO.setKey(header.getKey());
            headerDTO.setRequired(header.getRequired());

            System.out.println(headerDTO.getDescription());
            System.out.println(headerDTO.getKey());
            System.out.println(headerDTO.getRequired());

            headers.add(headerDTO);
        }

        for (RequestParameter requestParameter : api.getRequestParameters()) {
            RequestParameterDTO requestParameterDTO = new RequestParameterDTO();
            requestParameterDTO.setDescription(requestParameter.getDescription());
            requestParameterDTO.setKey(requestParameter.getKey());
            requestParameterDTO.setRequired(requestParameter.getRequired());

            requestParameters.add(requestParameterDTO);
        }

        for (ResponseParameter responseParameter : api.getResponseParameters()) {
            ResponseParameterDTO responseParameterDTO = new ResponseParameterDTO();
            responseParameterDTO.setDescription(responseParameter.getDescription());
            responseParameterDTO.setKey(responseParameter.getKey());
            responseParameterDTO.setRequired(responseParameter.getRequired());

            responseParameters.add(responseParameterDTO);
        }

        for (ErrorCode errorCode : api.getErrorCodes()) {
            ErrorCodeDTO errorCodeDTO = new ErrorCodeDTO();
            errorCodeDTO.setDescription(errorCode.getDescription());
            errorCodeDTO.setKey(errorCode.getKey());

            errorCodes.add(errorCodeDTO);
        }
    }
}
