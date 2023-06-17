package hanium.apiplatform.repository;

import hanium.apiplatform.entity.Api;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiRepository extends JpaRepository<Api, Long> {

}
