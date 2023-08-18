package hanium.proxyapiserver.dto;

import hanium.proxyapiserver.entity.UserServiceKey;
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
    private ServiceDto service;
    private UserDto user;
    private String key;

    public static UserServiceKeyDto toDto(UserServiceKey userServiceKey){
        UserServiceKeyDto userServiceKeyDto = new UserServiceKeyDto();
        userServiceKeyDto.setId(userServiceKey.getId());
        userServiceKeyDto.setService(ServiceDto.toDto(userServiceKey.getService()));
        userServiceKeyDto.setUser(UserDto.toDto(userServiceKey.getUser()));
        userServiceKeyDto.setKey(userServiceKey.getKey());

        return userServiceKeyDto;
    }
}
