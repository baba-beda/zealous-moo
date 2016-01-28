import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

public class E {
    public static void main(String[] args) {
        new E().run();
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
            in = new FastScanner(new File("discrete" + ".in"));
            out = new PrintWriter(new File("discrete" + ".out"));
            solve();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void solve() {
        int states = in.nextInt();
        int tests = in.nextInt();
        int[][] transfers = new int[states][2];
        for (int i = 0; i < states; i++) {
            transfers[i][0] = in.nextInt() - 1;
            transfers[i][1] = in.nextInt() - 1;
        }

        double[][][] penalty = new double[states][2][26];
        int[][] minIndices = new int[states][2];
        for (int i = 0; i < tests; i++) {
            int len = in.nextInt();
            char[] ins = in.next().toCharArray();
            char[] outs = in.next().toCharArray();

            int curState = 0;
            double comp = 1.0 / len;
            for (int j = 0; j < len; j++) {
                int event = ins[j] == '0' ? 0 : 1;
                penalty[curState][event][outs[j] - 'a'] -= comp;

                if (penalty[curState][event][outs[j] - 'a'] < penalty[curState][event][minIndices[curState][event]]) {
                    minIndices[curState][event] = outs[j] - 'a';
                }

                curState = transfers[curState][event];
            }
        }

        for (int i = 0; i < states; i++) {
            out.println(((char) (minIndices[i][0] + 'a')) + " " + ((char) (minIndices[i][1] + 'a')));
        }
    }
}