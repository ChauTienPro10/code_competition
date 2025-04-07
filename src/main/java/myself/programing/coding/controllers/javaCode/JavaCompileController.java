package myself.programing.coding.controllers.javaCode;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/java/compile")
public class JavaCompileController {

    @PostMapping("/")
    public String hello() {
        return "hello";
    }
}
