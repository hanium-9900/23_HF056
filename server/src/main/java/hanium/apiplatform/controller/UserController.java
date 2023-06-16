package hanium.apiplatform.controller;

import hanium.apiplatform.config.JwtTokenProvider;
import hanium.apiplatform.dto.UserDTO;
import hanium.apiplatform.entity.User;
import hanium.apiplatform.exception.DuplicateEmailException;
import hanium.apiplatform.exception.UserNotFoundException;
import hanium.apiplatform.exception.WrongPasswordException;
import hanium.apiplatform.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    // 회원가입
    @PostMapping("/join")
    public String join(@RequestBody UserDTO userDTO) {
        Optional<User> found = userRepository.findByEmail(userDTO.getEmail());

        if (found.isPresent()) {
            throw new DuplicateEmailException();
        }

        return userRepository.save(User.builder()
            .email(userDTO.getEmail())
            .password(passwordEncoder.encode(userDTO.getPassword()))
            .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
            .build()).getEmail();
    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail())
            .orElseThrow(() -> new UserNotFoundException());
        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new WrongPasswordException();
        }
        return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
    }
}
