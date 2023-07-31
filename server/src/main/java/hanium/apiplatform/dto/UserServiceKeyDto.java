package hanium.apiplatform.dto;

import hanium.apiplatform.entity.UserServiceKey;
import hanium.apiplatform.entity.Service;
import hanium.apiplatform.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserServiceKeyDto {
    private Long id;
    private Service service;
    private User user;
    private String key;

    public static UserServiceKeyDto toDto(UserServiceKey userServiceKey){
        UserServiceKeyDto userServiceKeyDto = new UserServiceKeyDto();
        userServiceKeyDto.setId(userServiceKey.getId());
        userServiceKeyDto.setService(userServiceKey.getService());
        userServiceKeyDto.setUser(userServiceKey.getUser());
        userServiceKeyDto.setKey(userServiceKey.getKey());

        return userServiceKeyDto;
    }
}
