package hanium.apiplatform.service;

import hanium.apiplatform.dto.ReportDto;
import hanium.apiplatform.dto.ServiceDto;
import hanium.apiplatform.dto.UserDto;
import hanium.apiplatform.entity.Report;
import hanium.apiplatform.entity.User;
import hanium.apiplatform.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {
    private final ReportRepository reportRepository;

    public boolean isReportDuplicated(ReportDto reportDto){
        if(reportRepository.findByService_IdAndUser_IdAndReason(
                reportDto.getService().getId(),
                reportDto.getUser().getId(),
                reportDto.getReason()).size() > 0){
            return true;
        }
        else return false;
    }

    public ReportDto saveReport
            (ReportDto reportDto, User user, hanium.apiplatform.entity.Service service){
        Report report = new Report();
        report.setReason(reportDto.getReason());
        report.setService(service);
        report.setUser(user);

        return ReportDto.toDto(reportRepository.save(report));
    }
}
