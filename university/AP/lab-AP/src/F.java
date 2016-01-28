import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;

public class F {
    FastScanner in;
    PrintWriter out;
    double eps = 10e-6;

    public static void main(String[] args) {
        new F().run();
    }

    void run() {
        try {
            in = new FastScanner(new File("continuous" + ".in"));
            out = new PrintWriter(new File("continuous" + ".out"));
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

        int[] len = new int[tests];

        HashSet<Integer> usedStates = new HashSet<>();

        double[][] alpha = new double[states * 2][states * 2];
        double[] beta = new double[states * 2];

        for (int i = 0; i < tests; i++) {

            len[i] = in.nextInt();

            double[] ans = new double[len[i]];
            int[] outIndices = new int[len[i]];
            char[] input = in.next().toCharArray();

            int curState = 0;
            for (int j = 0; j < len[i]; j++) {
                int event = input[j] == '0' ? 0 : 1;
                ans[j] = in.nextDouble();

                outIndices[j] = curState * 2 + event;
                for (int k = 0; k <= j; k++) {
                    beta[outIndices[k]] += ans[j] / len[i];
                }
                usedStates.add(curState * 2 + event);

                curState = transfers[curState][event];
            }

            for (int j = 0; j < len[i]; j++) {
                for (int k = 0; k < j; k++) {
                    alpha[outIndices[j]][outIndices[k]] += (len[i] - j) / (double) len[i];
                }
                for (int k = j; k < len[i]; k++) {
                    alpha[outIndices[j]][outIndices[k]] += (len[i] - k) / (double) len[i];
                }
            }
        }

        for (int i = 0; i < states * 2; i++) {
            if (!usedStates.contains(i)) {
                alpha[i][i] = 1;
            }
        }

        for (int i = 0; i < states * 2; i++) {
            for (int j = 0; j < states * 2; j++) {
                System.out.print(alpha[i][j] + " ");
            }
            System.out.println();
        }

        for (int i = 0; i < states * 2; i++) {
            System.out.print(beta[i] + " ");
        }
        double[] result = lsolve(alpha, beta);
        for (int i = 0; i < states; i++) {
            out.println(result[2 * i] + " " + result[2 * i + 1]);
        }
    }

    public double[] lsolve(double[][] A, double[] b) {
        int N = b.length;

        for (int p = 0; p < N; p++) {
            int max = p;
            for (int i = p + 1; i < N; i++) {
                if (Math.abs(A[i][p]) > Math.abs(A[max][p])) {
                    max = i;
                }
            }
            double[] temp = A[p];
            A[p] = A[max];
            A[max] = temp;
            double t = b[p];
            b[p] = b[max];
            b[max] = t;


            for (int i = p + 1; i < N; i++) {
                double alpha = A[i][p] / A[p][p];
                b[i] -= alpha * b[p];
                for (int j = p; j < N; j++) {
                    A[i][j] -= alpha * A[p][j];
                }
            }
        }

        double[] x = new double[N];
        for (int i = N - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < N; j++) {
                sum += A[i][j] * x[j];
            }
            x[i] = (b[i] - sum) / A[i][i];
        }
        return x;
    }

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

        double nextDouble() {
            return Double.parseDouble(next());
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
}