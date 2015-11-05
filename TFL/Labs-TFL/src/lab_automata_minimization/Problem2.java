package lab_automata_minimization;


import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by  baba_beda on 11/3/15.
 */
public class Problem2 {
    public static void main(String[] args) {
        new Problem2().run();
    }

    Scanner in;
    PrintWriter out;

    void run() {
        try {
            in = new Scanner(new FileInputStream(new File("equivalence" + ".in")));
            out = new PrintWriter(new File("equivalence" + ".out"));
            solve();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void solve() {
        Automata a1 = readAuto();
        Automata a2 = readAuto();
        visited1 = new boolean[a1.states];
        visited2 = new boolean[a2.states];
        if (bfsEquivCheck(a1, a2)) {
            out.println("YES");
        }
        else {
            out.println("NO");
        }
    }

    Automata readAuto() {
        int n = in.nextInt();
        int m = in.nextInt();
        int k = in.nextInt();
        Automata a = new Automata(n);
        for (int i = 0; i < k; i++) {
            a.addTerminal(in.nextInt() - 1);
        }
        for (int i = 0; i < m; i++) {
            a.addTransfer(in.nextInt() - 1, in.nextInt() - 1, in.next().charAt(0));
        }
        return a;
    }

    boolean visited1[];
    boolean visited2[];

    boolean bfsEquivCheck(Automata a1, Automata a2) {
        ArrayDeque<Pair> queue = new ArrayDeque<>();
        queue.add(new Pair(0, 0));
        visited1[0] = true;
        visited2[0] = true;
        while (!queue.isEmpty()) {
            Pair p = queue.poll();
            int u = p.fst;
            int v = p.snd;
            if (a1.isTerminal(u) != a2.isTerminal(v)) {
                return false;
            }
            for (int c = 0; c < 26; c++) {
                char ch = (char) ('a' + c);
                if (a1.transfers[u].containsKey(ch) && !visited1[a1.transfers[u].get(ch)]
                        || a2.transfers[v].containsKey(ch) && !visited2[a2.transfers[v].get(ch)]) {
                    queue.add(new Pair(a1.transfers[u].get(ch), a2.transfers[v].get(ch)));
                    visited1[a1.transfers[u].get(ch)] = true;
                    visited2[a2.transfers[v].get(ch)] = true;
                }
            }
        }
        return true;
    }
    class Automata {
        HashSet<Integer> terminals;
        HashMap<Character, Integer>[] transfers;
        int states;
        Automata(int states) {
            terminals = new HashSet<>();
            this.states = states;
            transfers = new HashMap[states];
            for (int i = 0; i < states; i++) {
                transfers[i] = new HashMap<>();
            }
        }
        void addTransfer(int from, int to, char c) {
            transfers[from].put(c, to);
        }
        void addTerminal(int t) {
            terminals.add(t);
        }
        boolean isTerminal(int t) {
            return terminals.contains(t);
        }
        boolean isDevilish(int t) {
            if (t == -1) {
                return true;
            }
            for (Map.Entry<Character, Integer> entry : transfers[t].entrySet()) {
                if (entry.getValue() != t) {
                    return false;
                }
            }
            return true;
        }
        int canGo(int from, char c) {
            if (transfers[from].containsKey(c)) {
                return transfers[from].get(c);
            }
            return -1;
        }

        boolean accepts(char[] word) {
            int curState = 0;
            for (char c : word) {
                int can = canGo(curState, c);
                if (can != -1) {
                    curState = can;
                }
                else {
                    return false;
                }
            }
            return isTerminal(curState);
        }
    }
    class Pair {
        int fst, snd;

        public Pair(int fst, int snd) {
            this.fst = fst;
            this.snd = snd;
        }
    }
}