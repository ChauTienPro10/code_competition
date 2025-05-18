package myself.programing.coding.dto;

import lombok.*;

@Builder
@Data
public class HttpResponseApi<T> {
    private int code;
    private String message;
    private T data;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static enum API_RESPONSE_CODE {

        OK(200, "OK"),
        BAD_REQUEST(400, "Bad request!")
        ;

        private Integer code;
        private String message;
    }
}

