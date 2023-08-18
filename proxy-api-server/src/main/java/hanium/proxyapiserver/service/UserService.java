package hanium.proxyapiserver.service;

import hanium.proxyapiserver.dto.UserDto;
import hanium.proxyapiserver.entity.User;
import hanium.proxyapiserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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