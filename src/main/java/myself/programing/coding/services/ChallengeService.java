package myself.programing.coding.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import myself.programing.coding.entity.Challenge;
import myself.programing.coding.entity.TestCase;
import myself.programing.coding.repository.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChallengeService {

    @Autowired private ChallengeRepository challengeRepository;

    /**
     *
     * @param testCasesJson
     * @param template
     * @param simpleInput
     * @param simpleOutput
     * @param content
     * @throws JsonProcessingException
     */
    public void addChallenge(String testCasesJson, String template, String simpleInput, String simpleOutput, String content) throws JsonProcessingException {
        List<TestCase> testcases = getTestCaseFormJsonArray(testCasesJson);
        Challenge challenge = Challenge.builder()
                .content(content)
                .template(template)
                .testCase(new ArrayList<>())
                .simpleInput(simpleInput)
                .simpleOutput(simpleOutput)
                .build();
        for (TestCase testCase : testcases) {
            testCase.setChallenge(challenge);
        }

        challenge.setTestCase(testcases);

        challengeRepository.save(challenge);
    }

    /**
     *
     * @param testCasesJson
     * @return {@code List<TestCase>}
     * @throws JsonProcessingException
     */
    public List<TestCase> getTestCaseFormJsonArray(String testCasesJson) throws JsonProcessingException {
        List<TestCase> testcases = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode inputsVsOutputs = objectMapper.readTree(testCasesJson);
        for (JsonNode item : inputsVsOutputs) {
            testcases.add(TestCase.builder()
                    .input(item.get("input").asText())
                    .output(item.get("output").asText())
                    .build());
        }
        return testcases;
    }
}
