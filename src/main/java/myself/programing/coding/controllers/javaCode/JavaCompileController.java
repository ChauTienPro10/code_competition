package myself.programing.coding.controllers.javaCode;

import java.util.List;
import java.util.concurrent.ExecutionException;
import myself.programing.coding.controllers.ICompileController;
import myself.programing.coding.dto.CompileRequestDto;
import myself.programing.coding.dto.CompileResponse;
import myself.programing.coding.dto.HttpResponseApi;
import myself.programing.coding.dto.RunWithTestCasesDto;
import myself.programing.coding.enums.API_RESPONSE_STATUS;
import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.javaCoding.threads.ThreadForJavaCompileCode;
import myself.programing.coding.services.javaCoding.threads.ThreadRunWithTestCases;
import myself.programing.coding.services.javaCoding.threads.ThreadsForJavaRunCode;
import myself.programing.coding.utils.HandleExeptionUtils;
import myself.programing.coding.utils.HandleStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/compile/java")
public class JavaCompileController implements ICompileController {

    @Autowired
    ThreadForJavaCompileCode threadForJavaCompileCode;

    @Autowired ThreadsForJavaRunCode threadsForJavaRunCode;

    @Autowired ThreadRunWithTestCases threadRunWithTestCases;

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

    /**
     *
     * @param request
     * @return {@code HttpResponseApi<CompileResponse>}
     */
    public HttpResponseApi<CompileResponse> compile(@RequestBody CompileRequestDto request) {
        try {
            logInfo("*****START COMPILING*****");
            String resultCompile =
                    String.valueOf(threadForJavaCompileCode.compile(request.getCode(), request.getIdUser()).get());
            CompileResponse compileResponse = new CompileResponse(resultCompile);
            HttpResponseApi<CompileResponse> result = HttpResponseApi.<CompileResponse>builder()
                    .message(API_RESPONSE_STATUS.SUCCESS.getMessage())
                    .code(API_RESPONSE_STATUS.SUCCESS.getCode())
                    .data(compileResponse)
                    .build();
            logInfo("*****COMPILING SUCCESSFULLY*****");
            return result;
        } catch (ExecutionException e) {
            Throwable actual = HandleExeptionUtils.unwrap(e);
            logInfo("*****COMPILING FAIL: " + e.getMessage() + "*****");
            if (actual instanceof DockerExecuteException dex) {
                return HttpResponseApi.<CompileResponse>builder()
                        .code(API_RESPONSE_STATUS.ERROR_COMPILE.getCode())
                        .message(API_RESPONSE_STATUS.ERROR_COMPILE.getMessage())
                        .data(new CompileResponse(HandleStringUtils.getRightPartAfterFirstBracket(dex.getMessage())))
                        .build();
            } else {
                return HttpResponseApi.<CompileResponse>builder()
                        .code(API_RESPONSE_STATUS.ERROR_COMPILE.getCode())
                        .message(API_RESPONSE_STATUS.ERROR_COMPILE.getMessage())
                        .data(new CompileResponse(HandleStringUtils.getRightPartAfterFirstBracket(actual.getMessage())))
                        .build();
            }
        }
        catch (Exception e) {
            logError("*****COMPILING FAIL BY ERROR SYSTEM: " + e.getMessage()+ "*****");
            return HttpResponseApi.<CompileResponse>builder()
                    .code(API_RESPONSE_STATUS.SERVER_ERROR.getCode())
                    .message(API_RESPONSE_STATUS.SERVER_ERROR.getMessage())
                    .data(null)
                    .build();
        }
    }

    /**
     *
     * @param request
     * @return {@code HttpResponseApi<CompileResponse>}
     */
    public HttpResponseApi<CompileResponse> run(@RequestBody CompileRequestDto request) {
        try {
            logInfo("*****START COMPILING AND RUN*****");
            String resultRun = String.valueOf(
                    threadsForJavaRunCode.runCode(request.getCode(), request.getIdUser(), request.getChallengeId()).get());
            CompileResponse compileResponse = new CompileResponse(resultRun);
            HttpResponseApi<CompileResponse> result = HttpResponseApi.<CompileResponse>builder()
                    .message(API_RESPONSE_STATUS.SUCCESS.getMessage())
                    .code(API_RESPONSE_STATUS.SUCCESS.getCode())
                    .data(compileResponse)
                    .build();
            logInfo("*****RUN COMPETITION*****");
            return result;
        } catch (ExecutionException e) {
            Throwable actual = HandleExeptionUtils.unwrap(e);
            logInfo("*****RUN FAIL: " + e.getMessage()+ "*****");
            if (actual instanceof DockerExecuteException dex) {
                return HttpResponseApi.<CompileResponse>builder()
                        .code(API_RESPONSE_STATUS.ERROR_COMPILE.getCode())
                        .message(API_RESPONSE_STATUS.ERROR_COMPILE.getMessage())
                        .data(new CompileResponse(HandleStringUtils.getRightPartAfterFirstBracket(dex.getMessage())))
                        .build();
            } else {
                return HttpResponseApi.<CompileResponse>builder()
                        .code(API_RESPONSE_STATUS.ERROR_COMPILE.getCode())
                        .message(API_RESPONSE_STATUS.ERROR_COMPILE.getMessage())
                        .data(new CompileResponse(HandleStringUtils.getRightPartAfterFirstBracket(actual.getMessage())))
                        .build();
            }
        } catch (Exception e) {
            logError("*****RUN FAIL BY ERROR SYSTEM: " + e.getMessage() + "*****");
            return HttpResponseApi.<CompileResponse>builder()
                    .code(API_RESPONSE_STATUS.SERVER_ERROR.getCode())
                    .message(API_RESPONSE_STATUS.SERVER_ERROR.getMessage())
                    .data(null)
                    .build();
        }
    }

    /**
     *
     * @param request
     * @return {@code HttpResponseApi<List<RunWithTestCasesDto>>}
     */
    public HttpResponseApi<List<RunWithTestCasesDto>> runWithTests(@RequestBody CompileRequestDto request) {
        try {
            logInfo("*****START COMPILING AND RUN*****");
            List<RunWithTestCasesDto> resultRun = threadRunWithTestCases.runWithTest(request.getCode(), request.getIdUser(), request.getChallengeId()).get();
            HttpResponseApi<List<RunWithTestCasesDto>> result = HttpResponseApi.<List<RunWithTestCasesDto>>builder()
                    .message(API_RESPONSE_STATUS.SUCCESS.getMessage())
                    .code(API_RESPONSE_STATUS.SUCCESS.getCode())
                    .data(resultRun)
                    .build();
            logInfo("*****RUN COMPETITION*****");
            return result;
        } catch (Exception e) {
            logError("*****RUN FAIL BY ERROR SYSTEM: " + e.getMessage() + "*****");
            return HttpResponseApi.<List<RunWithTestCasesDto>>builder()
                    .code(API_RESPONSE_STATUS.SERVER_ERROR.getCode())
                    .message(API_RESPONSE_STATUS.SERVER_ERROR.getMessage())
                    .data(null)
                    .build();
        }
    }
}
