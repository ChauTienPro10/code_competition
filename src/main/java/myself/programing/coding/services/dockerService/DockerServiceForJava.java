package myself.programing.coding.services.dockerService;

import org.springframework.stereotype.Service;

@Service
public class DockerServiceForJava extends DockerBaseService{

    /**
     *
     * @return String
     */
    public String GenCheckJavaVersionCmd() {
        return DOCKER_EXEC + " java -version";
    }

    /**
     *
     * @param filePath
     * @return String
     */
    public String genCompileFileJavaCmd(String filePath) {
        return DOCKER_EXEC + " javac " + filePath;
    }

    /**
     *
     * @param filePath
     * @return String
     */
    public String genRunFileJavaCmd(String filePath) {
        String folder = filePath.substring(0, filePath.lastIndexOf('/'));
        String className = filePath.substring(filePath.lastIndexOf('/') + 1, filePath.lastIndexOf('.'));
        return DOCKER_EXEC + " java -cp " + folder + " " + className;
    }
}
