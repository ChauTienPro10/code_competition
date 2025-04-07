package myself.programing.coding.services.javaCoding;

import myself.programing.coding.enums.DOCKER_EXECUTE_TYPE_ERROR;
import myself.programing.coding.exception.DockerExecuteException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

@Service
public class JavaCompileService extends JavaBaseService{

    /**
     *
     * @param filePath
     * @return Path
     * @throws IOException
     * @throws InterruptedException
     */
    public Path compileJavaFile(Path filePath) throws IOException, InterruptedException {
        String fileName = filePath.getFileName().toString();
        String className = fileName.replace(".java", "");
        Path parentDir = filePath.getParent() != null ? filePath.getParent() : Path.of(".");

        ProcessBuilder pb = new ProcessBuilder("javac", fileName);
        pb.directory(parentDir.toFile());
        pb.redirectErrorStream(true);

        Process process = pb.start();

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        int exitCode = process.waitFor();

        if (exitCode == 0) {
            return parentDir.resolve(className + ".class");
        } else {
            throw new RuntimeException("Compilation failed:\n" + output);
        }
    }

    /**
     *
     * @param pathInContainer
     * @return String
     * @throws DockerExecuteException
     */
    public String doCompileToClassFile(String pathInContainer) throws DockerExecuteException {
        if (pathInContainer.isEmpty()) {
            throw new DockerExecuteException(DOCKER_EXECUTE_TYPE_ERROR.UNKNOWN_ERROR, " Invalid path: " + pathInContainer);
        }
        String command = dockerServiceForJava.genCompileFileJavaCmd(pathInContainer);
        dockerServiceForJava.executeDockerCommandHasResult(command);
        return pathInContainer.replace(".java", ".class");
    }
}
