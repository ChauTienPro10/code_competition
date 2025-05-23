package myself.programing.coding.controllers.auths;

import myself.programing.coding.dto.HttpResponseApi;
import myself.programing.coding.dto.LoginDto;
import myself.programing.coding.dto.SignUpDto;
import myself.programing.coding.dto.UserDto;
import myself.programing.coding.enums.API_RESPONSE_STATUS;
import myself.programing.coding.exception.UserInforException;
import myself.programing.coding.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
                    .code(API_RESPONSE_STATUS.SUCCESS.getCode())
                    .message(API_RESPONSE_STATUS.SUCCESS.getMessage())
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

    /**
     *
     * @param request
     * @return {@code HttpResponseApi<UserDto> }
     */
    @PostMapping("/login")
    public HttpResponseApi<UserDto> login(@RequestBody LoginDto request) {
        try {
            return HttpResponseApi.<UserDto>builder()
                    .code(API_RESPONSE_STATUS.SUCCESS.getCode())
                    .message(API_RESPONSE_STATUS.SUCCESS.getMessage())
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

    /**
     *
     * @param bearerToken
     * @return {@code HttpResponseApi<String>}
     */
    @PostMapping("/logout")
    public HttpResponseApi<String> logout(@RequestHeader("Authorization") String bearerToken) {
        try {
            userService.logout(bearerToken);
            return HttpResponseApi.<String>builder()
                    .code(HttpResponseApi.API_RESPONSE_CODE.OK.getCode())
                    .message(HttpResponseApi.API_RESPONSE_CODE.OK.getMessage())
                    .build();
        } catch (Exception e) {
            // them ghi log
            return HttpResponseApi.<String>builder()
                    .code(HttpResponseApi.API_RESPONSE_CODE.BAD_REQUEST.getCode())
                    .message(HttpResponseApi.API_RESPONSE_CODE.BAD_REQUEST.getMessage())
                    .build();
        }
    }
}
