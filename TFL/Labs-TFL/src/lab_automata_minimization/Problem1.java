package lab_automata_minimization;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by  baba_beda on 11/3/15.
 */
public class Problem1 {
    public static void main(String[] args) {
        new Problem1().run();
    }

    Scanner in;
    PrintWriter out;

    void run() {
        try {
            in = new Scanner(new FileInputStream(new File("isomorphism" + ".in")));
            out = new PrintWriter(new File("isomorphism" + ".out"));
            solve();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void solve() {
        Automaton a1 = readAuto();
        Automaton a2 = readAuto();
        visited1 = new boolean[a1.states];
        boolean ans = checkIsomorphism(a1, a2, 0, 0);
        if (ans) {
            out.println("YES");
        }
        else {
            out.println("NO");
        }
    }

    boolean visited1[];
    boolean checkIsomorphism(Automaton a1, Automaton a2, int u, int v) {
        visited1[u] = true;
        if (a1.isTerminal(u) != a2.isTerminal(v)) {
            return false;
        }
        boolean result = true;
        for (Map.Entry<Character, Integer> entry : a1.transfers[u].entrySet()) {
            int t1 = entry.getValue();
            int t2 = a2.transfers[v].containsKey(entry.getKey()) ? a2.transfers[v].get(entry.getKey()) : -1;
            if (a1.isDevilish(t1) != a2.isDevilish(t2)) {
                return false;
            }
            if (!visited1[t1]) {
                result = result & checkIsomorphism(a1, a2, t1, t2);
            }
        }
        return result;
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

    class Automaton {
        HashSet<Integer> terminals;
        HashMap<Character, Integer>[] transfers;
        int states;
        Automaton(int states) {
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
}