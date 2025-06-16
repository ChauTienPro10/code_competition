package myself.programing.coding.services.dockerService;

import myself.programing.coding.consts.CONFIG;
import org.springframework.stereotype.Service;

@Service
public class DockerServiceForPython extends DockerBaseService implements
        IDockerServiceForCompileProcess, IDockerServiceForFileProcess{

    @Override
    public String genTouchFolderCmd(String folder) {
        return STR."\{DOCKER_EXEC}\{CONFIG.PYTHON_CONTAINER_NAME} mkdir -p /app/\{folder}";
    }

    @Override
    public String generateCopyFileToContainerCmd(String srcPath, String destPath) {
        return STR."docker cp \{srcPath} \{CONFIG.PYTHON_CONTAINER_NAME}\{WORKSPACE}/\{destPath}";
    }

    /**
     * Python3 - Sử dụng linux
     * @param filePath
     * @return String
     */
    @Override
    public String genCompileFileCmd(String filePath) {
        return STR."\{DOCKER_EXEC}\{CONFIG.PYTHON_CONTAINER_NAME} python3 -m compileall -q \{filePath}";
    }

    @Override
    public String genRunFileCmd(String filePath) {
        return STR."\{DOCKER_EXEC}\{CONFIG.PYTHON_CONTAINER_NAME} python3 \{filePath}";
    }
}
