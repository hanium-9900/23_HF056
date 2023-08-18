package hanium.proxyapiserver.repository;

import hanium.proxyapiserver.entity.RequestParameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestParameterRepository extends JpaRepository<RequestParameter, Long> {

}
