package hanium.apiplatform.service;

import hanium.apiplatform.dto.UserDto;
import hanium.apiplatform.entity.User;
import hanium.apiplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    /*public User updateUserInfo(UserDto userDto){

    }*/

    public Optional<User> findByUserId(UserDto userDto){
        return userRepository.findById(userDto.getId());
    }
}