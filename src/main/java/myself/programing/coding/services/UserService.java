package myself.programing.coding.services;

import myself.programing.coding.dto.UserDto;
import myself.programing.coding.entity.Account;
import myself.programing.coding.entity.User;
import myself.programing.coding.enums.USER_ERROR_TYPE;
import myself.programing.coding.exception.UserInforException;
import myself.programing.coding.mapper.UserMapper;
import myself.programing.coding.repository.AccountRepository;
import myself.programing.coding.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;

    @Autowired private AccountRepository accountRepository;

    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired private UserMapper userMapper;

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
                .name(name)
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
     * @param password
     * @return boolean
     */
    public boolean invalidPassword(String password) {
        return password.length() < 6;
    }

    public UserDto login(String username, String password) throws UserInforException {
        if (accountRepository.findByUsername(username).isEmpty()) {
            throw new UserInforException(USER_ERROR_TYPE.ERROR_INFO_LOGIN, "Username was wrong!");
        }

    }
}
