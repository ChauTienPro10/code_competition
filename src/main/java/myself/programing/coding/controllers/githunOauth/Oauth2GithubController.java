package myself.programing.coding.controllers.githunOauth;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth/github")
public class Oauth2GithubController {

    @GetMapping("/authorize")
    public void login(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/github");
    }
}
