package myself.programing.coding.model;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@Setter
public class GithubUser implements OAuth2User {

    private final OAuth2User oAuth2User;

    public GithubUser(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }
    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2User.getName();
    }

    @Override
    public <A> A getAttribute(String name) {
        return oAuth2User.getAttribute(name);
    }

    public Long getId() {
        return ((Number) Objects.requireNonNull(getAttribute("id"))).longValue();
    }

    public String getLogin() {
        return getAttribute("login");
    }

    public String getAvatarUrl() {
        return getAttribute("avatar_url");
    }
}
