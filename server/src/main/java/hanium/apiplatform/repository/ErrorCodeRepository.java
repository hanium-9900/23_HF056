package hanium.apiplatform.repository;

import hanium.apiplatform.entity.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorCodeRepository extends JpaRepository<ErrorCode, Long> {

}
