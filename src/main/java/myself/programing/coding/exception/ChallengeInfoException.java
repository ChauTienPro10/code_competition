package myself.programing.coding.exception;


import lombok.Getter;
import myself.programing.coding.enums.CHALLENGE_ERROR_TYPE;

@Getter
public class ChallengeInfoException extends RuntimeException {
    private int status;
    private String infoMessage;

    public ChallengeInfoException(String message) {
        super(message);
    }

    public ChallengeInfoException(CHALLENGE_ERROR_TYPE type) {
        super(type.getMessage());
        this.status = type.getCode();
        this.infoMessage = getMessage();
    }

    public ChallengeInfoException(CHALLENGE_ERROR_TYPE type, Throwable e) {
        super(type.getMessage(), e);
        this.status = type.getCode();
        this.infoMessage = type.getMessage();
    }
}
