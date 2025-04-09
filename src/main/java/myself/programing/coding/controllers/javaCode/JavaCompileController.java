package myself.programing.coding.controllers.javaCode;

import java.io.IOException;
import myself.programing.coding.dto.CompileRequestJavaDto;
import myself.programing.coding.dto.CompileResponse;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.javaCoding.JavaCompileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/java/compile")
public class JavaCompileController {

    @Autowired private JavaCompileService javaCompileService;

    @PostMapping("/")
    public CompileResponse compile(@RequestBody CompileRequestJavaDto request)
            throws DockerExecuteException, IOException {
        String nameClass = javaCompileService.detectFileName(request.getCode());
        String filePath = javaCompileService.doCopyFileToContainer(javaCompileService.generateJavaFile(nameClass, request.getCode(), 1002), 1002);

        System.out.println(filePath);
        return null;
    }
}
