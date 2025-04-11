package myself.programing.coding.services.javaCoding.threads;

import lombok.extern.slf4j.Slf4j;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.javaCoding.JavaCompileService;

import java.util.concurrent.*;

@Slf4j
public class ThreadForJavaCompileCode {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     *
     * @param code
     * @return String
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public String compile(String code) throws InterruptedException, ExecutionException {

        Callable<String> compileTask = () -> {
            JavaCompileService javaCompileService = new JavaCompileService();
            try {
                String nameClass = javaCompileService.detectFileName(code);
                String filePath = javaCompileService.doCopyFileToContainer(
                        javaCompileService.generateJavaFile(nameClass, code, 1002),
                        1002
                );
                return javaCompileService.doCompileToClassFile(filePath);
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
