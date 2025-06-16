package myself.programing.coding.services.pythonCoding;

import myself.programing.coding.enums.DOCKER_EXECUTE_TYPE_ERROR;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.dockerService.DockerServiceForPython;
import myself.programing.coding.services.interfaces.ICompile;
import org.springframework.stereotype.Service;

@Service
public class PythonCompileService extends PythonBaseService implements ICompile {

    public PythonCompileService(
            DockerServiceForPython dockerService) {
        super(dockerService);
    }


    @Override
    public String doCompile(String pathInContainer) throws DockerExecuteException {
        if (pathInContainer.isEmpty()) {
            throw new DockerExecuteException(DOCKER_EXECUTE_TYPE_ERROR.UNKNOWN_ERROR, " Invalid path: " + pathInContainer);
        }
        try {
            String command = dockerService.genCompileFileCmd(pathInContainer);
            String result = dockerService.executeDockerCommandHasResult(command);
            if(result.isEmpty()) {
                return pathInContainer.replace(".py", ".pyc");
            }
            return result;
        } finally {
            dockerService.deleteFile(pathInContainer);
        }
    }
}
