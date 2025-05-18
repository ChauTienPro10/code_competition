package myself.programing.coding.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "token_invalid")
@Data
public class TokenInvalid {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invalid_token_seq_gen")
    @SequenceGenerator(name = "invalid_token_seq_gen", sequenceName = "invalid_token_seq", allocationSize = 1)
    private Long id;

    private String token;
}
