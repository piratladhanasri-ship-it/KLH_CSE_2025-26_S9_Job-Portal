import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class SmartJobSearch {

    // Stop words
    private static final Set<String> STOP_WORDS = Set.of(
            "a", "an", "the", "and", "or",
            "for", "to", "of", "in", "on",
            "with", "is", "are", "be",
            "as", "by", "from", "at"
    );

    // Tokenize text
    private static List<String> tokenize(String text) {

        return Arrays.stream(
                text.toLowerCase()
                    .replaceAll("[^a-z0-9+#.]+", " ")
                    .split("\\s+")
        )
        .filter(s -> !s.isBlank())
        .filter(s -> !STOP_WORDS.contains(s))
        .collect(Collectors.toList());
    }

    // Calculate Term Frequency
    private static Map<String, Integer>
    termFrequency(String text) {

        Map<String, Integer> tf = new HashMap<>();

        for (String word : tokenize(text)) {

            tf.put(
                word,
                tf.getOrDefault(word, 0) + 1
            );
        }

        return tf;
    }

    // Cosine Similarity
    private static double cosineSimilarity(
            String query,
            String document) {

        Map<String, Integer> q =
                termFrequency(query);

        Map<String, Integer> d =
                termFrequency(document);

        Set<String> vocabulary =
                new HashSet<>(q.keySet());

        vocabulary.addAll(d.keySet());

        double dot = 0.0;
        double qNorm = 0.0;
        double dNorm = 0.0;

        for (String word : vocabulary) {

            int qValue =
                    q.getOrDefault(word, 0);

            int dValue =
                    d.getOrDefault(word, 0);

            dot += qValue * dValue;

            qNorm += qValue * qValue;

            dNorm += dValue * dValue;
        }

        if (qNorm == 0 || dNorm == 0) {
            return 0.0;
        }

        return dot /
                (Math.sqrt(qNorm) *
                 Math.sqrt(dNorm));
    }

    // Find query keywords using KMP
    private static String findKMPMatches(
            String query,
            String description) {

        List<String> matched =
                new ArrayList<>();

        for (String word : tokenize(query)) {

            if (KMPMatcher.contains(
                    description,
                    word)) {

                matched.add(word);
            }
        }

        return String.join(", ", matched);
    }

    // Main method
    public static void main(String[] args) {

        String corpusPath =
                args.length > 0
                ? args[0]
                : "data/jobs";

        Scanner scanner =
                new Scanner(System.in);

        try {

            // Load job corpus
            List<Job> jobs =
                    JobCorpus.loadJobs(corpusPath);

            System.out.println(
                "=============================================="
            );

            System.out.println(
                "           SMART JOB SEARCH - DSA-3"
            );

            System.out.println(
                "=============================================="
            );

            System.out.println(
                "Corpus loaded: "
                + jobs.size()
                + " job files"
            );

            System.out.print(
                "Enter skills/job keywords: "
            );

            String query =
                    scanner.nextLine().trim();

            if (query.isBlank()) {

                System.out.println(
                    "Please enter at least one keyword."
                );

                return;
            }

            List<Result> results =
                    new ArrayList<>();

            // Search every job
            for (Job job : jobs) {

                double score =
                        cosineSimilarity(
                            query,
                            job.getDescription()
                        );

                String matches =
                        findKMPMatches(
                            query,
                            job.getDescription()
                        );

                if (score > 0 ||
                    !matches.isBlank()) {

                    results.add(
                        new Result(
                            job,
                            score,
                            matches
                        )
                    );
                }
            }

            // Sort according to similarity
            results.sort(
                (a, b) ->
                    Double.compare(
                        b.score,
                        a.score
                    )
            );

            System.out.println(
                "\nSEARCH RESULTS"
            );

            System.out.println(
                "----------------------------------------------"
            );

            if (results.isEmpty()) {

                System.out.println(
                    "No matching jobs found."
                );

            } else {

                int rank = 1;

                for (Result result : results) {

                    System.out.printf(
                        "%d. %s (%s)%n",
                        rank++,
                        result.job.getTitle(),
                        result.job.getId()
                    );

                    System.out.printf(
                        "   Cosine Similarity: %.4f%n",
                        result.score
                    );

                    System.out.println(
                        "   KMP Matches: "
                        + (result.matches.isBlank()
                           ? "None"
                           : result.matches)
                    );

                    System.out.println();
                }
            }

            System.out.println(
                "Algorithms used:"
            );

            System.out.println(
                "- KMP: exact keyword matching"
            );

            System.out.println(
                "- Term Frequency + Cosine Similarity: job ranking"
            );

            System.out.println(
                "- Corpus: collection of separate job text files"
            );

        } catch (IOException e) {

            System.out.println(
                "Error loading corpus: "
                + e.getMessage()
            );

        } finally {

            scanner.close();
        }
    }

    // Result class
    private static class Result {

        Job job;
        double score;
        String matches;

        Result(
                Job job,
                double score,
                String matches) {

            this.job = job;
            this.score = score;
            this.matches = matches;
        }
    }
}
