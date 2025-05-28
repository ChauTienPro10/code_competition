package myself.programing.coding.services.rustCoding.threads;

import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.dockerService.DockerServiceForJava;
import myself.programing.coding.services.javaCoding.JavaCompileService;

import java.util.concurrent.*;

public class ThreadsForRustCompileCode {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public String compile(String code, Long idUser) throws InterruptedException, ExecutionException {

        Callable<String> compileTask = () -> {
            JavaCompileService javaCompileService = new JavaCompileService(new DockerServiceForJava());
            try {
                String nameClass = javaCompileService.detectFileName(code);
                String filePath = javaCompileService.doCopyFileToContainer(
                        javaCompileService.generateCodeFile(nameClass, code, idUser),
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
