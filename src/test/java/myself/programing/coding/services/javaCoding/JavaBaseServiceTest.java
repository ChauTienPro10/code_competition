package myself.programing.coding.services.javaCoding;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class JavaBaseServiceTest {
    private final JavaBaseService javaBaseService = new JavaBaseService();

//    @Test
//    void generateJavaFile() throws IOException {
//        int id = 110;
//        String codeString = "public static void main(){}";
//        Path path = javaBaseService.generateJavaFile(codeString, id);
//        assertTrue(path.getFileName().toString().contains(id + "_" + LocalDate.now()));
//    }
//
//    @Test
//    void deleteFile() throws IOException {
//        int id = 110;
//        String codeString = "public static void main(){}";
//        Path path = javaBaseService.generateJavaFile(codeString, id);
//        assertTrue(Files.exists(path));
//        javaBaseService.deleteJavaFile(path.toAbsolutePath().toString());
//        assertFalse(Files.exists(path));
//    }
}