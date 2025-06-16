package myself.programing.coding.services.dockerService;

import myself.programing.coding.consts.CONFIG;
import org.springframework.stereotype.Service;

@Service
public class DockerServiceForJava extends DockerBaseService implements
        IDockerServiceForCompileProcess, IDockerServiceForFileProcess{

    /**
     *
     * @param filePath
     * @return String
     */
    public String genCompileFileCmd(String filePath) {
        return STR."\{DOCKER_EXEC}\{CONFIG.JDK_CONTAINER_NAME} javac \{filePath}";
    }

    /**
     *
     * @param folder
     * @return String
     */
    public String genTouchFolderCmd(String folder) {
        return STR."\{DOCKER_EXEC}\{CONFIG.JDK_CONTAINER_NAME} mkdir -p /app/\{folder}";
    }

    /**
     *
     * @param filePath
     * @return String
     */
    public String genRunFileCmd(String filePath) {
        String folder = filePath.substring(0, filePath.lastIndexOf('/'));
        String className = filePath.substring(filePath.lastIndexOf('/') + 1, filePath.lastIndexOf('.'));
        return STR."\{DOCKER_EXEC}\{CONFIG.JDK_CONTAINER_NAME} java -cp \{folder} \{className}";
    }

    /**
     *
     * @param path
     * @param filePath
     * @return String
     */
    public String generateCopyFileToContainerCmd(String path, String filePath) {
        return STR."docker cp \{path} \{CONFIG.JDK_CONTAINER_NAME}\{WORKSPACE}/\{filePath}";
    }
}
