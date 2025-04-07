package myself.programing.coding.services.javaCoding.threads;

import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.javaCoding.JavaCompileService;

import java.util.concurrent.*;

public class ThreadForJavaCompileCode {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public String compile(String filePath) throws ExecutionException, InterruptedException {
        Callable<String> compileTask = () -> {
            JavaCompileService javaCompileService = new JavaCompileService();
            try {
                return javaCompileService.doCompileToClassFile(filePath);
            } catch (DockerExecuteException e) {
                throw new RuntimeException(e);
            }
        };
        Future<String> future = executorService.submit(compileTask);
        return future.get();
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
