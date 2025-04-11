package myself.programing.coding.controllers.javaCode;

import java.util.concurrent.ExecutionException;
import myself.programing.coding.dto.CompileRequestJavaDto;
import myself.programing.coding.dto.CompileResponse;
import myself.programing.coding.dto.HttpResponseApi;
import myself.programing.coding.enums.API_RESPONSE_STATUS;
import myself.programing.coding.services.javaCoding.JavaCompileService;
import myself.programing.coding.services.javaCoding.threads.ThreadForJavaCompileCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/java/compile")
public class JavaCompileController {

    @Autowired private JavaCompileService javaCompileService;
    private final ThreadForJavaCompileCode threadForJavaCompileCode = new ThreadForJavaCompileCode();

    @PostMapping("/")
    public HttpResponseApi<CompileResponse> compile(@RequestBody CompileRequestJavaDto request) {
        try {
            String resultCompile = threadForJavaCompileCode.compile(request.getCode());
            CompileResponse compileResponse = new CompileResponse(resultCompile);
            return HttpResponseApi.<CompileResponse>builder()
                    .message(API_RESPONSE_STATUS.SUCCESS.getMessage())
                    .code(API_RESPONSE_STATUS.SUCCESS.getCode())
                    .data(compileResponse)
                    .build();
        } catch (InterruptedException | ExecutionException e) {
            return HttpResponseApi.<CompileResponse>builder()
                    .code(API_RESPONSE_STATUS.ERROR_COMPILE.getCode())
                    .message(API_RESPONSE_STATUS.ERROR_COMPILE.getMessage())
                    .data(new CompileResponse(this.getRightPartAfterLastColon(e.getMessage())))
                    .build();
        } catch (Exception e) {
            return HttpResponseApi.<CompileResponse>builder()
                    .code(API_RESPONSE_STATUS.SERVER_ERROR.getCode())
                    .message(API_RESPONSE_STATUS.SERVER_ERROR.getMessage())
                    .data(null)
                    .build();
        }
    }

    /**
     * String
     * @param input
     * @return
     */
    public String getRightPartAfterLastColon(String input) {
        int lastColon = input.lastIndexOf(":");
        if (lastColon != -1 && lastColon < input.length() - 1) {
            return input.substring(lastColon + 1);
        } else {
            return input;
        }
    }

}
