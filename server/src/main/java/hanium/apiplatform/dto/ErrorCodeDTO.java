package hanium.apiplatform.dto;

import hanium.apiplatform.entity.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorCodeDTO {

    private String description;
    private String key;
}
