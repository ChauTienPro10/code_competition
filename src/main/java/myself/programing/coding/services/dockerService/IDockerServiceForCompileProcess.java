package myself.programing.coding.services.dockerService;

import myself.programing.coding.exception.DockerExecuteException;

public interface IDockerServiceForCompileProcess {

    /**
     *
     * @param command
     * @throws DockerExecuteException
     */
    void executeDockerCommand(String command) throws DockerExecuteException;

    /**
     *
     * @param filePath
     * @return String
     */
    String genCompileFileCmd(String filePath);

    /**
     *
     * @param filePath
     * @return String
     */
    String genRunFileCmd(String filePath);

}