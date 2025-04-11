package myself.programing.coding.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    /**
     *
     * @param codeString
     * @param path
     * @return Path
     * @throws IOException
     */
    public static Path generateFile(String codeString, String path) throws IOException {
        Path filePath = Path.of(path);
        Path directoryPath = filePath.getParent();
        if (directoryPath != null && !Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }
        Files.writeString(filePath, codeString);
        return filePath;
    }

    /**
     *
     * @param idUser
     * @return idUser
     */
    public static String generateDockerFolderName(int idUser) {
        return "file_run_" + idUser;
    }

    /**
     *
     * @param path
     * @return String
     */
    public static String getFileNameFromPath(String path) {
        return Paths.get(path).getFileName().toString();
    }
}
