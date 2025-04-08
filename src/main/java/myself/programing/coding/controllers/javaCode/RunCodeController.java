package myself.programing.coding.controllers.javaCode;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/java/run")
public class RunCodeController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
