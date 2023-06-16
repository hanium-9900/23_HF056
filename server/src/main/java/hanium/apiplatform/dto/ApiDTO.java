package hanium.apiplatform.dto;

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
    private ArrayList<HeaderDTO> headers;
    private ArrayList<ParameterDTO> parameters;
    private ArrayList<ResponseParameterDTO> responseParameters;
    private ArrayList<ErrorCodeDTO> errorCodes;
}
