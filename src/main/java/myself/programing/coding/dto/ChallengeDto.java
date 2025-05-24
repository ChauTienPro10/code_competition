package myself.programing.coding.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ChallengeDto {
    private int id;
    private String content;
    private String template;
    private String simpleInput;
    private String simpleOutput;
    private String level;

    public void setLevel(int level) {
        switch (level) {
            case 1:
                this.level = "Easy";
                break;
            case 2:
                this.level = "Medium";
                break;
            case 3:
                this.level = "Hard";
                break;
        }
    }
}
