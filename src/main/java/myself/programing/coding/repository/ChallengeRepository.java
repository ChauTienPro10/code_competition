package myself.programing.coding.repository;

import myself.programing.coding.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    List<Challenge> findAll();

    List<Challenge> findByType(int type);
}
