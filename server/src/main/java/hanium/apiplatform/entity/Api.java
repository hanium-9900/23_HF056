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

    @Column
    private String requestParameters;

    @Column
    private String responseParameters;

    @OneToMany(mappedBy = "api", cascade = CascadeType.ALL)
    private List<ErrorCode> errorCodes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_id")
    private Service service;

    @OneToMany(mappedBy = "api", cascade = CascadeType.ALL)
    private List<ApiUsage> apiUsages = new ArrayList<>();

    @Column
    private int limitation;

    public static Api toEntity(ApiDto apiDto) {
        Api api = new Api();

        api.setHost(apiDto.getHost());
        api.setDescription(apiDto.getDescription());
        api.setMethod(apiDto.getMethod());
        api.setPath(apiDto.getPath());
        api.setLimitation((apiDto.getLimitation()));

        for (HeaderDto headerDto : apiDto.getHeaders()) {
            Header header = new Header();
            header.setDescription(headerDto.getDescription());
            header.setKey(headerDto.getKey());
            header.setType(headerDto.getType());
            header.setRequired(headerDto.getRequired());

            api.addHeader(header);
        }

        api.setRequestParameters((apiDto.getRequestParameter()));
        api.setResponseParameters((apiDto.getResponseParameter()));

        for (ErrorCodeDto errorCodeDto : apiDto.getErrorCodes()) {
            ErrorCode errorCode = new ErrorCode();
            errorCode.setDescription(errorCodeDto.getDescription());
            errorCode.setStatusCode(errorCodeDto.getStatusCode());

            api.addErrorCode(errorCode);
        }

        return api;
    }

    public static ArrayList<Api> toEntity(ArrayList<ApiDto> apiDtos) {
        ArrayList<Api> apis = new ArrayList<>();

        for(ApiDto apiDto : apiDtos){
            Api api = new Api();

            api.setHost(apiDto.getHost());
            api.setDescription(apiDto.getDescription());
            api.setMethod(apiDto.getMethod());
            api.setPath(apiDto.getPath());
            api.setLimitation((apiDto.getLimitation()));

            for (HeaderDto headerDto : apiDto.getHeaders()) {
                Header header = new Header();
                header.setDescription(headerDto.getDescription());
                header.setKey(headerDto.getKey());
                header.setType(headerDto.getType());
                header.setRequired(headerDto.getRequired());

                api.addHeader(header);
            }

            api.setRequestParameters((apiDto.getRequestParameter()));
            api.setResponseParameters((apiDto.getResponseParameter()));

            for (ErrorCodeDto errorCodeDto : apiDto.getErrorCodes()) {
                ErrorCode errorCode = new ErrorCode();
                errorCode.setDescription(errorCodeDto.getDescription());
                errorCode.setStatusCode(errorCodeDto.getStatusCode());

                api.addErrorCode(errorCode);
            }

            apis.add(api);
        }

        return apis;
    }

    public void addHeader(Header header) {
        this.headers.add(header);
        header.updateApi(this);
    }

    public void addErrorCode(ErrorCode errorCode) {
        this.errorCodes.add(errorCode);
        errorCode.updateApi(this);
    }

    public void updateService(Service service) {
        this.service = service;
    }
}