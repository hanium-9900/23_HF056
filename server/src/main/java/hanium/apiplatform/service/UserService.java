package hanium.apiplatform.service;

import hanium.apiplatform.entity.User;
import hanium.apiplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    public final UserRepository userRepository;

    public void save(String email, String password) {
        userRepository.save(new User(email, password));
    }
}
