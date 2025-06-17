package myself.programing.coding.services.javaCoding.threads;

import myself.programing.coding.entity.TestCase;
import myself.programing.coding.services.javaCoding.JavaCompileService;
import myself.programing.coding.services.javaCoding.JavaRunCodeService;

import java.util.List;
import java.util.concurrent.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadsForJavaRunCode {

    @Autowired JavaCompileService javaCompileService;

    @Autowired JavaRunCodeService javaRunCodeService;

    /**
     *
     * @param code
     * @param idUser
     * @param challengeId
     * @return {@code CompletableFuture<String>}
     * @throws ExecutionException
     */
    @Async("taskExecutor")
    public CompletableFuture<String> runCode(String code, Long idUser, Long challengeId)
            throws ExecutionException {
        try {
            StringBuilder output;
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
            return CompletableFuture.completedFuture(output.toString());
        } catch (Throwable e) {
            throw new ExecutionException(e);
        }

    }

}
