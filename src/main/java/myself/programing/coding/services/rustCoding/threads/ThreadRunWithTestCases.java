package myself.programing.coding.services.rustCoding.threads;

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
import myself.programing.coding.services.dockerService.DockerServiceForRust;
import myself.programing.coding.services.javaCoding.JavaCompileService;
import myself.programing.coding.services.javaCoding.JavaRunCodeService;
import myself.programing.coding.services.rustCoding.RustCompileService;
import myself.programing.coding.services.rustCoding.RustRunCodeService;

public class ThreadRunWithTestCases {
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
    public List<RunWithTestCasesDto> runWithTest(String code, Long idUser, List<TestCase> testCases) throws ExecutionException, InterruptedException {
        Callable<List<RunWithTestCasesDto>> runTask = () -> {
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
                List<RunWithTestCasesDto> resultDtoList = new ArrayList<>();
                output = new StringBuilder(rustCompileService.doCompileToClassFile(filePath));
                if(output.toString().contains(".exe")) {
                    String classFilePath = output.toString();
                    for (TestCase testCase : testCases) {
                        String rsRun = rustRunCodeService.doRunFile(classFilePath, testCase.getInput());
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
