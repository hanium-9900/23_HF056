package hanium.apiplatform.controller;

import hanium.apiplatform.config.JwtTokenProvider;
import hanium.apiplatform.dto.UserDto;
import hanium.apiplatform.entity.User;
import hanium.apiplatform.exception.DuplicateEmailException;
import hanium.apiplatform.exception.UserNotFoundException;
import hanium.apiplatform.exception.WrongPasswordException;
import hanium.apiplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

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
        User found = userRepository.findByEmail(userDto.getEmail()).orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(userDto.getPassword(), found.getPassword())) {
            throw new WrongPasswordException();
        }
        return jwtTokenProvider.createToken(found.getUsername(), found.getRoles());
    }

    @GetMapping("/me")
    public UserDto join(HttpServletRequest header) {
        String userToken = jwtTokenProvider.resolveToken(header);
        if (userToken != null && jwtTokenProvider.validateToken(userToken)) {
            // 유효한 토큰이면 user data 추출
            User user = userRepository.findByEmail(jwtTokenProvider.getUserPk(userToken)).orElseThrow(UserNotFoundException::new);
            return UserDto.toDto(user);
        }
        return null;
    }
}
