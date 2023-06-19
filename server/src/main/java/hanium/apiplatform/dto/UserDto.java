package hanium.apiplatform.dto;

import hanium.apiplatform.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private String password;

    public static UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), null);
    }
}
