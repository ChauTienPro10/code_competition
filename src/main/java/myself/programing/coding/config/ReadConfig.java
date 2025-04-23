package myself.programing.coding.config;

import org.springframework.stereotype.Component;

@Component
public class ReadConfig {
    static ConfigLoader configLoader = new ConfigLoader();

    public static String REACT_CLIENT = configLoader.get("REACT_CLIENT");
}
