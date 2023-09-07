package hanium.apiplatform.service;

import hanium.apiplatform.dto.RequestParameterDto;
import hanium.apiplatform.entity.RequestParameter;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RequestParameterService {
    private final EntityManager em;

    public String updateRequestParameter(String requestParametersJson) {
        JSONArray requestParametersArray = new JSONArray(requestParametersJson);
        List<RequestParameterDto> updatedRequestParameterDtos = new ArrayList<>();

        // 문자열 형태의 request parameter json schema를 JSON 형태로 변환하여 업데이트
        for (int i = 0; i < requestParametersArray.length(); i++) {
            JSONObject requestParameterObj = requestParametersArray.getJSONObject(i);

            long requestId = requestParameterObj.getLong("id");
            RequestParameter requestParameter = em.find(RequestParameter.class, requestId);

            requestParameter.setDescription(requestParameterObj.getString("description"));
            requestParameter.setKey(requestParameterObj.getString("key"));
            requestParameter.setType(requestParameterObj.getString("type"));
            requestParameter.setRequired(requestParameterObj.getInt("required"));

            updatedRequestParameterDtos.add(RequestParameterDto.toDto(requestParameter));
        }

        // JSON 형태로 변환한 거 다시 문자열 형태로 변환
        JSONArray updatedArray = new JSONArray();
        for (RequestParameterDto dto : updatedRequestParameterDtos) {
            JSONObject dtoObject = new JSONObject();
            dtoObject.put("id", dto.getId());
            dtoObject.put("description", dto.getDescription());
            dtoObject.put("key", dto.getKey());
            dtoObject.put("type", dto.getType());
            dtoObject.put("required", dto.getRequired());
            updatedArray.put(dtoObject);
        }

        return updatedArray.toString();
    }
}
