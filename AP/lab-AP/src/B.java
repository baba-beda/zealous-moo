import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by  baba_beda on 11/11/15.
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void solve() {
        int n = in.nextInt();
        int m = in.nextInt();
        ArrayList<String> parents = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            parents.add(in.next());
        }
        int[] suffixes = new int[n];
        int[] prefixes = new int[n];
        String res = in.next();
        for (int i = 0; i < n; i++) {
            int j = 0;
            while (j < m && parents.get(i).charAt(j) == res.charAt(j)) {
                j++;
            }
            prefixes[i] = j;
            j = 0;
            while (j >= 0 && parents.get(i).charAt(m - j - 1) == res.charAt(m - j - 1)) {
                j++;
            }
            suffixes[i] = j;
        }

        boolean foundOnePoint = false;
        boolean foundTwoPoints = false;
        boolean foundLast = false;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (prefixes[i] + suffixes[j] >= m) {
                    foundOnePoint = true;
                    break;
                }
            }
            if (foundOnePoint) {
                break;
            }
        }
        out.println(foundOnePoint ? "YES" : "NO");

        for (int i = 0; i < n; i++) {
            String resMiddle = res.substring(prefixes[i], m - suffixes[i]);
            for (int j = 0; j < n; j++) {
                String parentsMiddle = parents.get(j).substring(prefixes[i], m - suffixes[i]);
                if (parentsMiddle.equals(resMiddle)) {
                    foundTwoPoints = true;
                    break;
                }
            }
            if (foundTwoPoints) {
                break;
            }
        }
        out.println(foundTwoPoints ? "YES" : "NO");

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                boolean[] equiv = new boolean[m];
                for (int k = 0; k < m; k++) {
                    equiv[k] = (parents.get(i).charAt(k) == parents.get(j).charAt(k) && i != j);
                }
                int k;
                for (k = 0; k < m; k++) {
                    if (equiv[k] && parents.get(i).charAt(k) != res.charAt(k)) {
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