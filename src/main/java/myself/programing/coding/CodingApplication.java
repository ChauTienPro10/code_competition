package myself.programing.coding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class CodingApplication {
	public static void main(String[] args) throws IOException {
		SpringApplication.run(CodingApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner run(ChallengeService challengeService) {
//		return args -> {
//			String testCasesJson = "["
//					+ "{\"input\": \"2 3\", \"output\": \"5\"},"
//					+ "{\"input\": \"10 5\", \"output\": \"15\"},"
//					+ "{\"input\": \"10023 77\", \"output\": \"10100\"}"
//					+ "]";
//
//			String template =
//					"public class Main {\n" +
//							"    public static void main(String[] args) {\n" +
//							"        int a = Integer.parseInt(args[0]);\n" +
//							"        int b = Integer.parseInt(args[1]);\n" +
//							"        System.out.println(sum(a, b));\n" +
//							"    }\n\n" +
//							"    public static int sum(int a, int b) {\n" +
//							"        // Write your code here\n" +
//							"    }\n" +
//							"}";
//
//			String content = "Cho 2 số nguyên a và b viết phương thức để tính tổng 2 số";
//			String simpleInput = "2 5";
//			String simpleOutput = "7";
//
//			challengeService.addChallenge(testCasesJson, template, simpleInput, simpleOutput, content);
//		};
//	}
}
