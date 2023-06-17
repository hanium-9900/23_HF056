package hanium.apiplatform.controller;

import hanium.apiplatform.dto.ApiDTO;
import hanium.apiplatform.dto.ServiceDTO;
import hanium.apiplatform.dto.ErrorCodeDTO;
import hanium.apiplatform.dto.HeaderDTO;
import hanium.apiplatform.dto.RequestParameterDTO;
import hanium.apiplatform.dto.ResponseParameterDTO;
import hanium.apiplatform.entity.Api;
import hanium.apiplatform.entity.ErrorCode;
import hanium.apiplatform.entity.Header;
import hanium.apiplatform.entity.RequestParameter;
import hanium.apiplatform.entity.ResponseParameter;
import hanium.apiplatform.entity.Service;
import hanium.apiplatform.exception.UserNotFoundException;
import hanium.apiplatform.repository.ServiceRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin()
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final ServiceRepository serviceRepository;

    @GetMapping
    public ServiceDTO getService(@RequestParam Long id) {
        System.out.println("id = " + id.toString());
        Service service = serviceRepository.findById(id).orElseThrow(() -> new UserNotFoundException());

        return new ServiceDTO(service);
    }

    @PostMapping()
    public void registerApi(@RequestBody ServiceDTO serviceDTO) {
        String title = serviceDTO.getTitle();
        String description = serviceDTO.getDescription();
        int price = serviceDTO.getPrice();
        String key = serviceDTO.getKey();
        ArrayList<ApiDTO> apis = serviceDTO.getApis();

        // apis는 배열이고, 요소에 접근하고 싶으면 인덱싱한 후 위에처럼 .get(...)으로 접근하면 됨
        // ex) apis.get(0).getPath()

        System.out.println(title);
        System.out.println(description);
        System.out.println(price);
        System.out.println(key);
        System.out.println(apis);

        Service service = new Service();
        service.setTitle(title);
        service.setDescription(description);
        service.setPrice(price);
        service.setKey(key);

        for (ApiDTO apiDTO : apis) {
            Api api = new Api();
            api.setHost(apiDTO.getHost());
            api.setDescription(apiDTO.getDescription());
            api.setMethod(apiDTO.getMethod());
            api.setPath(apiDTO.getPath());

            for (HeaderDTO headerDTO : apiDTO.getHeaders()) {
                Header header = new Header();
                header.setDescription(headerDTO.getDescription());
                header.setKey(headerDTO.getKey());
                header.setRequired(headerDTO.getRequired());

                api.addHeader(header);
            }

            for (RequestParameterDTO parameterDTO : apiDTO.getRequestParameters()) {
                RequestParameter parameter = new RequestParameter();
                parameter.setDescription(parameterDTO.getDescription());
                parameter.setKey(parameterDTO.getKey());
                parameter.setRequired(parameterDTO.getRequired());

                api.addRequestParameter(parameter);
            }

            for (ResponseParameterDTO responseParameterDTO : apiDTO.getResponseParameters()) {
                ResponseParameter responseParameter = new ResponseParameter();
                responseParameter.setDescription(responseParameterDTO.getDescription());
                responseParameter.setKey(responseParameterDTO.getKey());
                responseParameter.setRequired(responseParameterDTO.getRequired());

                api.addResponseParameter(responseParameter);
            }

            for (ErrorCodeDTO errorCodeDTO : apiDTO.getErrorCodes()) {
                ErrorCode errorCode = new ErrorCode();
                errorCode.setDescription(errorCodeDTO.getDescription());
                errorCode.setKey(errorCodeDTO.getKey());

                api.addErrorCode(errorCode);
            }

            service.addApi(api);
        }

        serviceRepository.save(service);

    }
}
