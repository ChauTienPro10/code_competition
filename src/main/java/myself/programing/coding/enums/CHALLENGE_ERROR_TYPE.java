package myself.programing.coding.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum CHALLENGE_ERROR_TYPE {

    NORMAL_RESULT(0, ""),
    CHALLENGE_NOT_FOUND(1, "Challenge doesn't exist or has been deleted!")
    ;
    private int code;
    private String message;
}
