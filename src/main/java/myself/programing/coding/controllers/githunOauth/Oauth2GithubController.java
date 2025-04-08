package myself.programing.coding.controllers.githunOauth;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

    @GetMapping("/callback")
    public Map<String, Object> callback(@AuthenticationPrincipal OAuth2User principal) {
        System.out.println(principal.getAttributes().get("login"));
        return principal.getAttributes();
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
