package hanium.apiplatform.repository;

import hanium.apiplatform.entity.Service;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {

    List<Service> findByCategory(String category);
}
