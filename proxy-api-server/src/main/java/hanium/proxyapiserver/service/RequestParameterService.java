package hanium.proxyapiserver.service;

import hanium.proxyapiserver.dto.RequestParameterDto;
import hanium.proxyapiserver.entity.RequestParameter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RequestParameterService {
    private final EntityManager em;

    public List<RequestParameterDto> updateRequestParameters
            (List<RequestParameterDto> requestParameterDtos){
        List<RequestParameterDto> updatedRequestParameterDtos
                = new ArrayList<>();

        for (RequestParameterDto requestParameterDto : requestParameterDtos){
            RequestParameter requestParameter
                    = em.find(RequestParameter.class, requestParameterDto.getId());

            requestParameter.setDescription(requestParameterDto.getDescription());
            requestParameter.setKey(requestParameterDto.getKey());
            requestParameter.setType(requestParameterDto.getType());
            requestParameter.setRequired(requestParameterDto.getRequired());

            updatedRequestParameterDtos.add(RequestParameterDto.toDto(requestParameter));
        }

        return updatedRequestParameterDtos;
    }
}
