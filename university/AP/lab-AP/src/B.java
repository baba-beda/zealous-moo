import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by  baba_beda on 11/18/15.
 */
public class B {
    public static void main(String[] args) {
        new B().run();
    }

    Scanner in;
    PrintWriter out;

    void run() {
        try {
            in = new Scanner(new FileInputStream(new File("crossover" + ".in")));
            out = new PrintWriter(new File("crossover" + ".out"));
            solve();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void solve() {
        int n = in.nextInt();
        String[] parents = new String[n];
        int m = in.nextInt();
        for (int i = 0; i < n; i++) {
            parents[i] = in.next();
        }
        String res = in.next();
        int[] prefixes = new int[n];
        int[] suffixes = new int[n];
        for (int i = 0; i < n; i++) {
            int j = 0;
            while (j < m && parents[i].charAt(j) == res.charAt(j)) {
                j++;
            }
            prefixes[i] = j;
            j = 0;
            while (j < m && parents[i].charAt(m - j - 1) == res.charAt(m - j - 1)) {
                j++;
            }
            suffixes[i] = j;
        }
        boolean foundOne = false;
        boolean foundTwo = false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (prefixes[i] + suffixes[j] >= m) {
                    foundOne = true;
                    foundTwo = true;
                    break;
                }
            }
            if (foundOne) {
                break;
            }
        }
        out.println(foundOne ? "YES" : "NO");
        if (!foundTwo) {
            for (int i = 0; i < n; i++) {
                if (prefixes[i] > m - suffixes[i]) {
                    continue;
                }
                String resMid = res.substring(prefixes[i], m - suffixes[i]);
                for (int j = 0; j < n; j++) {
                    String parMid = parents[j].substring(prefixes[i], m - suffixes[i]);
                    if (resMid.equals(parMid)) {
                        foundTwo = true;
                        break;
                    }
                }
                if (foundTwo) {
                    break;
                }
            }
        }
        out.println(foundTwo ? "YES" : "NO");
        boolean foundLast = false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                boolean[] equal = new boolean[m];
                for (int k = 0; k < m; k++) {
                    equal[k] = (parents[i].charAt(k) == parents[j].charAt(k));
                }
                int k;
                for (k = 0; k < m; k++) {
                    if (equal[k] && res.charAt(k) != parents[i].charAt(k)) {
                        break;
                    }
                }
                if (k == m) {
                    foundLast = true;
                    break;
                }
            }
            if (foundLast) {
                break;
            }
        }
        out.println(foundLast ? "YES" : "NO");
    }
}
