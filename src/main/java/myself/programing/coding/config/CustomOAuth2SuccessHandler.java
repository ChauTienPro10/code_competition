package myself.programing.coding.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import myself.programing.coding.dto.LoginDto;
import myself.programing.coding.dto.UserDto;
import myself.programing.coding.utils.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtService;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public CustomOAuth2SuccessHandler(JwtUtil jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // Lấy thông tin user từ GitHub
        String username = oAuth2User.getAttribute("login");
        String email = oAuth2User.getAttribute("email");

        // Tạo JWT token
        String token = jwtService.generateToken(username,  List.of(new SimpleGrantedAuthority("ROLE_USER")));

        // Redirect về frontend với token
        String redirectUrl = "http://localhost:3000/oauth-success?token=" + token;

        redirectStrategy.sendRedirect(request, response, redirectUrl);
    }
}
