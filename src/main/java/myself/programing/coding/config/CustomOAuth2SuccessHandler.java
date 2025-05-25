package myself.programing.coding.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import myself.programing.coding.dto.UserDto;
import myself.programing.coding.enums.PLATFORM_OAUTH;
import myself.programing.coding.exception.UserInforException;
import myself.programing.coding.mapper.UserMapper;
import myself.programing.coding.services.UserService;
import myself.programing.coding.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;

    private final JwtUtil jwtService;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final String SUCCESS_ENDPOINT = "/oauth-success?token=";
    private final String FAIL_AUTH_ENPOINT = "/oauth-fail?error=";

    public CustomOAuth2SuccessHandler(JwtUtil jwtService) {
        this.jwtService = jwtService;
    }

    /**
     *
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        try {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            OAuth2User oAuth2User = oauthToken.getPrincipal();
            String registrationId = oauthToken.getAuthorizedClientRegistrationId();
            if (PLATFORM_OAUTH.GITHUB.getName().equals(registrationId)) {
                String username = getUsernameFromPrincipal(oAuth2User);
                redirectStrategy.sendRedirect(request,
                        response,
                        genRedirectUrlForOAuth(username,
                                username,
                                getAccessTokenFromAuthentication(authentication),
                                PLATFORM_OAUTH.GITHUB));
            } else if (PLATFORM_OAUTH.GOOGLE.getName().equals(registrationId)) {
                String email = oAuth2User.getAttribute("email");
                String name = oAuth2User.getAttribute("name");
                redirectStrategy.sendRedirect(request,
                        response,
                        genRedirectUrlForOAuth(name,
                                email,
                                getAccessTokenFromAuthentication(authentication),
                                PLATFORM_OAUTH.GOOGLE));
            }
        } catch (Exception e) {
            // Them xu ly ghi log o day
            e.printStackTrace();
            String redirectUrl = ReadConfig.REACT_CLIENT + FAIL_AUTH_ENPOINT + e.getMessage();
            redirectStrategy.sendRedirect(request, response, redirectUrl);
        }
    }

    /**
     *
     * @param authentication
     * @return String
     */
    public String getAccessTokenFromAuthentication(Authentication authentication) {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName());
        return client.getAccessToken().getTokenValue();
    }

    /**
     *
     * @param principal
     * @return String
     */
    public String getUsernameFromPrincipal(Object principal) {
        OAuth2User oAuth2User = (OAuth2User) principal;
        return oAuth2User.getAttribute("login");
    }

    /**
     *
     * @param name
     * @param username
     * @param accessToken
     * @return User
     */
    public UserDto createUser (String name, String username, String accessToken, PLATFORM_OAUTH platformOauth) throws UserInforException {
        return userService.signUp(name, username, null, accessToken, platformOauth.getName());
    }

    /**
     *
     * @param name
     * @param username
     * @param accessToken
     * @param platformOauth
     * @return String
     */
    public String genRedirectUrlForOAuth(String name, String username, String accessToken, PLATFORM_OAUTH platformOauth) {
        UserDto user = null;
        try {
            user = createUser(
                    name,
                    username,
                    accessToken,
                    platformOauth);
        } catch (UserInforException e) {

            user = userMapper.toDto(userService.findByUsername(username));
        }
        if (user == null) {
            throw new RuntimeException("Can't save this user's info");
        }
        String token = jwtService.generateToken(username,  List.of(new SimpleGrantedAuthority("ROLE_USER")));
        String infoUser = "&id=" + user.getId() + "&name=" + user.getName();
        return ReadConfig.REACT_CLIENT + SUCCESS_ENDPOINT + token + infoUser ;
    }

}
