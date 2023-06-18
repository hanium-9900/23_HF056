package hanium.apiplatform.dto;

import hanium.apiplatform.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private String email;
    private String password;

    public User toEntity() {
        return new User(email, password);
    }
}
