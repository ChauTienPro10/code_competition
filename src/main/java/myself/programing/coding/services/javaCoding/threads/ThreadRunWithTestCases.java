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
import myself.programing.coding.services.javaCoding.JavaCompileService;
import myself.programing.coding.services.javaCoding.JavaRunCodeService;

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
    public List<RunWithTestCasesDto> runWithTest(String code, int idUser, List<TestCase> testCases) throws ExecutionException, InterruptedException {
        Callable<List<RunWithTestCasesDto>> runTask = () -> {
            StringBuilder output;
            JavaCompileService javaCompileService = new JavaCompileService();
            JavaRunCodeService javaRunCodeService = new JavaRunCodeService();
            try {
                String nameClass = javaCompileService.detectFileName(code);
                String filePath = javaCompileService.doCopyFileToContainer(
                        javaCompileService.generateJavaFile(nameClass, code, idUser),
                        idUser
                );
                List<RunWithTestCasesDto> resultDtoList = new ArrayList<>();
                output = new StringBuilder(javaCompileService.doCompileToClassFile(filePath));
                if(output.toString().contains(".class")) {
                    String classFilePath = output.toString();
                    output = new StringBuilder();
                    for (TestCase testCase : testCases) {
                        String rsRun = javaRunCodeService.doRunJavaClass(classFilePath, testCase.getInput());
                        if(rsRun.equals(testCase.getOutput())) {
                            resultDtoList.add(new RunWithTestCasesDto(rsRun, true));
                            continue;
                        }
                        resultDtoList.add(new RunWithTestCasesDto(rsRun, true));
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
