package myself.programing.coding;

import myself.programing.coding.exception.DockerExecuteException;
import myself.programing.coding.services.javaCoding.JavaBaseService;
import myself.programing.coding.services.javaCoding.JavaCompileService;
import myself.programing.coding.services.javaCoding.threads.ThreadForJavaCompileCode;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws IOException, DockerExecuteException, ExecutionException, InterruptedException {
//        String filePath = "C:/Users/LENOVO/Documents/sanbox_java21/Test.java";
//        String fileName = FileUtils.getFileNameFromPath(filePath);
//        DockerServiceForJava dockerServiceForJava = new DockerServiceForJava();
//        dockerServiceForJava.genTouchFolderCmd(FileUtils.generateDockerFolderName(1002));
//        dockerServiceForJava.executeDockerCommand(dockerServiceForJava.genTouchFolderCmd(FileUtils.generateDockerFolderName(1002)));
//        String filePathDocker = FileUtils.generateDockerFolderName(1002) + "/" + fileName;
//
//        dockerServiceForJava.executeDockerCommand(dockerServiceForJava.generateCopyFileToContainerCmd("C:/Users/LENOVO/Documents/sanbox_java21/Test.java", filePathDocker));
//        dockerServiceForJava.executeDockerCommandHasResult(dockerServiceForJava.genCompileFileJavaCmd(filePathDocker));
//        System.out.println(dockerServiceForJava.executeDockerCommandHasResult(dockerServiceForJava.genRunFileJavaCmd(filePathDocker)));

        JavaBaseService javaBaseService = new JavaBaseService();
//        System.out.println(javaBaseService.generateJavaFile("TestOfMe", "public class TestOfMe { public static void main(String[] args) { System.out.println(\"Hi!\"); } }", 10024));

//        System.out.println(javaBaseService.doCopyFileToContainer("C:/fileStorage/java/10024/TestOfMe.java", 10024));

//        JavaCompileService compileService = new JavaCompileService();
//        System.out.println(compileService.doCompileToClassFile("file_run_10024/TestOfMe.java"));

        ThreadForJavaCompileCode thread = new ThreadForJavaCompileCode();
        try {
            String result = thread.compile("file_run_10024/TestOfMe.java");
            System.out.println(result);
        } catch (Exception e) {
            throw e;
        } finally {
//            thread.shutdown();
        }

        System.out.println("hello");

        ThreadForJavaCompileCode thread1 = new ThreadForJavaCompileCode();
        String result1 = thread1.compile("file_run_10024/TestOfMưe.java");
        System.out.println(result1);


    }
}
