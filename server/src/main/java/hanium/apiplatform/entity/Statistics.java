package hanium.apiplatform.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Statistics implements Serializable {

    @Id
    @Column
    private Long api_id;

    @Column
    private String method;

    @Column
    private String path;

    @Id
    @Column
    private int month;

    @Id
    @Column
    private int day;

    @Column
    private int response_code;

    @Column
    private int count;
}
