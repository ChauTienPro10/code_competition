package myself.programing.coding.services;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import myself.programing.coding.dto.UserDto;
import myself.programing.coding.entity.Account;
import myself.programing.coding.entity.User;
import myself.programing.coding.enums.USER_ERROR_TYPE;
import myself.programing.coding.exception.UserInforException;
import myself.programing.coding.mapper.UserMapper;
import myself.programing.coding.repository.AccountRepository;
import myself.programing.coding.repository.UserRepository;
import myself.programing.coding.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;

    @Autowired private AccountRepository accountRepository;

    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired private UserMapper userMapper;

    @Autowired private CustomUserDetailsService userDetailsService;

    @Autowired private JwtUtil jwtUtil;

    /**
     *
     * @param name
     * @param username
     * @param password
     * @return User
     */
    public UserDto signUp(String name, String username, String password) throws UserInforException {
        if (accountRepository.findByUsername(username).isPresent()) {
            throw new UserInforException(USER_ERROR_TYPE.ERROR_CREATE_USER, "This username is used");
        }

        if (invalidPassword(password)) {
            throw new UserInforException(USER_ERROR_TYPE.ERROR_CREATE_ACCOUNT, "Invalid password");
        }

        User user = User.builder()
                .name(formatName(name))
                .account(null)
                .build();
        Account account = Account.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode(password))
                .user(null)
                .build();
        account.setUser(user);
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
     *
     * @param password
     * @return boolean
     */
    public boolean invalidPassword(String password) {
        return password.length() < 6;
    }

    /**
     *
     * @param username
     * @param password
     * @return UserDto
     * @throws UserInforException
     */
    public UserDto login(String username, String password) throws UserInforException {
        Optional<Account> account = accountRepository.findByUsername(username);
        if (account.isEmpty()) {
            throw new UserInforException(USER_ERROR_TYPE.ERROR_INFO_LOGIN, "Username was wrong!");
        }
        if (!bCryptPasswordEncoder.matches(password, account.get().getPassword())) {
            throw new UserInforException(USER_ERROR_TYPE.ERROR_INFO_LOGIN, "Password was wrong!");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        User user = userRepository.findByAccount(account.get());
        if (user == null) {
            throw new UserInforException(USER_ERROR_TYPE.ERROR_INFO_LOGIN, "User not found!");
        }

        UserDto userDto = userMapper.toDto(user);

        String jwt = jwtUtil.generateToken(userDetails.getUsername());
        userDto.setJwt(jwt);

        return userDto;
    }

}
