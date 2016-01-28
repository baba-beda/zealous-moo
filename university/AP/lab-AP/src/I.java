import java.util.Scanner;
import java.util.Arrays;

public class I {
    Scanner in;

    public static void main(String[] args) {
        new I().run();
    }

    void run() {
        in = new Scanner(System.in);
        solve();
    }

    void solve() {
        int n = in.nextInt();
        int cur, lastCur;
        char[] curString = new char[n];
        Arrays.fill(curString, '0');
        for (int i = 0; i < n; i++) {
            System.out.println(curString);
            cur = in.nextInt();
            curString[i] = '1';
            System.out.println(curString);
            lastCur = in.nextInt();
            if (lastCur < cur) {
                curString[i] = '0';
            }
            if (lastCur == n || cur == n) {
                break;
            }
        }
    }
}

