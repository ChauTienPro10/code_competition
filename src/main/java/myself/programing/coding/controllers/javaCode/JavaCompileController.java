package myself.programing.coding.controllers.javaCode;

import java.util.List;
import java.util.concurrent.ExecutionException;
import myself.programing.coding.dto.ChallengeDto;
import myself.programing.coding.dto.CompileRequestJavaDto;
import myself.programing.coding.dto.CompileResponse;
import myself.programing.coding.dto.HttpResponseApi;
import myself.programing.coding.dto.RunWithTestCasesDto;
import myself.programing.coding.enums.API_RESPONSE_STATUS;
import myself.programing.coding.enums.CHALLENGE_ERROR_TYPE;
import myself.programing.coding.exception.ChallengeInfoException;
import myself.programing.coding.repository.TestCaseRepository;
import myself.programing.coding.services.ChallengeService;
import myself.programing.coding.services.javaCoding.JavaCompileService;
import myself.programing.coding.services.javaCoding.threads.ThreadForJavaCompileCode;
import myself.programing.coding.services.javaCoding.threads.ThreadRunWithTestCases;
import myself.programing.coding.services.javaCoding.threads.ThreadsForJavaRunCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/java/compile")
public class JavaCompileController {
    @Autowired
    TestCaseRepository testCaseRepository;

    @Autowired
    ChallengeService challengeService;

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

    @Autowired private JavaCompileService javaCompileService;

    /**
     *
     * @param request
     * @return {@code HttpResponseApi<CompileResponse>}
     */
    @PostMapping("/")
    public HttpResponseApi<CompileResponse> compile(@RequestBody CompileRequestJavaDto request) {
        try {
            logInfo("*****START COMPILING*****");
            ThreadForJavaCompileCode threadForJavaCompileCode = new ThreadForJavaCompileCode();
            String resultCompile = threadForJavaCompileCode.compile(request.getCode(), request.getIdUser());
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
                    .data(new CompileResponse(this.getRightPartAfterFirstBracket(e.getMessage())))
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
    public HttpResponseApi<CompileResponse> run(@RequestBody CompileRequestJavaDto request) {
        try {
            logInfo("*****START COMPILING AND RUN*****");
            ThreadsForJavaRunCode threadsForJavaRunCode = new ThreadsForJavaRunCode();
            System.out.println(testCaseRepository.findByChallengeId(request.getChallengeId()).size());
            String resultRun = threadsForJavaRunCode.runCode(request.getCode(), request.getIdUser(), testCaseRepository.findByChallengeId(request.getChallengeId()));
            CompileResponse compileResponse = new CompileResponse(resultRun);
            HttpResponseApi<CompileResponse> result = HttpResponseApi.<CompileResponse>builder()
                    .message(API_RESPONSE_STATUS.SUCCESS.getMessage())
                    .code(API_RESPONSE_STATUS.SUCCESS.getCode())
                    .data(compileResponse)
                    .build();
            logInfo("*****RUN COMPETITION*****");
            System.out.println(result.getData().getResult());
            return result;
        } catch (InterruptedException | ExecutionException e) {
            logInfo("*****RUN FAIL: " + e.getMessage()+ "*****");
            return HttpResponseApi.<CompileResponse>builder()
                    .code(API_RESPONSE_STATUS.ERROR_COMPILE.getCode())
                    .message(API_RESPONSE_STATUS.ERROR_COMPILE.getMessage())
                    .data(new CompileResponse(this.getRightPartAfterFirstBracket(e.getMessage())))
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

    /**
     *
     * @param request
     * @return {@code HttpResponseApi<List<RunWithTestCasesDto>>}
     */
    @PostMapping("/runWithTestcases")
    public HttpResponseApi<List<RunWithTestCasesDto>> runWithTests(@RequestBody CompileRequestJavaDto request) {
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

    /**
     *
     * @param input
     * @return String
     */
    public String getRightPartAfterFirstBracket(String input) {
        int index = input.indexOf("]");
        if (index != -1 && index < input.length() - 1) {
            return input.substring(index + 1).trim();
        } else {
            return input;
        }
    }

    @GetMapping("/challenge/{id}")
    public HttpResponseApi<ChallengeDto> getChallenge(@PathVariable Long id) {
        try {
            ChallengeDto challenge = challengeService.getById(id);
            return HttpResponseApi.<ChallengeDto>builder()
                    .data(challenge)
                    .message("OK")
                    .code(CHALLENGE_ERROR_TYPE.NORMAL_RESULT.getCode())
                    .build();
        } catch (ChallengeInfoException e) {
            return HttpResponseApi.<ChallengeDto>builder()
                    .data(null)
                    .message(e.getInfoMessage())
                    .code(e.getStatus())
                    .build();
        }
    }
}
