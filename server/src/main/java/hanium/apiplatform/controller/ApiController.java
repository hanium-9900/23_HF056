package hanium.apiplatform.controller;

import hanium.apiplatform.data.Api;
import hanium.apiplatform.data.ApiRegisterForm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin()
@RequestMapping("/api")
public class ApiController {

    @PostMapping()
    public void registerApi(@RequestBody ApiRegisterForm apiRegisterForm) {
        String title = apiRegisterForm.getTitle();
        String description = apiRegisterForm.getDescription();
        int price = apiRegisterForm.getPrice();
        String key = apiRegisterForm.getKey();
        ArrayList<Api> apis = apiRegisterForm.getApis();

        // apis는 배열이고, 요소에 접근하고 싶으면 인덱싱한 후 위에처럼 .get(...)으로 접근하면 됨
        // ex) apis.get(0).getPath()

        System.out.println(title);
        System.out.println(description);
        System.out.println(price);
        System.out.println(key);
        System.out.println(apis);
    }
}
