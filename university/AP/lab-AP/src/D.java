import java.io.*;
import java.util.*;

public class D {
    public static void main(String[] args) {
        new D().run();
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
            in = new FastScanner(new File("start" + ".in"));
            out = new PrintWriter(new File("start" + ".out"));
            solve();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void solve() {
        int m = in.nextInt();
        int n = in.nextInt();
        int[][] transfers = new int[n][2];
        char[] outs = new char[n];
        for (int i = 0; i < n; i++) {
            transfers[i][0] = in.nextInt() - 1;
            transfers[i][1] = in.nextInt() - 1;
            outs[i] = in.next().charAt(0);
        }
        NonDeterministicAutomata nda = new NonDeterministicAutomata(n);
        for (int i = 0; i < n; i++) {
            nda.addTransfer(transfers[i][0], i, outs[transfers[i][0]]);
            nda.addTransfer(transfers[i][1], i, outs[transfers[i][1]]);
        }

        char[] word = in.next().toCharArray();
        ArrayList<Integer> ans = nda.accepts(word);
        out.print(ans.size() + " ");
        for (int st : ans) {
            out.print((st + 1) + " ");
        }
    }

    class NonDeterministicAutomata {
        HashMap<Character, ArrayList<Integer>>[] transfers;
        int states;
        NonDeterministicAutomata(int states) {
            this.states = states;
            transfers = new HashMap[states];
            for (int i = 0; i < states; i++) {
                transfers[i] = new HashMap<>();
            }
        }

        void addTransfer(int from, int to, char c) {
            if (!transfers[from].containsKey(c)) {
                transfers[from].put(c, new ArrayList<>());
            }
            transfers[from].get(c).add(to);
        }

        ArrayList<Integer> accepts(char[] word) {
            HashSet<Integer> current = new HashSet<>();
            for (int i = 0; i < states; i++) {
                current.add(i);
            }
            HashSet<Integer> aux = new HashSet<>();
            for (int i = word.length - 1; i >= 0; i--) {
                aux.clear();
                for (int st : current) {
                    if (transfers[st].containsKey(word[i])) {
                        aux.addAll(transfers[st].get(word[i]));
                    }
                }
                current = new HashSet<>(aux);
            }
            ArrayList<Integer> ans = new ArrayList<>();
            ans.addAll(current);
            Collections.sort(ans);
            return ans;
        }
    }
}