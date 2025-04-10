package myself.programing.coding.services.javaCoding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import myself.programing.coding.enums.DOCKER_EXECUTE_TYPE_ERROR;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.dockerService.DockerServiceForJava;
import myself.programing.coding.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

@Service
public class JavaBaseService {

    //    dockerBaseService Logger
    private final Logger logger = LoggerFactory.getLogger(JavaBaseService.class);

    @Autowired
    protected DockerServiceForJava dockerServiceForJava  = new DockerServiceForJava();

    /**
     *
     * @param className
     * @param codeString
     * @param idUser
     * @return Path
     * @throws IOException
     */
    public String generateJavaFile(String className, String codeString, int idUser) throws IOException {
        String filePath = "/fileStorage/java/" + idUser + "/" + className + ".java";
        return FileUtils.generateFile(codeString, filePath).toAbsolutePath().toString();
    }

    /**
     *
     * @param code
     * @return
     */
    public String detectFileName(String code) throws DockerExecuteException {
        if (code == null || code.isEmpty()) {
            throw new DockerExecuteException(DOCKER_EXECUTE_TYPE_ERROR.UNKNOWN_ERROR, "Code is emmppty!");
        }

        String publicClassPattern = "\\bpublic\\s+class\\s+(\\w+)";
        Pattern pattern = Pattern.compile(publicClassPattern);
        Matcher matcher = pattern.matcher(code);

        if (matcher.find()) {
            return matcher.group(1);
        }

        String classPattern = "\\bclass\\s+(\\w+)";
        pattern = Pattern.compile(classPattern);
        matcher = pattern.matcher(code);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    /**
     *
     * @param directoryPath
     * @throws IOException
     */
    public void deleteJavaFile(String directoryPath) throws IOException {
        Path startPath = Path.of(directoryPath);
        Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".java") || file.toString().endsWith(".class")) {
                    Files.delete(file);
                }
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * @param path
     * @return String
     */
    public String doCopyFileToContainer(String path, int idUser) throws DockerExecuteException {
            if (path == null || path.isEmpty()) {
                logError("Invalid path : " + path);
                throw new DockerExecuteException(DOCKER_EXECUTE_TYPE_ERROR.UNKNOWN_ERROR, " Invalid: " + path);
            }
            String fileName = FileUtils.getFileNameFromPath(path);
            String folder = FileUtils.generateDockerFolderName(idUser);
            dockerServiceForJava.executeDockerCommand(dockerServiceForJava.genTouchFolderCmd(folder));
            String filePathInContainer = folder + "/" + fileName;
            String command = dockerServiceForJava.generateCopyFileToContainerCmd(path, filePathInContainer);
            dockerServiceForJava.executeDockerCommand(command);
            return filePathInContainer;
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
