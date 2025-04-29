package myself.programing.coding.repository;

import java.util.Optional;
import myself.programing.coding.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     *
     * @param username
     * @return Account
     */
    Optional<Account> findByUsername(String username);

}
