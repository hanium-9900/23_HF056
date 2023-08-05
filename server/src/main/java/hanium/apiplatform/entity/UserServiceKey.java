package hanium.apiplatform.entity;

import hanium.apiplatform.dto.UserServiceKeyDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserServiceKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String key;

    public static UserServiceKey toEntity(UserServiceKeyDto userServiceKeyDto){
        UserServiceKey userServiceKey = new UserServiceKey();

        userServiceKey.setId(userServiceKeyDto.getId());
        userServiceKey.setService(Service.toEntity(userServiceKeyDto.getService()));
        userServiceKey.setUser(User.toEntity(userServiceKeyDto.getUser()));
        userServiceKey.setKey(userServiceKey.getKey());

        return userServiceKey;
    }
}
