package myself.programing.coding.services.javaCoding;

import myself.programing.coding.enums.DOCKER_EXECUTE_TYPE_ERROR;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.dockerService.DockerServiceForJava;
import org.springframework.stereotype.Service;

@Service
public class JavaCompileService extends JavaBaseService{

    public JavaCompileService(DockerServiceForJava dockerService) {
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
            String command = dockerService.genCompileFileJavaCmd(pathInContainer);
            String result = dockerService.executeDockerCommandHasResult(command);
            if(result.isEmpty()) {
                return pathInContainer.replace(".java", ".class");
            }
            return result;
        } finally {
            dockerService.deleteFile(pathInContainer);
        }
    }
}
