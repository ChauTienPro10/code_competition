package myself.programing.coding.services.rustCoding;

import java.util.List;
import myself.programing.coding.entity.TestCase;
import myself.programing.coding.enums.DOCKER_EXECUTE_TYPE_ERROR;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.repository.TestCaseRepository;
import myself.programing.coding.services.dockerService.DockerServiceForRust;
import myself.programing.coding.services.interfaces.IRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RustRunCodeService extends RustBaseService implements IRun {

    public RustRunCodeService(
            DockerServiceForRust dockerService) {
        super(dockerService);
    }

    @Autowired
    TestCaseRepository testCaseRepository;

    /**
     *
     * @param rustFilePath
     * @return String
     * @throws DockerExecuteException
     */
    public String doRunFile(String rustFilePath, String input) throws DockerExecuteException {
        if (rustFilePath.isEmpty()) {
            throw new DockerExecuteException(DOCKER_EXECUTE_TYPE_ERROR.FILE_NOT_FOUND, "Invalid file");
        }
        String command = dockerService.genRunFileCmd(rustFilePath);
        return dockerService.executeDockerCommandHasResult(command + " " + input);
    }

    @Override
    public List<TestCase> findAllTestCase(Long challengeId) {
        return testCaseRepository.findByChallengeId(challengeId);
    }
}
