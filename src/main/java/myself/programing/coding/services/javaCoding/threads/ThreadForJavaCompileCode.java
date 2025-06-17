package myself.programing.coding.services.javaCoding.threads;

import java.io.IOException;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.dockerService.DockerServiceForJava;
import myself.programing.coding.services.javaCoding.JavaCompileService;

import java.util.concurrent.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadForJavaCompileCode {

    @Autowired JavaCompileService javaCompileService;

    /**
     *
     * @param code
     * @param idUser
     * @return {@code CompletableFuture<String>}
     * @throws DockerExecuteException
     * @throws IOException
     */
    @Async("taskExecutor")
    public CompletableFuture<String> compile(String code, Long idUser)
            throws DockerExecuteException, IOException {
        String nameClass = javaCompileService.detectFileName(code);
        String filePath = javaCompileService.doCopyFileToContainer(
                javaCompileService.generateCodeFile(nameClass, code, idUser),
                idUser
        );
        String rs = javaCompileService.doCompile(filePath);
        return CompletableFuture.completedFuture(rs);
    }
}
