package myself.programing.coding.services.rustCoding.threads;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import myself.programing.coding.entity.TestCase;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.dockerService.DockerServiceForRust;
import myself.programing.coding.services.rustCoding.RustCompileService;
import myself.programing.coding.services.rustCoding.RustRunCodeService;

public class ThreadsForRustRunCode {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     *
     * @param code
     * @param idUser
     * @param testCases
     * @return String
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public String runCode(String code, Long idUser, List<TestCase> testCases) throws ExecutionException, InterruptedException {
        Callable<String> runTask = () -> {
            StringBuilder output;
            DockerServiceForRust dockerServiceForRust = new DockerServiceForRust();
            RustCompileService rustCompileService = new RustCompileService(dockerServiceForRust);
            RustRunCodeService rustRunCodeService = new RustRunCodeService(dockerServiceForRust);
            try {
                String nameClass = rustCompileService.detectFileName(code);
                String filePath = rustCompileService.doCopyFileToContainer(
                        rustCompileService.generateCodeFile(nameClass, code, idUser),
                        idUser
                );
                output = new StringBuilder(rustCompileService.doCompileToClassFile(filePath));
                if(output.toString().endsWith(".exe")) {
                    String classFilePath = output.toString();
                    output = new StringBuilder();
                    for (TestCase testCase : testCases) {
                        output.append(" ").append(rustRunCodeService.doRunFile(classFilePath, testCase.getInput()));
                    }
                }

                return output.toString();
            } catch (DockerExecuteException e) {
                throw new RuntimeException(e);
            }
        };
        Future<String> future = executorService.submit(runTask);
        String result = future.get();
        shutdown();
        return result;
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
