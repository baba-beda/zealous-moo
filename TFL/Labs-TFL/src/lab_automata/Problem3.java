package lab_automata;


import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by  baba_beda on 9/16/15.
 */
public class Problem3 {
    public static void main(String[] args) {
        new Problem3().run();
    }

    Scanner in;
    PrintWriter out;

    void run() {
        try {
            in = new Scanner(new FileInputStream(new File("problem3" + ".in")));
            out = new PrintWriter(new File("problem3" + ".out"));
            solve();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void solve() {
        int n = in.nextInt();
        int m = in.nextInt();
        int k = in.nextInt();
        Automata automata = new Automata(n);
        for (int i = 0; i < k; i++) {
            automata.addTerminal(in.nextInt() - 1);
        }
        for (int i = 0; i < m; i++) {
            int from = in.nextInt() - 1;
            int to = in.nextInt() - 1;
            char c = in.next().charAt(0);
            automata.addTransfer(from, to);
        }
        automata.checkFalseStates(0);
        automata.checkCycle();

        out.print(automata.getWords() % MODULO);

    }
    class Automata {
        HashSet<Integer> terminals;
        ArrayList<Integer>[] transfers;
        ArrayList<Integer>[] reversedTransfers;
        boolean[] visitedStates;
        boolean[] acceptableStates;
        int states;
        long words;
        Automata(int states) {
            terminals = new HashSet<>();
            this.states = states;
            transfers = new ArrayList[states];
            reversedTransfers = new ArrayList[states];

            for (int i = 0; i < states; i++) {
                transfers[i] = new ArrayList<>();
                reversedTransfers[i] = new ArrayList<>();
            }
            visitedStates = new boolean[states];
            acceptableStates = new boolean[states];
            words = 0;
        }
        void addTransfer(int from, int to) {
            transfers[from].add(to);
            reversedTransfers[to].add(from);
        }
        void addTerminal(int t) {
            terminals.add(t);
        }


        void checkFalseStates(int state) {
            Stack<Integer> stack = new Stack<>();
            stack.push(state);
            while(!stack.isEmpty()) {
                int v = stack.pop();
                if (!acceptableStates[v]) {
                    acceptableStates[v] = true;
                    transfers[v].forEach(stack::push);
                }
            }
        }

        void checkCycle() {
            for (int t : terminals) {
                Arrays.fill(visitedStates, false);
                if (!acceptableStates[t]) {
                    continue;
                }
                Stack<Integer> stack = new Stack<>();
                stack.push(t);
                while (!stack.isEmpty()) {
                    int v = stack.pop();
                    if (!visitedStates[v]) {
                        visitedStates[v] = true;
                        HashSet<Integer> forward = new HashSet<>();
                        forward.addAll(reversedTransfers[v]);
                        (forward).stream().filter(to -> acceptableStates[to]).forEach(stack::push);
                    }
                    else {
                        out.print(-1);
                        out.close();
                        System.exit(0);
                    }
                }
            }
        }


        void countWords(int state) {
            if (!acceptableStates[state]) {
                return;
            }
            Stack<Integer> stack = new Stack<>();
            stack.push(state);
            while (!stack.isEmpty()) {
                int v = stack.pop();
                if (v == 0) {
                    words = (words + 1) % MODULO;
                }
                (reversedTransfers[v]).stream().filter(to -> acceptableStates[to]).forEach(stack::push);


            }
        }

        public long getWords() {
            terminals.forEach(this::countWords);
            return words;
        }
    }
    final int MODULO = 1000000007;
}