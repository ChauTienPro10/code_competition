package myself.programing.coding.services.pythonCoding.threads;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import myself.programing.coding.entity.TestCase;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.dockerService.DockerServiceForPython;
import myself.programing.coding.services.pythonCoding.PythonCompileService;
import myself.programing.coding.services.pythonCoding.PythonRunCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThreadForPythonRunCode {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired PythonRunCodeService pythonRunCodeService;

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
            DockerServiceForPython dockerServiceForPython = new DockerServiceForPython();
            PythonCompileService pythonCompileService = new PythonCompileService(dockerServiceForPython);

            try {
                String nameClass = pythonCompileService.detectFileName(code);
                String filePath = pythonCompileService.doCopyFileToContainer(
                        pythonCompileService.generateCodeFile(nameClass, code, idUser),
                        idUser
                );
                output = new StringBuilder(pythonCompileService.doCompile(filePath));
                if(output.toString().contains(".pyc")) {
                    String classFilePath = output.toString().replace("pyc", "py");
                    output = new StringBuilder();
                    List<TestCase> testCases = pythonRunCodeService.findAllTestCase(challengeId);
                    for (TestCase testCase : testCases) {
                        output.append(" ").append(pythonRunCodeService.doRunFile(classFilePath, testCase.getInput()));
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
