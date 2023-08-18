package hanium.proxyapiserver.service;

import hanium.proxyapiserver.dto.ResponseParameterDto;
import hanium.proxyapiserver.entity.ResponseParameter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ResponseParameterService {
    private final EntityManager em;

    public List<ResponseParameterDto> updateResponseParameters(List<ResponseParameterDto> responseParameterDtos){
        List<ResponseParameterDto> updatedResponseParameterDtos =
                new ArrayList<>();

        for (ResponseParameterDto responseParameterDto : responseParameterDtos){
            ResponseParameter responseParameter = em.find(ResponseParameter.class, responseParameterDto.getId());

            responseParameter.setDescription(responseParameterDto.getDescription());
            responseParameter.setKey(responseParameterDto.getKey());
            responseParameter.setType(responseParameterDto.getType());
            responseParameter.setRequired(responseParameterDto.getRequired());

            updatedResponseParameterDtos.add(ResponseParameterDto.toDto(responseParameter));
        }

        return updatedResponseParameterDtos;
    }
}
