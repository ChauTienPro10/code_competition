package myself.programing.coding.services.javaCoding.threads;

import myself.programing.coding.entity.TestCase;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.dockerService.DockerServiceForJava;
import myself.programing.coding.services.javaCoding.JavaCompileService;
import myself.programing.coding.services.javaCoding.JavaRunCodeService;

import java.util.List;
import java.util.concurrent.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThreadsForJavaRunCode {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired JavaCompileService javaCompileService;

    @Autowired JavaRunCodeService javaRunCodeService;

    /**
     *
     * @param code
     * @param idUser
     * @param challengeId
     * @return String
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public String runCode(String code, Long idUser, Long challengeId) throws ExecutionException, InterruptedException {
        Callable<String> runTask = () -> {
            StringBuilder output;
            try {
                String nameClass = javaCompileService.detectFileName(code);
                String filePath = javaCompileService.doCopyFileToContainer(
                        javaCompileService.generateCodeFile(nameClass, code, idUser),
                        idUser
                );
                output = new StringBuilder(javaCompileService.doCompile(filePath));
                if(output.toString().contains(".class")) {
                    String classFilePath = output.toString();
                    output = new StringBuilder();
                    List<TestCase> testCases = javaRunCodeService.findAllTestCase(challengeId);
                    for (TestCase testCase : testCases) {
                        output.append(" ").append(javaRunCodeService.doRunFile(classFilePath, testCase.getInput()));
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
