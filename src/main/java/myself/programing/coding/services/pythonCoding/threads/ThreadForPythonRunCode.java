package myself.programing.coding.services.pythonCoding.threads;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import myself.programing.coding.entity.TestCase;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.pythonCoding.PythonCompileService;
import myself.programing.coding.services.pythonCoding.PythonRunCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadForPythonRunCode {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired PythonCompileService pythonCompileService;
    @Autowired PythonRunCodeService pythonRunCodeService;

    /**
     *
     * @param code
     * @param idUser
     * @param challengeId
     * @return {@code CompletableFuture<String>}
     * @throws DockerExecuteException
     * @throws IOException
     */
    @Async("taskExecutor")
    public CompletableFuture<String> runCode(String code, Long idUser, Long challengeId)
            throws DockerExecuteException, IOException {
            StringBuilder output;
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
            return CompletableFuture.completedFuture(output.toString());
    }
}
