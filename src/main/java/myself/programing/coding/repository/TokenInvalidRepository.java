package myself.programing.coding.repository;

import myself.programing.coding.entity.TokenInvalid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenInvalidRepository extends JpaRepository<TokenInvalid, Long> {

    Optional<TokenInvalid> findByToken(String token);
}
