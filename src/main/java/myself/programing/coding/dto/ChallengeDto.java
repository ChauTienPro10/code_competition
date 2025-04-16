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
}
