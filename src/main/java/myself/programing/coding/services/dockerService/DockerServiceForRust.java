package myself.programing.coding.services.dockerService;

import myself.programing.coding.consts.CONFIG;

public class DockerServiceForRust extends DockerBaseService implements IDockerService {
    @Override
    public String genCompileFileCmd(String filePath) {
        return "";
    }

    @Override
    public String genRunFileCmd(String filePath) {
        return "";
    }

    /**
     *
     * @param folder
     * @return String
     */
    public String genTouchFolderCmd(String folder) {
        return DOCKER_EXEC + CONFIG.RUST_CONTAINER_NAME + " mkdir -p " + "/app/" + folder;
    }
}
