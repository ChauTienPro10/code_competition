package myself.programing.coding.controllers.auths;

import myself.programing.coding.dto.HttpResponseApi;
import myself.programing.coding.dto.LoginDto;
import myself.programing.coding.dto.SignUpDto;
import myself.programing.coding.dto.UserDto;
import myself.programing.coding.exception.UserInforException;
import myself.programing.coding.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private UserService userService;

    /**
     *
     * @param request
     * @return {@code HttpResponseApi<User>}
     */
    @PostMapping("/sign_up")
    public HttpResponseApi<UserDto> signUp(@RequestBody SignUpDto request) {
        try {
            UserDto user = userService.signUp(request.getName(), request.getUsername(), request.getPassword());
            return HttpResponseApi.<UserDto>builder()
                    .code(200)
                    .message("OK")
                    .data(user)
                    .build();
        } catch (UserInforException e) {
            return HttpResponseApi.<UserDto>builder()
                    .code(e.getErrorType().getCode())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @PostMapping("/login")
    public HttpResponseApi<UserDto> login(@RequestBody LoginDto request) {
        try {
            return HttpResponseApi.<UserDto>builder()
                    .code(200)
                    .message("OK")
                    .data(userService.login(request.getUsername(), request.getPassword()))
                    .build();
        } catch (UserInforException e) {
            return HttpResponseApi.<UserDto>builder()
                    .code(e.getErrorType().getCode())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }
}
