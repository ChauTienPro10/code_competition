package myself.programing.coding.exception;

import lombok.Getter;
import myself.programing.coding.enums.USER_ERROR_TYPE;

@Getter
public class UserInforException extends Throwable {
    private USER_ERROR_TYPE errorType;
    public UserInforException(String message) {
        super(message);
    }

    public UserInforException(USER_ERROR_TYPE userErrorType, Throwable e) {
        super(e.getMessage());
        this.errorType = userErrorType;
    }

    public UserInforException(USER_ERROR_TYPE userErrorType, String message) {
        super(message);
        this.errorType = userErrorType;
    }
}
