import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

public class GPrint {
    public static void main(String[] args) {
        new GPrint().run();
    }

    FastScanner in;
    PrintWriter out;

    class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        FastScanner(File f) {
            try {
                br = new BufferedReader(new FileReader(f));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }


    void run() {
        try {
            in = new FastScanner(new File("artificial" + ".in"));
            out = new PrintWriter(new File("artificial" + ".out"));
            solve();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void solve() {
        int test = in.nextInt();
        switch (test) {
            //+
            case 1:
                out.println("2 4 L M\n4 3 R M\n4 1 M M\n2 2 R M\n");
                break;
            //+
            case 2:
                out.println("1 1 R M\n");
                break;
            //+
            case 3:
                out.println("3 3 R M\n3 1 L M\n1 2 M M\n");
                break;
            //+
            case 4:
                out.println("2 1 M M\n3 4 M M\n1 3 R M\n1 4 L M\n");
                break;
            //+
            case 5:
                out.println("2 3 M M\n" +
                        "3 2 M L\n" +
                        "1 1 R M");
                break;
            //+
            case 6:
                out.println("4 2 M M\n" +
                        "3 5 M M\n" +
                        "2 2 L M\n" +
                        "2 1 R M\n" +
                        "3 1 M M");
                break;
            //+
            case 7:
                out.println("2 3 R M\n2 4 L M\n1 4 M M\n2 3 M M\n");
                break;
            //+
            case 8:
                out.println("2 3 M M\n3 3 L M\n4 3 M M\n5 2 R M\n2 1 L M\n");
                break;
            //+
            case 9:
                out.println("3 5 M M\n" +
                        "4 1 L M\n" +
                        "6 5 R M\n" +
                        "1 3 R M\n" +
                        "4 2 M M\n" +
                        "2 2 L M");
                break;
            //+
            case 10:
                out.println("3 2 L M\n" +
                        "4 5 R M\n" +
                        "5 5 M M\n" +
                        "1 5 R M\n" +
                        "6 4 R M\n" +
                        "1 1 L M");
                break;

        }


    }
}