package myself.programing.coding.enums;

import lombok.Getter;

@Getter
public enum API_RESPONSE_STATUS {
    SUCCESS(200, "Success"),
    ERROR_COMPILE(3001, "Error"),
    RUN_FAIL(3002, "Fail"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    BAD_REQUEST(400, "Bad Request"),
    SERVER_ERROR(500, "Server Error");

    private final String message;
    private final int code;

    API_RESPONSE_STATUS(int code, String message) {
        this.message = message;
        this.code = code;
    }
}

