package hanium.proxyapiserver.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ResponseParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String description;

    @Column
    private String key;

    @Column
    private String type;

    @Column
    private int required;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "api_id")
    private Api api;

    public void updateApi(Api api) {
        this.api = api;
    }
}
