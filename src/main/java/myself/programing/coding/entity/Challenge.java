package myself.programing.coding.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "challenge")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "challenge_seq_gen")
    @SequenceGenerator(name = "challenge_seq_gen", sequenceName = "challenge_seq", allocationSize = 1)
    private Long id;

    @Column(name = "content", columnDefinition = "CLOB")
    private String content;

    @Column(name = "template", columnDefinition = "CLOB")
    private String template;

    private String simpleInput;

    private String simpleOutput;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TestCase> testCase;
}