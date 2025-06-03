package myself.programing.coding.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import myself.programing.coding.annountation.Password;

@Getter
@NoArgsConstructor
public class LoginDto {
    private String username;

    @Password
    private String password;
}
