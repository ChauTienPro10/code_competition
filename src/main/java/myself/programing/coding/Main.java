package myself.programing.coding;

import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.javaCoding.JavaBaseService;
import myself.programing.coding.services.javaCoding.JavaCompileService;
import myself.programing.coding.services.javaCoding.threads.ThreadForJavaCompileCode;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws IOException, DockerExecuteException, ExecutionException, InterruptedException {
        JavaCompileService javaCompileService = new JavaCompileService();
        String resultCopy = javaCompileService.doCopyFileToContainer("/fileStorage/java/1002/HelloWorld.java", 10002);
        System.out.println(resultCopy);
        String resultCompile = javaCompileService.doCompileToClassFile("/app/file_run_10002/HelloWorld.java");
        System.out.println(resultCompile);
    }
}
