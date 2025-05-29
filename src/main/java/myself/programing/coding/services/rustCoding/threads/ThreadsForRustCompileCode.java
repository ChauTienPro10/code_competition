package myself.programing.coding.services.rustCoding.threads;

import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.dockerService.DockerServiceForRust;

import java.util.concurrent.*;
import myself.programing.coding.services.rustCoding.RustCompileService;

public class ThreadsForRustCompileCode {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public String compile(String code, Long idUser) throws InterruptedException, ExecutionException {

        Callable<String> compileTask = () -> {
            RustCompileService rustCompileService = new RustCompileService(new DockerServiceForRust());
            try {
                String nameClass = rustCompileService.detectFileName(code);
                String filePath = rustCompileService.doCopyFileToContainer(
                        rustCompileService.generateCodeFile(nameClass, code, idUser),
                        idUser
                );
                return rustCompileService.doCompileToClassFile(filePath);
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
