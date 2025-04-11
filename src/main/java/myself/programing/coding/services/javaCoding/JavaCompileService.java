package myself.programing.coding.services.javaCoding;

import java.io.File;
import myself.programing.coding.enums.DOCKER_EXECUTE_TYPE_ERROR;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.utils.FileUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

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
        String command = dockerServiceForJava.genCompileFileJavaCmd(pathInContainer);
        String result = dockerServiceForJava.executeDockerCommandHasResult(command);
        dockerServiceForJava.deleteFile(pathInContainer);
        return pathInContainer.replace(".java", ".class") + " " + result;
    }
}
