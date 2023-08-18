package hanium.proxyapiserver.repository;

import hanium.proxyapiserver.entity.Header;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeaderRepository extends JpaRepository<Header, Long> {

}
