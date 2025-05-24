package myself.programing.coding;

import myself.programing.coding.enums.CHALLENGE_LEVEL;
import myself.programing.coding.enums.CHALLENGE_TYPE;
import myself.programing.coding.services.ChallengeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.Arrays;

@SpringBootApplication
public class CodingApplication {
	public static void main(String[] args) throws IOException {
		SpringApplication.run(CodingApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner run(ChallengeService challengeService) {
//		return args -> {
//			String testCasesJson = "["
//					+ "{\"input\": \"\\\"2,5,4,1,3\\\" 7\", \"output\": \"[2,4]\"},"
//					+ "{\"input\": \"\\\"10,5,199,1,3\\\" 200\", \"output\": \"[2,3]\"},"
//					+ "{\"input\": \"\\\"1,15,99,1,3\\\" 18\", \"output\": \"[1,4]\"}"
//					+ "]";
//
//			String template =
//					"""
//							    public static void main(String[] args) {
//							    String[] numStrs = args[0].split(",");
//							    int[] nums = new int[numStrs.length];
//							    for (int i = 0; i < numStrs.length; i++) {
//							        nums[i] = Integer.parseInt(numStrs[i]);
//							    }
//
//							    int target = Integer.parseInt(args[1]);
//							    int[] output = getIndexOfTwoElmEqualTarget(nums, target);
//							    System.out.println(Arrays.toString(output));
//							}
//
//							public static int[] getIndexOfTwoElmEqualTarget(int[] nums, int target) {
//							    // write your code here.
//							    return new int[0]; // placeholder
//							}
//							""";
//
//			String content = "Cho một mảng số nguyên nums và một số nguyên target, hãy trả về chỉ số của hai số trong mảng sao cho tổng của chúng bằng target.\n" +
//					"\n" +
//					"Bạn có thể giả định rằng mỗi input chỉ có một cặp duy nhất thỏa mãn điều kiện, và không dùng lại cùng một phần tử hai lần.";
//			String simpleInput = "nums = [2,7,11,15], target = 9";
//			String simpleOutput = "[0,1]";
//
//			challengeService.addChallenge(testCasesJson, template, simpleInput, simpleOutput, content, CHALLENGE_TYPE.LEET_CODE.getType(), CHALLENGE_LEVEL.EASY.getLevel());
//		};
//	}
}
