package myself.programing.coding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CompileRequestJavaDto {
    private String code;
    private int idUser;
    private String language;
    private int challengeId;
}
