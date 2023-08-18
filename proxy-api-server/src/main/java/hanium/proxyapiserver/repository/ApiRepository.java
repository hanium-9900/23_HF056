package hanium.proxyapiserver.repository;

import hanium.proxyapiserver.entity.Api;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApiRepository extends JpaRepository<Api, Long> {
    List<Api> findByPath(String path);
}
