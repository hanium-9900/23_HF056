package hanium.apiplatform.service;

import hanium.apiplatform.dto.ServiceDto;
import hanium.apiplatform.dto.UserDto;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class KeyIssueService {

    // TODO: Proxy service key 생성
    public String issueServiceKey(ServiceDto serviceDto, UserDto userDto){
        String result = "";
        try{
            // SHA256 알고리즘 객체 생성
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // service id와 user id를 합친 문자열에 SHA 256 적용
            md.update((serviceDto.getId() + "-" + userDto.getId()).getBytes());
            byte[] key = md.digest();

            // byte to string (2자리 16진수)
            StringBuffer sb = new StringBuffer();
            for(byte b : key){
                sb.append(String.format("%02x", b));
            }

            result = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    // TODO: Proxy service key 검증
}
