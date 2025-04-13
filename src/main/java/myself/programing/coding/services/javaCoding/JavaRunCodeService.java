package myself.programing.coding.services.javaCoding;

import myself.programing.coding.enums.DOCKER_EXECUTE_TYPE_ERROR;
import myself.programing.coding.exception.DockerExecuteException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

@Service
public class JavaRunCodeService extends JavaBaseService{
    /**
     *
     * @param javaFilePath
     * @return String
     * @throws DockerExecuteException
     */
    public String doRunJavaClass(String javaFilePath, String input) throws DockerExecuteException {
        if (javaFilePath.isEmpty()) {
            throw new DockerExecuteException(DOCKER_EXECUTE_TYPE_ERROR.FILE_NOT_FOUND, "Invalid file");
        }
        String command = dockerServiceForJava.genRunFileJavaCmd(javaFilePath);
        return dockerServiceForJava.executeDockerCommandHasResult(command + " " + input);
    }
}
