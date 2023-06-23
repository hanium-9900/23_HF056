package hanium.apiplatform.service;

import hanium.apiplatform.dto.ApiDto;
import hanium.apiplatform.dto.ServiceDto;
import hanium.apiplatform.dto.UserDto;
import hanium.apiplatform.entity.User;
import org.springframework.stereotype.Service;

@Service
public class ApiService {

    public boolean verifyApi(ApiDto apiDto) {
        // TODO: API 검증 로직 추가

        return true;
    }
}
