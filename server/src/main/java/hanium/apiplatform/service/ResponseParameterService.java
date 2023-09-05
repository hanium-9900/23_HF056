package hanium.apiplatform.service;

import hanium.apiplatform.dto.ResponseParameterDto;
import hanium.apiplatform.entity.ResponseParameter;
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
public class ResponseParameterService {
    private final EntityManager em;

    public String updateResponseParameter(String responseParametersJson) {
        JSONArray responseParametersArray = new JSONArray(responseParametersJson);
        List<ResponseParameterDto> updatedResponseParameterDtos = new ArrayList<>();

        // 문자열 형태의 response parameter json schema를 JSON 형태로 변환하여 업데이트
        for (int i = 0; i < responseParametersArray.length(); i++) {
            JSONObject responseParameterObj = responseParametersArray.getJSONObject(i);

            long responseId = responseParameterObj.getLong("id");
            ResponseParameter responseParameter = em.find(ResponseParameter.class, responseId);

            responseParameter.setDescription(responseParameterObj.getString("description"));
            responseParameter.setKey(responseParameterObj.getString("key"));
            responseParameter.setType(responseParameterObj.getString("type"));
            responseParameter.setRequired(responseParameterObj.getInt("required"));

            updatedResponseParameterDtos.add(ResponseParameterDto.toDto(responseParameter));
        }

        // JSON 형태로 변환한 거 다시 문자열 형태로 변환
        JSONArray updatedArray = new JSONArray();
        for (ResponseParameterDto dto : updatedResponseParameterDtos) {
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
