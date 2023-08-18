package hanium.proxyapiserver.service;

import hanium.proxyapiserver.dto.HeaderDto;
import hanium.proxyapiserver.entity.Header;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HeaderService {
    private final EntityManager em;

    public List<HeaderDto> updateHeaderInfos(List<HeaderDto> headerDtos){
        List<HeaderDto> updatedHeaderDtos = new ArrayList<>();

        for (HeaderDto headerDto : headerDtos){
            Header header = em.find(Header.class, headerDto.getId());

            header.setDescription(headerDto.getDescription());
            header.setKey(header.getKey());
            header.setType(header.getType());
            header.setRequired(header.getRequired());

            updatedHeaderDtos.add(HeaderDto.toDto(header));
        }

        return updatedHeaderDtos;
    }
}
