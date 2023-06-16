package hanium.apiplatform.service;

import hanium.apiplatform.dto.UserDTO;
import hanium.apiplatform.entity.UserEntity;
import hanium.apiplatform.exception.AlreadyExistsException;
import hanium.apiplatform.repository.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    public final UserRepository userRepository;

    public UserEntity join(UserDTO userDTO) {
        List<UserEntity> users = userRepository.findByEmail(userDTO.getEmail());

        if (!users.isEmpty()) {
            throw new AlreadyExistsException("Duplicate email");
        }
        return userRepository.save(userDTO.toEntity());
    }
}
