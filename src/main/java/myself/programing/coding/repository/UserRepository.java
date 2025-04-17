package myself.programing.coding.repository;

import myself.programing.coding.entity.Account;
import myself.programing.coding.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByAccount(Account accountId);
}
