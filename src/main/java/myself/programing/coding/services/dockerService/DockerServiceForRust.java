package myself.programing.coding.services.dockerService;

import myself.programing.coding.consts.CONFIG;
import org.springframework.stereotype.Service;

@Service
public class DockerServiceForRust extends DockerBaseService implements IDockerService {
    @Override
    public String genCompileFileCmd(String filePath) {
        return DOCKER_EXEC + CONFIG.RUST_CONTAINER_NAME + " rustc " + filePath + " -o" + filePath.replace(".rs", ".exe");
    }

    @Override
    public String genRunFileCmd(String filePath) {
        return DOCKER_EXEC + CONFIG.RUST_CONTAINER_NAME  + " " + filePath;
    }

    /**
     *
     * @param folder
     * @return String
     */
    public String genTouchFolderCmd(String folder) {
        return DOCKER_EXEC + CONFIG.RUST_CONTAINER_NAME + " mkdir -p " + "/app/" + folder;
    }

    /**
     *
     * @param path
     * @param filePath
     * @return String
     */
    public String generateCopyFileToContainerCmd(String path, String filePath) {
        return "docker cp "
                + path
                + " "
                + CONFIG.RUST_CONTAINER_NAME
                + WORKSPACE
                + "/"
                + filePath;
    }
}
