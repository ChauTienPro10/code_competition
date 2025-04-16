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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "template", columnDefinition = "LONGTEXT")
    private String template;

    private String simpleInput;

    private String simpleOutput;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TestCase> testCase;
}
