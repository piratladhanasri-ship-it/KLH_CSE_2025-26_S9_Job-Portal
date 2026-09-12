import java.util.ArrayList;
import java.util.List;

public class KMPMatcher {

    // Create the LPS (Longest Prefix Suffix) array
    private static int[] computeLPS(String pattern) {

        int[] lps = new int[pattern.length()];
        int length = 0;
        int i = 1;

        while (i < pattern.length()) {

            if (Character.toLowerCase(pattern.charAt(i))
                    == Character.toLowerCase(pattern.charAt(length))) {

                length++;
                lps[i] = length;
                i++;

            } else {

                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }

    // KMP string matching algorithm
    public static List<Integer> search(String text, String pattern) {

        List<Integer> positions = new ArrayList<>();

        if (pattern == null || pattern.isEmpty()) {
            return positions;
        }

        int[] lps = computeLPS(pattern);

        int i = 0;
        int j = 0;

        while (i < text.length()) {

            if (Character.toLowerCase(text.charAt(i))
                    == Character.toLowerCase(pattern.charAt(j))) {

                i++;
                j++;

                if (j == pattern.length()) {

                    positions.add(i - j);

                    j = lps[j - 1];
                }

            } else {

                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        return positions;
    }
}
