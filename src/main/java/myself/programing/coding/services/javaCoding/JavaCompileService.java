package myself.programing.coding.services.javaCoding;

import myself.programing.coding.enums.DOCKER_EXECUTE_TYPE_ERROR;
import myself.programing.coding.exception.DockerExecuteException;
import org.springframework.stereotype.Service;

@Service
public class JavaCompileService extends JavaBaseService{

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
            String command = dockerServiceForJava.genCompileFileJavaCmd(pathInContainer);
            String result = dockerServiceForJava.executeDockerCommandHasResult(command);
            if(result.isEmpty()) {
                return pathInContainer.replace(".java", ".class");
            }
            return result;
        } finally {
            dockerServiceForJava.deleteFile(pathInContainer);
        }
    }
}
