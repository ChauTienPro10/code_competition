package myself.programing.coding.services.javaCoding;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

@Service
public class RunCodeService extends JavaBaseService{
    /**
     *
     * @param classFilePath
     * @return String
     * @throws IOException
     * @throws InterruptedException
     */
    public String runJavaClass(Path classFilePath) throws IOException, InterruptedException {
        String className = classFilePath.getFileName().toString().replace(".class", "");

        Path classDir = classFilePath.getParent() != null ? classFilePath.getParent() : Path.of(".");

        ProcessBuilder pb = new ProcessBuilder("java", className);
        pb.directory(classDir.toFile());
        pb.redirectErrorStream(true);

        Process process = pb.start();
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        int exitCode = process.waitFor();
        if (exitCode == 0) {
            return output.toString();
        } else {
            return "Error occurred while running the class.";
        }
    }
}
