package lab_automata;


import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by  baba_beda on 9/16/15.
 */
public class Problem2 {
    public static void main(String[] args) {
        new Problem2().run();
    }

    Scanner in;
    PrintWriter out;

    void run() {
        try {
            in = new Scanner(new FileInputStream(new File("problem2" + ".in")));
            out = new PrintWriter(new File("problem2" + ".out"));
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
        NonDeterministicAutomata automata = new NonDeterministicAutomata(n);
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

    class NonDeterministicAutomata {
        HashSet<Integer> terminals;
        HashMap<Character, LinkedList<Integer>>[] transfers;
        int states;
        NonDeterministicAutomata(int states) {
            terminals = new HashSet<>();
            this.states = states;
            transfers = new HashMap[states];
            for (int i = 0; i < states; i++) {
                transfers[i] = new HashMap<>();
            }
        }
        void addTransfer(int from, int to, char c) {
            if (!transfers[from].containsKey(c)) {
                transfers[from].put(c, new LinkedList<>());
            }
            transfers[from].get(c).add(to);
        }
        void addTerminal(int t) {
            terminals.add(t);
        }
        boolean isTerminal(int t) {
            return terminals.contains(t);
        }

        LinkedList<Integer> canGo(int from, char c) {
            if (transfers[from].containsKey(c)) {
                return transfers[from].get(c);
            }
            return null;
        }

        boolean transfer(char[] word, int from, int pos) {
            if (pos == word.length) {
                return isTerminal(from);
            }
            LinkedList<Integer> can = canGo(from, word[pos]);
            if (can != null) {
                boolean is = false;
                for (int t : can) {
                    is |= transfer(word, t, pos + 1);
                }
                return is;
            }
            return false;
        }
        boolean accepts(char[] word) {
            HashSet<Integer> accessible = new HashSet<>();
            accessible.add(0);

            for (char c : word) {
                HashSet<Integer> aux = new HashSet<>();
                accessible.stream().filter(state -> canGo(state, c) != null).forEach(state -> aux.addAll(canGo(state, c)));
                accessible = aux;
            }
            boolean result = false;
            for (int t : terminals) {
                result |= accessible.contains(t);
            }
            return result;
        }
    }
}