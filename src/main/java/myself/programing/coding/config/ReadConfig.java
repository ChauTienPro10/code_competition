package myself.programing.coding.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReadConfig {
    static ConfigLoader configLoader = new ConfigLoader();

    @Value("${client.react}")
    private String clientValue;
    public static String CLIENT;
    @PostConstruct
    public void init() {
        CLIENT = clientValue;
    }


    public static String AUTHORIZATION = configLoader.get("AUTHORIZATION");
    public static String AUTH_PREFIX = configLoader.get("BEARER");
    public static String TEST_URL = configLoader.get("TEST_URL");
    public static String PASSWORD_DEFAULF_FOR_OAUTH = configLoader.get("PASSWORD_DEFAULF_FOR_OAUTH");
}
