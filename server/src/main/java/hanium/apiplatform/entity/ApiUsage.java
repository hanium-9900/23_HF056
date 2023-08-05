package hanium.apiplatform.entity;

import hanium.apiplatform.dto.ApiUsageDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ApiUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "api_id")
    private Api api;

    @Column
    private int responseCode;

    @Column
    @CreationTimestamp
    private Timestamp creationTimestamp;

    public static ApiUsage toEntity(ApiUsageDto apiUsageDto){
        ApiUsage apiUsage = new ApiUsage();

        apiUsage.setId(apiUsageDto.getId());
        apiUsage.setUser(User.toEntity(apiUsageDto.getUser()));
        apiUsage.setApi(Api.toEntity(apiUsageDto.getApi()));
        apiUsage.setResponseCode(apiUsageDto.getResponseCode());

        return apiUsage;
    }
}
