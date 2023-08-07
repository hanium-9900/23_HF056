package hanium.apiplatform.repository;

import hanium.apiplatform.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findServicesByUserId(Long id);
}
