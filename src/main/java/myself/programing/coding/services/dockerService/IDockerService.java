package myself.programing.coding.services.dockerService;

import myself.programing.coding.exception.DockerExecuteException;

public interface IDockerService {

    void executeDockerCommand(String command) throws DockerExecuteException;

    String genTouchFolderCmd(String folder);

    void deleteFile(String path) throws DockerExecuteException;

    String generateCopyFileToContainerCmd(String srcPath, String destPath);

    String genCompileFileCmd(String filePath);

    String genRunFileCmd(String filePath);

}