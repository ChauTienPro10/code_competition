package myself.programing.coding.services;

import lombok.AllArgsConstructor;
import myself.programing.coding.enums.DOCKER_EXECUTE_TYPE_ERROR;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.dockerService.IDockerServiceForCompileProcess;
import myself.programing.coding.services.dockerService.IDockerServiceForFileProcess;
import myself.programing.coding.utils.FileUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public abstract class AbstractCodingService<T extends IDockerServiceForCompileProcess & IDockerServiceForFileProcess> {

    protected T dockerService;

    /**
     *
     * @param className
     * @param codeString
     * @param idUser
     * @return String
     */
    public abstract String generateCodeFile(String className, String codeString, Long idUser) throws IOException;

    public String doCopyFileToContainer(String path, Long idUser) throws DockerExecuteException {
        if (path == null || path.isEmpty()) {
            logError("Invalid path : " + path);
            throw new DockerExecuteException(DOCKER_EXECUTE_TYPE_ERROR.UNKNOWN_ERROR, " Invalid: " + path);
        }
        String fileName = FileUtils.getFileNameFromPath(path);
        String folder = FileUtils.generateDockerFolderName(idUser);

        dockerService.executeDockerCommand(dockerService.genTouchFolderCmd(folder));
        String filePathInContainer = folder + "/" + fileName;
        dockerService.deleteFile(filePathInContainer);
        String command = dockerService.generateCopyFileToContainerCmd(path, filePathInContainer);
        dockerService.executeDockerCommand(command);

        return filePathInContainer;
    }

    public abstract String detectFileName(String code) throws DockerExecuteException;

    protected abstract void logError(String message);
}

