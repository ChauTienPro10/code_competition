package myself.programing.coding.services.rustCoding;

import myself.programing.coding.enums.DOCKER_EXECUTE_TYPE_ERROR;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.dockerService.DockerServiceForRust;
import org.springframework.stereotype.Service;

@Service
public class RustCompileService extends RustBaseService {

    public RustCompileService(DockerServiceForRust dockerService) {
        super(dockerService);
    }

    /**
     *
     * @param pathInContainer
     * @return String
     * @throws DockerExecuteException
     */
    public String doCompileToClassFile(String pathInContainer) throws DockerExecuteException {
        if (pathInContainer.isEmpty()) {
            throw new DockerExecuteException(DOCKER_EXECUTE_TYPE_ERROR.UNKNOWN_ERROR, " Invalid path: " + pathInContainer);
        }
        try {
            String command = dockerService.genCompileFileCmd(pathInContainer);
            String result = dockerService.executeDockerCommandHasResult(command);
            if(result.isEmpty()) {
                return pathInContainer.replace(".rs", ".exe");
            }
            return result;
        } finally {
            dockerService.deleteFile(pathInContainer);
        }
    }

}
