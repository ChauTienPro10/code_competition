package myself.programing.coding.services.interfaces;

import java.util.List;
import myself.programing.coding.entity.TestCase;
import myself.programing.coding.exception.DockerExecuteException;

public interface IRun {
    String doRunFile(String javaFilePath, String input) throws DockerExecuteException;
    List<TestCase> findAllTestCase(Long challengeId);
}
