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
public class Problem3 {
    public static void main(String[] args) {
        new Problem3().run();
    }

    Scanner in;
    PrintWriter out;

    void run() {
        try {
            in = new Scanner(new FileInputStream(new File("minimization" + ".in")));
            out = new PrintWriter(new File("minimization" + ".out"));
            solve();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void solve() {
        Automata a = readAuto();

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
}