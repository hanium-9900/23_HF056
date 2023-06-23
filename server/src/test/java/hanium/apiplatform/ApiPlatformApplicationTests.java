package hanium.apiplatform;

import hanium.apiplatform.dto.ApiDto;
import hanium.apiplatform.dto.ServiceDto;
import hanium.apiplatform.dto.UserDto;
import hanium.apiplatform.entity.Api;
import hanium.apiplatform.entity.Service;
import hanium.apiplatform.entity.User;
import hanium.apiplatform.repository.ServiceRepository;
import hanium.apiplatform.repository.UserRepository;
import hanium.apiplatform.service.KeyIssueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Collections;

@ActiveProfiles("test")
@ContextConfiguration(classes = {TestDatasourceConfig.class})
@SpringBootTest
@AutoConfigureMockMvc
class ApiPlatformApplicationTests {

	@Autowired
	ServiceRepository serviceRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	KeyIssueService keyIssueService;

	@Test
	void contextLoads() {
	}

	@Test
	@Rollback(value = true)
	void serviceKeyTest(){
		// 1
		ServiceDto serviceDto1 = new ServiceDto();
		serviceDto1.setTitle("test");
		serviceDto1.setDescription("test service");
		serviceDto1.setPrice(1000);
		serviceDto1.setKey("1q2w3e4r");

		serviceDto1 = ServiceDto.toDto(serviceRepository.save(Service.toEntity(serviceDto1)));

		UserDto userDto1 = UserDto.toDto(userRepository.save(User.builder()
				.email("test@mail.com")
				.password(passwordEncoder.encode("1q2w3e4r"))
				.roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER로 설정
				.build()));

		String testKey = serviceDto1.getId().toString() + "-" + userDto1.getId().toString();
		System.out.println("original string  => " + testKey);

		String hashResult = keyIssueService.issueServiceKey(serviceDto1, userDto1);
		System.out.println("hash result string => " + hashResult);

		// 2
		ServiceDto serviceDto2 = new ServiceDto();
		serviceDto2.setTitle("test2");
		serviceDto2.setDescription("test service2");
		serviceDto2.setPrice(1000);
		serviceDto2.setKey("1q2w3e4r");

		serviceDto2 = ServiceDto.toDto(serviceRepository.save(Service.toEntity(serviceDto2)));

		UserDto userDto2 = UserDto.toDto(userRepository.save(User.builder()
				.email("test2@mail.com")
				.password(passwordEncoder.encode("1q2w3e4r"))
				.roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER로 설정
				.build()));

		String testKey2 = serviceDto2.getId().toString() + "-" + userDto2.getId().toString();
		System.out.println("original string  => " + testKey);

		hashResult = keyIssueService.issueServiceKey(serviceDto2, userDto2);
		System.out.println("hash result string => " + hashResult);
	}

}
