package hanium.apiplatform.service;

import hanium.apiplatform.dto.ErrorCodeDto;
import hanium.apiplatform.entity.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ErrorCodeService {
    private final EntityManager em;

    public List<ErrorCodeDto> updateErrorCodes(List<ErrorCodeDto> errorCodeDtos){
        List<ErrorCodeDto> updatedErrorCodeDtos = new ArrayList<>();

        for (ErrorCodeDto errorCodeDto : errorCodeDtos){
            ErrorCode errorCode = em.find(ErrorCode.class, errorCodeDto.getId());

            errorCode.setDescription(errorCodeDto.getDescription());
            errorCode.setStatusCode(errorCodeDto.getStatusCode());

            updatedErrorCodeDtos.add(ErrorCodeDto.toDto(errorCode));
        }

        return updatedErrorCodeDtos;
    }
}
