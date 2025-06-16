package myself.programing.coding.services.javaCoding.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import myself.programing.coding.dto.RunWithTestCasesDto;
import myself.programing.coding.entity.TestCase;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.dockerService.DockerServiceForJava;
import myself.programing.coding.services.javaCoding.JavaCompileService;
import myself.programing.coding.services.javaCoding.JavaRunCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThreadRunWithTestCases {
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
    public List<RunWithTestCasesDto> runWithTest(String code, Long idUser, Long challengeId) throws ExecutionException, InterruptedException {
        Callable<List<RunWithTestCasesDto>> runTask = () -> {
            StringBuilder output;

            try {
                String nameClass = javaCompileService.detectFileName(code);
                String filePath = javaCompileService.doCopyFileToContainer(
                        javaCompileService.generateCodeFile(nameClass, code, idUser),
                        idUser
                );
                List<RunWithTestCasesDto> resultDtoList = new ArrayList<>();
                output = new StringBuilder(javaCompileService.doCompile(filePath));
                if(output.toString().contains(".class")) {
                    String classFilePath = output.toString();
                    List<TestCase> testCases = javaRunCodeService.findAllTestCase(challengeId);
                    for (TestCase testCase : testCases) {
                        String rsRun = javaRunCodeService.doRunFile(classFilePath, testCase.getInput());
                        if(rsRun.replace("\n", "").equals(testCase.getOutput())) {
                            resultDtoList.add(new RunWithTestCasesDto(testCase.getInput(), testCase.getOutput(), true));
                            continue;
                        }
                        resultDtoList.add(new RunWithTestCasesDto(testCase.getInput(), testCase.getOutput(), false));
                    }
                }

                return resultDtoList;
            } catch (DockerExecuteException e) {
                throw new RuntimeException(e);
            }
        };
        Future<List<RunWithTestCasesDto>> future = executorService.submit(runTask);
        List<RunWithTestCasesDto> result = future.get();
        shutdown();
        return result;
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
