package myself.programing.coding.services.rustCoding;

import myself.programing.coding.enums.DOCKER_EXECUTE_TYPE_ERROR;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.dockerService.DockerServiceForRust;
import org.springframework.stereotype.Service;

@Service
public class RustRunCodeService extends RustBaseService {

    public RustRunCodeService(
            DockerServiceForRust dockerService) {
        super(dockerService);
    }

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
}
