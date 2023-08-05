package hanium.apiplatform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UsageRate {

    @Id
    @Column
    private Long id;

    @Column
    private String method;

    @Column
    private String path;

    @Column
    private double usage_rate;
}
