package hanium.apiplatform.dto;

import hanium.apiplatform.entity.Report;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReportDto {
    private Long id;
    private String reason;
    private ServiceDto service;
    private UserDto user;

    public static ReportDto toDto(Report report){
        ReportDto reportDto = new ReportDto();

        reportDto.setId(report.getId());
        reportDto.setReason(report.getReason());
        reportDto.setService(ServiceDto.toDto(report.getService()));
        reportDto.setUser(UserDto.toDto(report.getUser()));

        return reportDto;
    }
}
