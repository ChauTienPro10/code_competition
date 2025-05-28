package myself.programing.coding.services.javaCoding;

import myself.programing.coding.enums.DOCKER_EXECUTE_TYPE_ERROR;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.dockerService.DockerServiceForJava;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

@Service
public class JavaRunCodeService extends JavaBaseService{


    public JavaRunCodeService(DockerServiceForJava dockerService) {
        super(dockerService);
    }

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
        String command = dockerService.genRunFileCmd(javaFilePath);
        return dockerService.executeDockerCommandHasResult(command + " " + input);
    }
}
