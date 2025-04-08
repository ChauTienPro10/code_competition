package myself.programing.coding.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum FILE_TYPE {
    TEXT(1, "txt"),
    JAVA(2, "java"),
    PY(3, "py"),
    CPP(4, "cpp"),
    RS(5, "rs")
    ;

    private int code;
    private String name;
}