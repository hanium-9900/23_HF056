package hanium.proxyapiserver.repository;

import hanium.proxyapiserver.entity.ApiUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiUsageRepository extends JpaRepository<ApiUsage, Long> {

}
