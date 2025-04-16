package myself.programing.coding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RunWithTestCasesDto {
    private String input;
    private String expectedResult;
    private boolean status;
}
