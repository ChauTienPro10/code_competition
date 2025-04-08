package myself.programing.coding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CodingApplication {
	private static final Logger logger = LoggerFactory.getLogger(CodingApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CodingApplication.class, args);
	}
}
