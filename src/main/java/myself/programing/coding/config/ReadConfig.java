package myself.programing.coding.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReadConfig {
    private static ConfigLoader configLoader = new ConfigLoader();

    @Value("${client.react}")
    private String clientValue;

    public static String CLIENT;

    public static String AUTHORIZATION;
    public static String AUTH_PREFIX;
    public static String TEST_URL;
    public static String PASSWORD_DEFAULF_FOR_OAUTH;

    @PostConstruct
    public void init() {
        CLIENT = clientValue;

        // Khởi tạo các biến static sau khi configLoader đã sẵn sàng
        AUTHORIZATION = configLoader.get("AUTHORIZATION");
        AUTH_PREFIX = configLoader.get("BEARER");
        TEST_URL = configLoader.get("TEST_URL");
        PASSWORD_DEFAULF_FOR_OAUTH = configLoader.get("PASSWORD_DEFAULF_FOR_OAUTH");
    }
}
