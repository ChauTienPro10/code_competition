package myself.programing.coding.services.javaCoding.threads;

import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.javaCoding.JavaCompileService;

import java.util.concurrent.*;

public class ThreadForJavaCompileCode {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     *
     * @param code
     * @return String
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public String compile(String code, Long idUser) throws InterruptedException, ExecutionException {

        Callable<String> compileTask = () -> {
            JavaCompileService javaCompileService = new JavaCompileService();
            try {
                String nameClass = javaCompileService.detectFileName(code);
                String filePath = javaCompileService.doCopyFileToContainer(
                        javaCompileService.generateJavaFile(nameClass, code, idUser),
                        idUser
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
