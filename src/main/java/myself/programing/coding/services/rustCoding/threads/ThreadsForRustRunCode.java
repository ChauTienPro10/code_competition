package myself.programing.coding.services.rustCoding.threads;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import java.util.concurrent.ExecutionException;
import myself.programing.coding.entity.TestCase;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.rustCoding.RustCompileService;
import myself.programing.coding.services.rustCoding.RustRunCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ThreadsForRustRunCode {

    @Autowired RustCompileService rustCompileService;
    @Autowired RustRunCodeService rustRunCodeService;

    @Async("taskExecutor")
    public CompletableFuture<String> runCode(String code, Long idUser, Long challengeId)
            throws ExecutionException {
        try {
            StringBuilder output;

            String nameClass = rustCompileService.detectFileName(code);
            String filePath = rustCompileService.doCopyFileToContainer(
                    rustCompileService.generateCodeFile(nameClass, code, idUser),
                    idUser
            );
            output = new StringBuilder(rustCompileService.doCompile(filePath));

            if (output.toString().endsWith(".exe")) {
                String classFilePath = output.toString();
                output = new StringBuilder();
                List<TestCase> testCases = rustRunCodeService.findAllTestCase(challengeId);
                for (TestCase testCase : testCases) {
                    output.append(" ").append(rustRunCodeService.doRunFile(classFilePath, testCase.getInput()));
                }
            }

            return CompletableFuture.completedFuture(output.toString());
        } catch (Throwable e) {
            throw new ExecutionException(e);
        }
    }
}
