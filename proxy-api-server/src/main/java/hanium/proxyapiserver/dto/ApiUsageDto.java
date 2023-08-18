package hanium.proxyapiserver.dto;

import hanium.proxyapiserver.entity.ApiUsage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiUsageDto {
    private Long id;
    private UserDto user;
    private ApiDto api;
    private int responseCode;
    private Timestamp creationTimestamp;

    public static ApiUsageDto toDto(ApiUsage apiUsage) {
        ApiUsageDto apiUsageDto = new ApiUsageDto();

        apiUsageDto.setId(apiUsage.getId());
        apiUsageDto.setUser(UserDto.toDto(apiUsage.getUser()));
        apiUsageDto.setApi(ApiDto.toDto(apiUsage.getApi()));
        apiUsageDto.setResponseCode(apiUsage.getResponseCode());
        apiUsageDto.setCreationTimestamp(apiUsage.getCreationTimestamp());

        return apiUsageDto;
    }
}
