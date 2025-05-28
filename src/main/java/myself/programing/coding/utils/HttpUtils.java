package myself.programing.coding.utils;

import jakarta.servlet.http.HttpServletResponse;
import myself.programing.coding.config.ReadConfig;

public class HttpUtils {
    public static HttpServletResponse addHeaderResponse(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", ReadConfig.CLIENT);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        return response;
    }
}
