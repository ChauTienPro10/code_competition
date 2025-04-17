package myself.programing.coding.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum USER_ERROR_TYPE {

    ERROR_CREATE_USER(1, "Create user fail!"),
    ERROR_CREATE_ACCOUNT(2, "Create account fail!"),
    ERROR_INFO_LOGIN(3, "Login fail!"),
    ;
    private int code;
    private String message;
}
