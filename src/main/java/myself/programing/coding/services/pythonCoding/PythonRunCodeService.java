package myself.programing.coding.services.pythonCoding;

import java.util.List;
import myself.programing.coding.entity.TestCase;
import myself.programing.coding.enums.DOCKER_EXECUTE_TYPE_ERROR;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.repository.TestCaseRepository;
import myself.programing.coding.services.dockerService.DockerServiceForPython;
import myself.programing.coding.services.interfaces.IRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class PythonRunCodeService extends PythonBaseService implements IRun {

    public PythonRunCodeService(
            DockerServiceForPython dockerService) {
        super(dockerService);
    }

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Override
    public String doRunFile(String javaFilePath, String input) throws DockerExecuteException {
        if (javaFilePath.isEmpty()) {
            throw new DockerExecuteException(DOCKER_EXECUTE_TYPE_ERROR.FILE_NOT_FOUND, "Invalid file");
        }
        String command = dockerService.genRunFileCmd(javaFilePath);
        return dockerService.executeDockerCommandHasResult(command + " " + input);
    }

    public List<TestCase> findAllTestCase(Long challengeId) {
        return testCaseRepository.findByChallengeId(challengeId);
    }
}
