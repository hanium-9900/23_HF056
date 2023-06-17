package hanium.apiplatform.entity;

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
