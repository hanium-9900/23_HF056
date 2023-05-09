package hanium.apiplatform.data;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Api {

    private String host;
    private String description;
    private String method;
    private String path;
    private ArrayList<Header> headers;
    private ArrayList<Parameter> parameters;
    private ArrayList<ResponseParameter> responseParameters;
    private ArrayList<ErrorCode> errorCodes;
}
