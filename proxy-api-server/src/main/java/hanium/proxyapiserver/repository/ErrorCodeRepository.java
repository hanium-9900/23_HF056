package hanium.proxyapiserver.repository;

import hanium.proxyapiserver.entity.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorCodeRepository extends JpaRepository<ErrorCode, Long> {

}
