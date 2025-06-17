package myself.programing.coding.services.javaCoding.threads;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import myself.programing.coding.dto.RunWithTestCasesDto;
import myself.programing.coding.entity.TestCase;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.dockerService.DockerServiceForJava;
import myself.programing.coding.services.javaCoding.JavaCompileService;
import myself.programing.coding.services.javaCoding.JavaRunCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadRunWithTestCases {

    @Autowired JavaCompileService javaCompileService;
    @Autowired JavaRunCodeService javaRunCodeService;

    /**
     *
     * @param code
     * @param idUser
     * @param challengeId
     * @return {@code CompletableFuture<List<RunWithTestCasesDto>>}
     * @throws ExecutionException
     */
    @Async("taskExecutor")
    public CompletableFuture<List<RunWithTestCasesDto>> runWithTest(String code, Long idUser, Long challengeId)
            throws ExecutionException {
        try {
            String nameClass = javaCompileService.detectFileName(code);
            String filePath = javaCompileService.doCopyFileToContainer(
                    javaCompileService.generateCodeFile(nameClass, code, idUser),
                    idUser
            );
            String compileResult = (javaCompileService.doCompile(filePath));
            if (!compileResult.contains(".class")) {
                return CompletableFuture.completedFuture(Collections.emptyList());
            }
            String executablePath = compileResult.trim();
            List<TestCase> testCases = javaRunCodeService.findAllTestCase(challengeId);
            List<CompletableFuture<RunWithTestCasesDto>> futures = testCases.stream()
                    .map(testCase -> CompletableFuture.supplyAsync(() -> {
                        try {
                            String actualOutput = javaRunCodeService.doRunFile(executablePath, testCase.getInput());
                            String expectedOutput = testCase.getOutput().replaceAll("\\s+", "").trim();
                            String cleanedOutput = actualOutput.replaceAll("\\s+", "").trim();
                            boolean passed = expectedOutput.equals(cleanedOutput);
                            return new RunWithTestCasesDto(testCase.getInput(), testCase.getOutput(), passed);
                        } catch (Exception | DockerExecuteException e) {
                            return new RunWithTestCasesDto(testCase.getInput(), testCase.getOutput(), false); // Or log error
                        }
                    })).toList();

            // Ghép tất cả các future lại
            CompletableFuture<Void> allDone = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            return allDone.thenApply(v ->
                    futures.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.toList())
            );
        } catch (Throwable e) {
            throw new ExecutionException(e);
        }

    }

}
