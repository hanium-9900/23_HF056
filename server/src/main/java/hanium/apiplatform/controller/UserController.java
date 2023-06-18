package hanium.apiplatform.controller;

import hanium.apiplatform.config.JwtTokenProvider;
import hanium.apiplatform.dto.UserDto;
import hanium.apiplatform.entity.User;
import hanium.apiplatform.exception.DuplicateEmailException;
import hanium.apiplatform.exception.UserNotFoundException;
import hanium.apiplatform.exception.WrongPasswordException;
import hanium.apiplatform.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@RestController
@CrossOrigin()
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @PostMapping("/join")
    public String join(@RequestBody UserDto userDto) {
        Optional<User> found = userRepository.findByEmail(userDto.getEmail());

        if (found.isPresent()) {
            throw new DuplicateEmailException();
        }

        return userRepository.save(User.builder()
            .email(userDto.getEmail())
            .password(passwordEncoder.encode(userDto.getPassword()))
            .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER로 설정
            .build()).getEmail();
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail())
            .orElseThrow(() -> new UserNotFoundException());
        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new WrongPasswordException();
        }
        return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
    }
}
