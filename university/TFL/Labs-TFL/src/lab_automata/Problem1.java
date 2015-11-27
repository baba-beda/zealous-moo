package lab_automata;


import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by  baba_beda on 9/16/15.
 */
public class Problem1 {
    public static void main(String[] args) {
        new Problem1().run();
    }

    Scanner in;
    PrintWriter out;

    void run() {
        try {
            in = new Scanner(new FileInputStream(new File("problem1" + ".in")));
            out = new PrintWriter(new File("problem1" + ".out"));
            solve();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void solve() {
        char[] word = in.next().toCharArray();
        int n = in.nextInt();
        int m = in.nextInt();
        int k = in.nextInt();
        Automata automata = new Automata(n);
        for (int i = 0; i < k; i++) {
            automata.addTerminal(in.nextInt() - 1);
        }
        for (int i = 0; i < m; i++) {
            automata.addTransfer(in.nextInt() - 1, in.nextInt() - 1, in.next().charAt(0));
        }

        if (automata.accepts(word)) {
            out.print("Accepts");
        }
        else {
            out.print("Rejects");
        }
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
            if (isTerminal(curState)) {
                return true;
            }
            return false;
        }
    }

}