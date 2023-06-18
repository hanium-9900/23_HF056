package hanium.apiplatform.entity;

import hanium.apiplatform.dto.ApiDto;
import hanium.apiplatform.dto.ErrorCodeDto;
import hanium.apiplatform.dto.HeaderDto;
import hanium.apiplatform.dto.RequestParameterDto;
import hanium.apiplatform.dto.ResponseParameterDto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Api {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String host;

    @Column
    private String description;

    @Column
    private String method;

    @Column
    private String path;

    @OneToMany(mappedBy = "api", cascade = CascadeType.ALL)
    private List<Header> headers = new ArrayList<>();

    @OneToMany(mappedBy = "api", cascade = CascadeType.ALL)
    private List<RequestParameter> RequestParameters = new ArrayList<>();

    @OneToMany(mappedBy = "api", cascade = CascadeType.ALL)
    private List<ResponseParameter> responseParameters = new ArrayList<>();

    @OneToMany(mappedBy = "api", cascade = CascadeType.ALL)
    private List<ErrorCode> errorCodes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_id")
    private Service service;

    public static Api toEntity(ApiDto apiDto) {
        Api api = new Api();

        api.setHost(apiDto.getHost());
        api.setDescription(apiDto.getDescription());
        api.setMethod(apiDto.getMethod());
        api.setPath(apiDto.getPath());

        for (HeaderDto headerDto : apiDto.getHeaders()) {
            Header header = new Header();
            header.setDescription(headerDto.getDescription());
            header.setKey(headerDto.getKey());
            header.setType(headerDto.getType());
            header.setRequired(headerDto.getRequired());

            api.addHeader(header);
        }

        for (RequestParameterDto requestParameterDto : apiDto.getRequestParameters()) {
            RequestParameter requestParameter = new RequestParameter();
            requestParameter.setDescription(requestParameterDto.getDescription());
            requestParameter.setKey(requestParameterDto.getKey());
            requestParameter.setType(requestParameterDto.getType());
            requestParameter.setRequired(requestParameterDto.getRequired());

            api.addRequestParameter(requestParameter);
        }

        for (ResponseParameterDto responseParameterDto : apiDto.getResponseParameters()) {
            ResponseParameter responseParameter = new ResponseParameter();
            responseParameter.setDescription(responseParameterDto.getDescription());
            responseParameter.setKey(responseParameterDto.getKey());
            responseParameter.setType(responseParameterDto.getType());
            responseParameter.setRequired(responseParameterDto.getRequired());

            api.addResponseParameter(responseParameter);
        }

        for (ErrorCodeDto errorCodeDto : apiDto.getErrorCodes()) {
            ErrorCode errorCode = new ErrorCode();
            errorCode.setDescription(errorCodeDto.getDescription());
            errorCode.setKey(errorCodeDto.getKey());

            api.addErrorCode(errorCode);
        }

        return api;
    }

    public void addHeader(Header header) {
        this.headers.add(header);
        header.updateApi(this);
    }

    public void addRequestParameter(RequestParameter parameter) {
        this.RequestParameters.add(parameter);
        parameter.updateApi(this);
    }

    public void addResponseParameter(ResponseParameter responseParameter) {
        this.responseParameters.add(responseParameter);
        responseParameter.updateApi(this);
    }

    public void addErrorCode(ErrorCode errorCode) {
        this.errorCodes.add(errorCode);
        errorCode.updateApi(this);
    }

    public void updateService(Service service) {
        this.service = service;
    }
}
