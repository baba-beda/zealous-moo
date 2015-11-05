package lab_cf_grammar;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Scanner;


/**
 * Created by  baba_beda on 11/5/15.
 */
public class D {
    public static void main(String[] args) {
        new D().run();
    }

    Scanner in;
    PrintWriter out;

    void run() {
        try {
            in = new Scanner(new FileInputStream(new File("nfc" + ".in")));
            out = new PrintWriter(new File("nfc" + ".out"));
            solve();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void solve() {

    }
}