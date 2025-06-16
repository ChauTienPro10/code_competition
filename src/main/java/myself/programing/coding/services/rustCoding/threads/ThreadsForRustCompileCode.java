package myself.programing.coding.services.rustCoding.threads;

import java.io.IOException;
import myself.programing.coding.exception.DockerExecuteException;

import java.util.concurrent.*;
import myself.programing.coding.services.rustCoding.RustCompileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadsForRustCompileCode {

    @Autowired
    RustCompileService rustCompileService;

    @Async("taskExecutor")
    public CompletableFuture<String> compile(String code, Long idUser) {
        try {
            String nameClass = rustCompileService.detectFileName(code);
            String filePath = rustCompileService.doCopyFileToContainer(
                    rustCompileService.generateCodeFile(nameClass, code, idUser),
                    idUser
            );
            String result = rustCompileService.doCompile(filePath);
            return CompletableFuture.completedFuture(result);
        } catch (DockerExecuteException | IOException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

}
