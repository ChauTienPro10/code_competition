package myself.programing.coding.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class HttpResponseApi<T> {
    private int code;
    private String message;
    private T data;
}

