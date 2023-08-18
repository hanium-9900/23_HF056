package hanium.proxyapiserver.repository;

import hanium.proxyapiserver.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    Optional<Service> findServiceByIdAndUserId(Long id, Long userId);

    List<Service> findServicesByUserId(Long id);

    List<Service> findByCategory(String category);
}
