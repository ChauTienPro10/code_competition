package myself.programing.coding.services.javaCoding.threads;

import myself.programing.coding.entity.TestCase;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.javaCoding.JavaCompileService;
import myself.programing.coding.services.javaCoding.JavaRunCodeService;

import java.util.List;
import java.util.concurrent.*;

public class ThreadsForJavaRunCode {
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
    public String runCode(String code, int idUser, List<TestCase> testCases) throws ExecutionException, InterruptedException {
        Callable<String> runTask = () -> {
            StringBuilder output;
            JavaCompileService javaCompileService = new JavaCompileService();
            JavaRunCodeService javaRunCodeService = new JavaRunCodeService();
            try {
                String nameClass = javaCompileService.detectFileName(code);
                String filePath = javaCompileService.doCopyFileToContainer(
                        javaCompileService.generateJavaFile(nameClass, code, idUser),
                        idUser
                );
                output = new StringBuilder(javaCompileService.doCompileToClassFile(filePath));
                if(output.toString().contains(".class")) {
                    String classFilePath = output.toString();
                    output = new StringBuilder();
                    for (TestCase testCase : testCases) {
                        output.append(" ").append(javaRunCodeService.doRunJavaClass(classFilePath, testCase.getInput()));
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
