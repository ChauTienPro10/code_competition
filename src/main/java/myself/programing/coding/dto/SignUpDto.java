package myself.programing.coding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import myself.programing.coding.annountation.Password;

@AllArgsConstructor
@Setter
@Getter
public class SignUpDto {
    private String name;
    private String username;

    @Password
    private String password;
}
