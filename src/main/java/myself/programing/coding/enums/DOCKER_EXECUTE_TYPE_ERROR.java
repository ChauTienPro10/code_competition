package myself.programing.coding.enums;

import lombok.Getter;

@Getter
public enum DOCKER_EXECUTE_TYPE_ERROR {
    CONTAINER_NOT_FOUND(1001, "Container not found"),
    COMMAND_FAILED(1002, "Docker command failed"),
    INVALID_IMAGE(1003, "Invalid Docker image"),
    TIMEOUT(1004, "Docker execution timed out"),
    PERMISSION_DENIED(1005, "Permission denied for Docker operation"),
    UNKNOWN_ERROR(1000, "Unknown error occurred"),
    FILE_NOT_FOUND(1006, "File not founds")
    ;

    private final int errorCode;
    private final String errorMessage;

    DOCKER_EXECUTE_TYPE_ERROR(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static DOCKER_EXECUTE_TYPE_ERROR fromErrorCode(int errorCode) {
        for (DOCKER_EXECUTE_TYPE_ERROR error : DOCKER_EXECUTE_TYPE_ERROR.values()) {
            if (error.getErrorCode() == errorCode) {
                return error;
            }
        }
        return UNKNOWN_ERROR;
    }

}

