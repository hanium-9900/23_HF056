package hanium.apiplatform.repository;

import hanium.apiplatform.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByService_IdAndUser_IdAndReason(long serviceId, long userId, String reason);
}
