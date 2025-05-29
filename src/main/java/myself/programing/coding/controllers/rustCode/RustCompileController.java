package myself.programing.coding.controllers.rustCode;

import java.util.List;
import java.util.concurrent.ExecutionException;
import myself.programing.coding.controllers.javaCode.JavaCompileController;
import myself.programing.coding.dto.CompileRequestDto;
import myself.programing.coding.dto.CompileResponse;
import myself.programing.coding.dto.HttpResponseApi;
import myself.programing.coding.dto.RunWithTestCasesDto;
import myself.programing.coding.enums.API_RESPONSE_STATUS;
import myself.programing.coding.repository.TestCaseRepository;
import myself.programing.coding.services.ChallengeService;
import myself.programing.coding.services.rustCoding.RustCompileService;
import myself.programing.coding.services.rustCoding.threads.ThreadRunWithTestCases;
import myself.programing.coding.services.rustCoding.threads.ThreadsForRustCompileCode;
import myself.programing.coding.services.rustCoding.threads.ThreadsForRustRunCode;
import myself.programing.coding.utils.HandleStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/compile/rust")
public class RustCompileController {
    @Autowired
    TestCaseRepository testCaseRepository;

    @Autowired
    ChallengeService challengeService;

    @Autowired
    RustCompileService rustCompileService;

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

    @PostMapping("/")
    public HttpResponseApi<CompileResponse> compile(@RequestBody CompileRequestDto request) {
        try {
            logInfo("*****START COMPILING*****");
            ThreadsForRustCompileCode theThreadsForRustCompileCode = new ThreadsForRustCompileCode();
            String resultCompile = theThreadsForRustCompileCode.compile(request.getCode(), request.getIdUser());
            CompileResponse compileResponse = new CompileResponse(resultCompile);
            HttpResponseApi<CompileResponse> result = HttpResponseApi.<CompileResponse>builder()
                    .message(API_RESPONSE_STATUS.SUCCESS.getMessage())
                    .code(API_RESPONSE_STATUS.SUCCESS.getCode())
                    .data(compileResponse)
                    .build();
            logInfo("*****COMPILING SUCCESSFULLY*****");
            return result;
        } catch (InterruptedException | ExecutionException e) {
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

    /**
     *
     * @param request
     * @return {@code HttpResponseApi<CompileResponse>}
     */
    @PostMapping("/run")
    public HttpResponseApi<CompileResponse> run(@RequestBody CompileRequestDto request) {
        try {
            logInfo("*****START COMPILING AND RUN*****");
            ThreadsForRustRunCode threadsForRustRunCode = new ThreadsForRustRunCode();
            String resultRun = threadsForRustRunCode.runCode(request.getCode(), request.getIdUser(), testCaseRepository.findByChallengeId(request.getChallengeId()));
            CompileResponse compileResponse = new CompileResponse(resultRun);
            HttpResponseApi<CompileResponse> result = HttpResponseApi.<CompileResponse>builder()
                    .message(API_RESPONSE_STATUS.SUCCESS.getMessage())
                    .code(API_RESPONSE_STATUS.SUCCESS.getCode())
                    .data(compileResponse)
                    .build();
            logInfo("*****RUN COMPETITION*****");
            return result;
        } catch (InterruptedException | ExecutionException e) {
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

    @PostMapping("/runWithTestcases")
    public HttpResponseApi<List<RunWithTestCasesDto>> runWithTests(@RequestBody CompileRequestDto request) {
        try {
            logInfo("*****START COMPILING AND RUN*****");
            ThreadRunWithTestCases threadRunWithTestCases = new ThreadRunWithTestCases();
            List<RunWithTestCasesDto> resultRun = threadRunWithTestCases.runWithTest(request.getCode(), request.getIdUser(), testCaseRepository.findByChallengeId(request.getChallengeId()));
            HttpResponseApi<List<RunWithTestCasesDto>> result = HttpResponseApi.<List<RunWithTestCasesDto>>builder()
                    .message(API_RESPONSE_STATUS.SUCCESS.getMessage())
                    .code(API_RESPONSE_STATUS.SUCCESS.getCode())
                    .data(resultRun)
                    .build();
            logInfo("*****RUN COMPETITION*****");
            return result;
        } catch (InterruptedException | ExecutionException e) {
            logInfo("*****RUN FAIL: " + e.getMessage()+ "*****");
            return HttpResponseApi.<List<RunWithTestCasesDto>>builder()
                    .code(API_RESPONSE_STATUS.ERROR_COMPILE.getCode())
                    .message(API_RESPONSE_STATUS.ERROR_COMPILE.getMessage())
                    .data(null)
                    .build();
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
