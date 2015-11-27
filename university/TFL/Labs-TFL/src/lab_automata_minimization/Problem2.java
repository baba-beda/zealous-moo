package lab_automata_minimization;

import javax.swing.text.html.HTMLDocument;
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
        Automaton a1 = readAuto();
        Automaton a2 = readAuto();
        if (bfsEquivCheck(a1, a2)) {
            System.out.println("YES");
            out.print("YES");
        }
        else {
            System.out.println("NO");
            out.print("NO");

        }
    }

    Automaton readAuto() {
        int n = in.nextInt();
        int m = in.nextInt();
        int k = in.nextInt();
        Automaton a = new Automaton(n);
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


    boolean bfsEquivCheck(Automaton a1, Automaton a2) {
        visited1 = new boolean[a1.states + 1];
        visited2 = new boolean[a2.states + 1];
        ArrayDeque<Pair> queue = new ArrayDeque<>();
        queue.add(new Pair(0, 0));
        visited1[0] = true;
        visited2[0] = true;
        while (!queue.isEmpty()) {
            Pair cur = queue.poll();
            int u = cur.fst;
            int v = cur.snd;
            if (a1.isTerminal(u) != a2.isTerminal(v)) {
                return false;
            }
            for (char c = 'a'; c < 'a' + 26; c++) {
                int to1 = a1.canGo(u, c);
                int to2 = a2.canGo(v, c);
                if (!(to1 == a1.states && to2 == a2.states) && (!visited1[to1] || !visited2[to2])) {
                    queue.add(new Pair(to1, to2));
                    if (to1 != a1.states) {
                        visited1[to1] = true;
                    }
                    if (to2 != a2.states) {
                        visited2[to2] = true;
                    }
                }
            }
        }
        return true;
    }
    class Automaton {
        HashSet<Integer> terminals;
        HashMap<Character, Integer>[] transfers;
        int states;
        Automaton(int states) {
            terminals = new HashSet<>();
            this.states = states;
            transfers = new HashMap[states + 1];
            for (int i = 0; i <= states; i++) {
                transfers[i] = new HashMap<>();
            }
            for (char c = 'a'; c < 'a' + 26; c++) {
                transfers[states].put(c, states);
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
            return states;
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