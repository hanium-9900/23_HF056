package hanium.apiplatform.repository;

import hanium.apiplatform.entity.Service;
import hanium.apiplatform.entity.User;
import hanium.apiplatform.entity.UserServiceKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserServiceKeyRepository extends JpaRepository<UserServiceKey, Long> {
    List<UserServiceKey> findByService_IdAndUser_Id(long serviceId, long userId);
    List<UserServiceKey> findByKey(String key);
    List<UserServiceKey> findByUser_Id(long userId);
}
