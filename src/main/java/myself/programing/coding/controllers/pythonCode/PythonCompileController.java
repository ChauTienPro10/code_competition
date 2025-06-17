package myself.programing.coding.controllers.pythonCode;

import java.util.List;
import java.util.concurrent.ExecutionException;
import myself.programing.coding.controllers.ICompileController;
import myself.programing.coding.controllers.javaCode.JavaCompileController;
import myself.programing.coding.dto.CompileRequestDto;
import myself.programing.coding.dto.CompileResponse;
import myself.programing.coding.dto.HttpResponseApi;
import myself.programing.coding.dto.RunWithTestCasesDto;
import myself.programing.coding.enums.API_RESPONSE_STATUS;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.pythonCoding.threads.ThreadForPythonCompileCode;
import myself.programing.coding.services.pythonCoding.threads.ThreadForPythonRunCode;
import myself.programing.coding.utils.HandleStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/compile/python")
public class PythonCompileController implements ICompileController {

    @Autowired
    ThreadForPythonRunCode threadForPythonRunCode;

    @Autowired
    ThreadForPythonCompileCode threadForPythonCompileCode;

    private final Logger logger = LoggerFactory.getLogger(JavaCompileController.class);

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

    @Override
    public HttpResponseApi<CompileResponse> compile(CompileRequestDto request) {
        try {
            logInfo("*****START COMPILING*****");
            String resultCompile = String.valueOf(
                    threadForPythonCompileCode.compile(request.getCode(), request.getIdUser()).get());
            CompileResponse compileResponse = new CompileResponse(resultCompile);
            HttpResponseApi<CompileResponse> result = HttpResponseApi.<CompileResponse>builder()
                    .message(API_RESPONSE_STATUS.SUCCESS.getMessage())
                    .code(API_RESPONSE_STATUS.SUCCESS.getCode())
                    .data(compileResponse)
                    .build();
            logInfo("*****COMPILING SUCCESSFULLY*****");
            return result;
        } catch (DockerExecuteException e) {
            logInfo("*****COMPILING FAIL: " + e.getMessage()+ "*****");
            return HttpResponseApi.<CompileResponse>builder()
                    .code(API_RESPONSE_STATUS.ERROR_COMPILE.getCode())
                    .message(API_RESPONSE_STATUS.ERROR_COMPILE.getMessage())
                    .data(new CompileResponse(HandleStringUtils.getRightPartAfterFirstBracket(e.getMessage())))
                    .build();
        } catch (Exception e) {
            logError("*****COMPILING FAIL BY ERROR SYSTEM: " + e.getMessage()+ "*****");
            return HttpResponseApi.<CompileResponse>builder()
                    .code(API_RESPONSE_STATUS.SERVER_ERROR.getCode())
                    .message(API_RESPONSE_STATUS.SERVER_ERROR.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public HttpResponseApi<CompileResponse> run(CompileRequestDto request) {
        try {
            logInfo("*****START COMPILING AND RUN*****");
            String resultRun = String.valueOf(
                    threadForPythonRunCode.runCode(request.getCode(), request.getIdUser(), request.getChallengeId()).get());
            CompileResponse compileResponse = new CompileResponse(resultRun);
            HttpResponseApi<CompileResponse> result = HttpResponseApi.<CompileResponse>builder()
                    .message(API_RESPONSE_STATUS.SUCCESS.getMessage())
                    .code(API_RESPONSE_STATUS.SUCCESS.getCode())
                    .data(compileResponse)
                    .build();
            logInfo("*****RUN COMPETITION*****");
            return result;
        } catch (DockerExecuteException e) {
            logInfo("*****RUN FAIL: " + e.getMessage()+ "*****");
            return HttpResponseApi.<CompileResponse>builder()
                    .code(API_RESPONSE_STATUS.ERROR_COMPILE.getCode())
                    .message(API_RESPONSE_STATUS.ERROR_COMPILE.getMessage())
                    .data(new CompileResponse(HandleStringUtils.getRightPartAfterFirstBracket(e.getMessage())))
                    .build();
        } catch (Exception e) {
            logError("*****RUN FAIL BY ERROR SYSTEM: " + e.getMessage() + "*****");
            return HttpResponseApi.<CompileResponse>builder()
                    .code(API_RESPONSE_STATUS.SERVER_ERROR.getCode())
                    .message(API_RESPONSE_STATUS.SERVER_ERROR.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public HttpResponseApi<List<RunWithTestCasesDto>> runWithTests(CompileRequestDto request) {
        return null;
    }
}
