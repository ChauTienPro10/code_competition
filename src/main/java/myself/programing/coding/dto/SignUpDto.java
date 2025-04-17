package myself.programing.coding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class SignUpDto {
    private String name;
    private String username;
    private String password;
}
