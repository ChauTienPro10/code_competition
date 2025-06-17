package myself.programing.coding.services.pythonCoding.threads;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.pythonCoding.PythonCompileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadForPythonCompileCode {

    @Autowired PythonCompileService pythonCompileService;

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
        String nameClass = pythonCompileService.detectFileName(code);
        String filePath = pythonCompileService.doCopyFileToContainer(
                pythonCompileService.generateCodeFile(nameClass, code, idUser),
                idUser
        );
        String rs = pythonCompileService.doCompile(filePath);
        return CompletableFuture.completedFuture(rs);
    }
}
