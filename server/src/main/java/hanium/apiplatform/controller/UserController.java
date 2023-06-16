package hanium.apiplatform.controller;

import hanium.apiplatform.dto.UserDTO;
import hanium.apiplatform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin()
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping()
    public String save(@RequestBody UserDTO userDTO) {
        userService.save(userDTO.getEmail(), userDTO.getPassword());
        return "success";
    }
}
