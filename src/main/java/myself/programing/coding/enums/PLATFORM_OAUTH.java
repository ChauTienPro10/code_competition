package myself.programing.coding.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PLATFORM_OAUTH {
    GITHUB(1, "github"),
    GOOGLE(2, "google"),
    ;

    private Integer id;
    private String name;

    /**
     *
     * @param platformName
     * @return Integer
     */
    public static PLATFORM_OAUTH getPlatformFromString(String platformName) {
        for (PLATFORM_OAUTH platform : PLATFORM_OAUTH.values()) {
            if (platform.getName().equalsIgnoreCase(platformName)) {
                return platform;
            }
        }
        return null;
    }
}
