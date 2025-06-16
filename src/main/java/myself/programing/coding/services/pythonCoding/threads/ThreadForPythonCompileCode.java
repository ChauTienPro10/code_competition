package myself.programing.coding.services.pythonCoding.threads;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.pythonCoding.PythonCompileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThreadForPythonCompileCode {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired PythonCompileService pythonCompileService;
    /**
     *
     * @param code
     * @return String
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public String compile(String code, Long idUser) throws InterruptedException, ExecutionException {

        Callable<String> compileTask = () -> {
            try {
                String nameClass = pythonCompileService.detectFileName(code);
                String filePath = pythonCompileService.doCopyFileToContainer(
                        pythonCompileService.generateCodeFile(nameClass, code, idUser),
                        idUser
                );
                return pythonCompileService.doCompile(filePath);
            } catch (DockerExecuteException e) {
                throw new RuntimeException(e);
            }
        };
        Future<String> future = executorService.submit(compileTask);
        String result = future.get();
        shutdown();
        return result;
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
