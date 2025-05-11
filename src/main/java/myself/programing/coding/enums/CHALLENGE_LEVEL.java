package myself.programing.coding.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CHALLENGE_LEVEL {

    EASY(1),
    MEDIUM(2),
    HARD(3),
    ;

    private Integer level;
}
