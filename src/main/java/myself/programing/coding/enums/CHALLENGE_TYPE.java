package myself.programing.coding.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum CHALLENGE_TYPE {
    ALGORITHM(1, "algorithm"),
    PROBLEM_RESOLVE(2, "Problem resolve"),
    LEET_CODE(3, "Leet code");
    ;

    private Integer type;
    private String nameType;

    /**
     *
     * @param type
     * @return Boolean
     */
    public static Boolean invalidType(int type) {
        for (CHALLENGE_TYPE challengeType : CHALLENGE_TYPE.values()) {
            if (challengeType.getType() == type) {
                return false;
            }
        }
        return true;
    }
}
