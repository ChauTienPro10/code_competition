package myself.programing.coding.services.rustCoding.threads;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import myself.programing.coding.dto.RunWithTestCasesDto;
import myself.programing.coding.entity.TestCase;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.rustCoding.RustCompileService;
import myself.programing.coding.services.rustCoding.RustRunCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadRunWithTestCaseRust {

    @Autowired RustCompileService rustCompileService;
    @Autowired RustRunCodeService rustRunCodeService;

    /**
     *
     * @param code
     * @param idUser
     * @param challengeId
     * @return {@code CompletableFuture<List<RunWithTestCasesDto>>}
     * @throws DockerExecuteException
     */
    @Async("taskExecutor")
    public CompletableFuture<List<RunWithTestCasesDto>> runWithTest(String code, Long idUser, Long challengeId)
            throws DockerExecuteException {
        try {
            String nameClass = rustCompileService.detectFileName(code);
            String filePath = rustCompileService.doCopyFileToContainer(
                    rustCompileService.generateCodeFile(nameClass, code, idUser),
                    idUser
            );

            String compileResult = rustCompileService.doCompile(filePath);

            if (!compileResult.contains(".exe")) {
                return CompletableFuture.completedFuture(Collections.emptyList());
            }

            String executablePath = compileResult.trim();
            List<TestCase> testCases = rustRunCodeService.findAllTestCase(challengeId);

            List<CompletableFuture<RunWithTestCasesDto>> futures = testCases.stream()
                    .map(testCase -> CompletableFuture.supplyAsync(() -> {
                        try {
                            String actualOutput = rustRunCodeService.doRunFile(executablePath, testCase.getInput());
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
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }


}
