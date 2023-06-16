package hanium.apiplatform.dto;

import hanium.apiplatform.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private String email;
    private String password;

    public UserEntity toEntity() {
        return new UserEntity(email, password);
    }
}
