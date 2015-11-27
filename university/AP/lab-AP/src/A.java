import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by  baba_beda on 11/11/15.
 */
public class A {
    public static void main(String[] args) {
        new A().run();
    }

    Scanner in;
    PrintWriter out;

    void run() {
        try {
            in = new Scanner(new FileInputStream(new File("mutation" + ".in")));
            out = new PrintWriter(new File("mutation" + ".out"));
            solve();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void solve() {
        double n = in.nextInt();
        int m = in.nextInt();
        for (int i = 0; i < m; i++) {
            char[] s = in.next().toCharArray();
            char[] t = in.next().toCharArray();
            double result = 1;
            for (int j = 0; j < n; j++) {
                if (s[j] != t[j]) {
                    result *= 1/n;
                }
                else {
                    result *= (n - 1)/n;
                }
            }
            out.println(result);
        }
    }
}