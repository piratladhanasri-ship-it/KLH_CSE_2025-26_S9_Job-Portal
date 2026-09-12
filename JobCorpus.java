import java.io.*;
import java.nio.file.*;
import java.util.*;

public class JobCorpus {

    // Load all job files from the corpus folder
    public static List<Job> loadJobs(String folderPath)
            throws IOException {

        List<Job> jobs = new ArrayList<>();

        Path folder = Paths.get(folderPath);

        if (!Files.exists(folder)) {
            throw new FileNotFoundException(
                "Corpus folder not found: "
                + folder.toAbsolutePath()
            );
        }

        // Read all .txt files
        try (DirectoryStream<Path> stream =
                     Files.newDirectoryStream(folder, "*.txt")) {

            for (Path file : stream) {

                List<String> lines =
                        Files.readAllLines(file);

                if (lines.isEmpty()) {
                    continue;
                }

                // First line = Job Title
                String title = lines.get(0).trim();

                // Remaining lines = Description
                String description =
                        String.join(
                                " ",
                                lines.subList(1, lines.size())
                        ).trim();

                // File name becomes Job ID
                String id =
                        file.getFileName()
                            .toString()
                            .replaceFirst(
                                "(?i)\\.txt$",
                                ""
                            );

                jobs.add(
                    new Job(id, title, description)
                );
            }
        }

        // Sort jobs by ID
        jobs.sort(
            Comparator.comparing(Job::getId)
        );

        return jobs;
    }
}
