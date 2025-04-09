package myself.programing.coding.controllers.javaCode;

import myself.programing.coding.dto.CompileRequestJavaDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/java/compile")
public class JavaCompileController {

    @PostMapping("/")
    public String compile(@RequestBody CompileRequestJavaDto request) {
        System.out.println(request.getCode());
        return request.getLanguage();
    }
}
