package hanium.apiplatform.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ErrorLog {

    @Id
    @Column
    private Long id;

    @Column
    private String method;

    @Column
    private String path;

    @Column
    private int response_code;

    @Column
    private Timestamp creation_timestamp;
}
