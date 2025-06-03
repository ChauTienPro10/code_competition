package myself.programing.coding.services;

import java.util.Arrays;
import java.util.stream.Collectors;

import myself.programing.coding.config.PasswordEncoderConfig;
import myself.programing.coding.config.ReadConfig;
import myself.programing.coding.dto.UserDto;
import myself.programing.coding.entity.Account;
import myself.programing.coding.entity.TokenInvalid;
import myself.programing.coding.entity.User;
import myself.programing.coding.enums.PLATFORM_OAUTH;
import myself.programing.coding.enums.USER_ERROR_TYPE;
import myself.programing.coding.exception.UserInforException;
import myself.programing.coding.mapper.UserMapper;
import myself.programing.coding.model.CustomUserDetails;
import myself.programing.coding.repository.AccountRepository;
import myself.programing.coding.repository.TokenInvalidRepository;
import myself.programing.coding.repository.UserRepository;
import myself.programing.coding.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;

    @Autowired private AccountRepository accountRepository;

    @Autowired private PasswordEncoderConfig passwordEncoderConfig;

    @Autowired private UserMapper userMapper;

    @Autowired TokenInvalidRepository tokenInvalidRepository;

    @Autowired CustomUserDetailsService userDetailsService;

    /**
     *
     * @param name
     * @param username
     * @param password
     * @return User
     */
    public UserDto signUp(String name, String username, String password , String accessToken, String platform) throws UserInforException {
        if (accountRepository.findByUsername(username).isPresent()) {
            throw new UserInforException(USER_ERROR_TYPE.ERROR_CREATE_USER, "This username is used");
        }

        User user = User.builder()
                .name(formatName(name))
                .account(null)
                .build();
        Account account = Account.builder()
                .username(username)
                .password(passwordEncoderConfig.passwordEncoder().encode(password != null ? password :
                        ReadConfig.PASSWORD_DEFAULF_FOR_OAUTH))
                .accessToken(accessToken)
                .platform(PLATFORM_OAUTH.getPlatformFromString(platform) != null ?
                        PLATFORM_OAUTH.getPlatformFromString(platform).getId() : null)
                .build();
        user.setAccount(account);
        return userMapper.toDto(userRepository.save(user));
    }

    /**
     *
     * @param name
     * @return String
     */
    public String formatName(String name) {
        return Arrays.stream(name.trim().toLowerCase().split("\\s+"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    /**
     * userDetail is account
     * @param username
     * @param password
     * @return UserDto
     * @throws UserInforException
     */
    public UserDto login(String username, String password) throws UserInforException {
        try {
            JwtUtil jwtUtil = new JwtUtil();
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
            if (userDetails.getAccount().getPlatform() != null) {
                throw new UsernameNotFoundException("invalid method for platform!");
            }
            if (!passwordEncoderConfig.passwordEncoder().matches(password, userDetails.getPassword())) {
                throw new UserInforException(USER_ERROR_TYPE.ERROR_INFO_LOGIN, "Password was wrong!");
            }
            User user = userRepository.findByAccount(userDetails.getAccount());
            if (user == null) {
                throw new UserInforException(USER_ERROR_TYPE.ERROR_INFO_LOGIN, "User not found!");
            }
            UserDto userDto = userMapper.toDto(user);
            String jwt = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getAuthorities());
            userDto.setJwt(jwt);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return userDto;
        } catch (UsernameNotFoundException e) {
            throw new UserInforException(USER_ERROR_TYPE.ERROR_INFO_LOGIN, "Username was wrong!");
        }
    }

    /**
     *
     * @param bearerToken
     */
    public void logout(String bearerToken) {
        try {
            String token = JwtUtil.getFromStringBearer(bearerToken);
            if (tokenInvalidRepository.findByToken(token).isPresent()) {
                return;
            }
            TokenInvalid tokenInvalid = new TokenInvalid();
            tokenInvalid.setToken(token);
            tokenInvalidRepository.save(tokenInvalid);
            SecurityContextHolder.clearContext();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Logout failed", e);
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByAccount(accountRepository.findByUsername(username).get());
    }
}
