package hanium.apiplatform.controller;

import hanium.apiplatform.config.JwtTokenProvider;
import hanium.apiplatform.dto.UserDto;
import hanium.apiplatform.entity.User;
import hanium.apiplatform.exception.DuplicateEmailException;
import hanium.apiplatform.exception.UserNotFoundException;
import hanium.apiplatform.exception.WrongPasswordException;
import hanium.apiplatform.repository.UserRepository;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin()
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @PostMapping("/join")
    public UserDto join(@RequestBody UserDto userDto) {
        Optional<User> found = userRepository.findByEmail(userDto.getEmail());
        
        if (found.isPresent()) {
            throw new DuplicateEmailException();
        }

        return UserDto.toDto(userRepository.save(User.builder()
            .email(userDto.getEmail())
            .password(passwordEncoder.encode(userDto.getPassword()))
            .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER로 설정
            .build()));
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDto userDto) {
        User found = userRepository.findByEmail(userDto.getEmail()).orElseThrow(() -> new UserNotFoundException());

        if (!passwordEncoder.matches(userDto.getPassword(), found.getPassword())) {
            throw new WrongPasswordException();
        }
        return jwtTokenProvider.createToken(found.getUsername(), found.getRoles());
    }
}
