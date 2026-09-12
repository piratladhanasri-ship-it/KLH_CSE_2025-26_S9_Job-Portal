import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class JobCorpus {

    public static Map<String, List<String>> loadCorpus(String folder)
            throws IOException {

        Map<String, List<String>> corpus = new LinkedHashMap<>();

        Path folderPath = Paths.get(folder);

        try (DirectoryStream<Path> files =
                     Files.newDirectoryStream(folderPath, "*.txt")) {

            for (Path file : files) {

                List<String> lines = Files.readAllLines(file);

                corpus.put(
                    file.getFileName().toString(),
                    lines
                );
            }
        }

        return corpus;
    }
}
