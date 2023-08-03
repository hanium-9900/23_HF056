package hanium.apiplatform.repository;

import hanium.apiplatform.entity.ApiUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiUsageRepository extends JpaRepository<ApiUsage, Long> {

}
