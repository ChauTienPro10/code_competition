package myself.programing.coding.services.dockerService;

import myself.programing.coding.consts.CONFIG;
import myself.programing.coding.enums.DOCKER_EXECUTE_TYPE_ERROR;
import myself.programing.coding.exception.DockerExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class DockerBaseService {
    //    dockerBaseService Logger
    private final Logger logger = LoggerFactory.getLogger(DockerBaseService.class);
    protected final String DOCKER_EXEC = "docker exec " + CONFIG.JDK_CONTAINER_NAME;
    protected final String BASH = "bash";
    protected final String WORKSPACE = ":/app";

    /**
     *
     * @param command
     */
    public void executeDockerCommand(String command) throws DockerExecuteException {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                logInfo("Executed successfully for: " + command);
            } else {
                logError("Execute fail : " + command);
                throw new DockerExecuteException(DOCKER_EXECUTE_TYPE_ERROR.UNKNOWN_ERROR, "Can't finish: " + command);
            }
        } catch (IOException | InterruptedException e) {
            throw new DockerExecuteException(DOCKER_EXECUTE_TYPE_ERROR.UNKNOWN_ERROR, e);
        }
    }

    /**
     *
     * @param command
     * @return String
     */
    public String executeDockerCommandHasResult(String command) throws DockerExecuteException {
        Process process = null;
        BufferedReader reader = null;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.redirectErrorStream(true);
            process = processBuilder.start();

            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                logInfo("Executed successfully for: " + command);
                return output.toString();
            } else {
                logError("Execute fail: " + command + " with exit code " + exitCode);
                throw new DockerExecuteException(DOCKER_EXECUTE_TYPE_ERROR.UNKNOWN_ERROR, "[Can't finish: " + command + "] " + output);
            }
        } catch (IOException | InterruptedException e) {
            logError("Error executing Docker command: " + command + " - " + e.getMessage());
            throw new DockerExecuteException(DOCKER_EXECUTE_TYPE_ERROR.UNKNOWN_ERROR, e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (IOException e) {
                logError("Error closing resources: " + e.getMessage());
            }
        }
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
                + CONFIG.JDK_CONTAINER_NAME
                + WORKSPACE
                + "/"
                + filePath;
    }

    /**
     *
     * @param folder
     * @return String
     */
    public String genTouchFolderCmd(String folder) {
        return DOCKER_EXEC + " mkdir -p " + "/app/" + folder;
    }

    /**
     *
     * @param path
     * @throws DockerExecuteException
     */
    public void deleteFile(String path) throws DockerExecuteException {
        executeDockerCommand(genDeleteFileCmd(path));
    }

    /**
     *
     * @param path
     * @return String
     */
    public String genDeleteFileCmd(String path) {
        return DOCKER_EXEC + " rm -f " + path;
    }

    /**
     *
     * @param e
     */
    public void logInfo(String e) {
        logger.info(e);
    }

    /**
     *
     * @param e
     */
    public void logError(Throwable e) {
        logger.error(e.getMessage());
    }

    /**
     *
     * @param e
     */
    public void logError(String e) {
        logger.error(e);
    }
}
