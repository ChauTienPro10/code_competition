package myself.programing.coding.services.javaCoding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import myself.programing.coding.enums.DOCKER_EXECUTE_TYPE_ERROR;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.AbstractCodingService;
import myself.programing.coding.services.dockerService.DockerServiceForJava;
import myself.programing.coding.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JavaBaseService extends AbstractCodingService<DockerServiceForJava> {

    //    dockerBaseService Logger
    private final Logger logger = LoggerFactory.getLogger(JavaBaseService.class);

    public JavaBaseService(DockerServiceForJava dockerService) {
        super(dockerService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generateCodeFile(String className, String codeString, Long idUser) throws IOException {
        String filePath = "/fileStorage/java/" + idUser + "/" + className + ".java";
        return FileUtils.generateFile(codeString, filePath).toAbsolutePath().toString();
    }

    /**
     *
     * @param code
     * @return
     */
    @Override
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

        throw new DockerExecuteException(DOCKER_EXECUTE_TYPE_ERROR.COMMAND_FAILED, "Class name invalid!");
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
