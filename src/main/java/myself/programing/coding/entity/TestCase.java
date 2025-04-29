package myself.programing.coding.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "test_case")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "test_case_seq_gen")
    @SequenceGenerator(name = "test_case_seq_gen", sequenceName = "test_case_seq", allocationSize = 1)
    private int id;

    private String input;

    private String output;

    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;
}