package myself.programing.coding.services.pythonCoding;

import java.io.IOException;
import myself.programing.coding.enums.DOCKER_EXECUTE_TYPE_ERROR;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.AbstractCodingService;
import myself.programing.coding.services.dockerService.DockerServiceForPython;
import myself.programing.coding.utils.FileUtils;
import org.springframework.stereotype.Service;

@Service
public class PythonBaseService extends AbstractCodingService<DockerServiceForPython> {

    public PythonBaseService(DockerServiceForPython dockerService) {
        super(dockerService);
    }

    @Override
    public String generateCodeFile(String className, String codeString, Long idUser)
            throws IOException {
        String filePath = "/fileStorage/python/" + idUser + "/" + className + ".py";
        return FileUtils.generateFile(codeString, filePath).toAbsolutePath().toString();
    }

    @Override
    public String detectFileName(String code) throws DockerExecuteException {
        if (code == null || code.isEmpty()) {
            throw new DockerExecuteException(DOCKER_EXECUTE_TYPE_ERROR.UNKNOWN_ERROR, "Code is emmppty!");
        }
        return "Main";
    }

    @Override
    protected void logError(String message) {

    }
}
