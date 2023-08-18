package hanium.apiplatform.entity;

import hanium.apiplatform.dto.ReportDto;
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
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String reason;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public static Report toEntity(ReportDto reportDto){
        Report report = new Report();

        report.id = reportDto.getId();
        report.reason = reportDto.getReason();
        report.service = Service.toEntity(reportDto.getService());
        report.user = User.toEntity(reportDto.getUser());

        return report;
    }
}
