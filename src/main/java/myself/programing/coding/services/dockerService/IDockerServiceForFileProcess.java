package myself.programing.coding.services.dockerService;

import myself.programing.coding.exception.DockerExecuteException;

public interface IDockerServiceForFileProcess {

    /**
     *
     * @param path
     * @throws DockerExecuteException
     */
    void deleteFile(String path) throws DockerExecuteException;

    /**
     *
     * @param srcPath
     * @param destPath
     * @return String
     */
    String generateCopyFileToContainerCmd(String srcPath, String destPath);

    /**
     *
     * @param folder
     * @return String
     */
    String genTouchFolderCmd(String folder);
}
