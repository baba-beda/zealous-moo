package lab_automata;


import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by  baba_beda on 9/19/15.
 */
public class Problem5 {
    public static void main(String[] args) {
        new Problem5().run();
    }

    Scanner in;
    PrintWriter out;

    void run() {
        try {
            in = new Scanner(new FileInputStream(new File("problem5" + ".in")));
            out = new PrintWriter(new File("problem5" + ".out"));
            solve();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void solve() {
        int n = in.nextInt();
        NonDeterministicAutomata automata = new NonDeterministicAutomata(n);
        int m = in.nextInt();
        int k = in.nextInt();
        int l = in.nextInt();
        HashSet<Character> alphabet = new HashSet<>();
        for (int i = 0; i < k; i++) {
            automata.addTerminal(in.nextInt() - 1);
        }
        for (int i = 0; i < m; i++) {
            int from = in.nextInt() - 1;
            int to = in.nextInt() - 1;
            char c = in.next().charAt(0);
            alphabet.add(c);
            automata.addTransfer(from, to, c);
        }

        DeterministicAutomata dfa = nfaToDfa(automata, alphabet);
        long[][] result = matrixToPower(dfa.transferMatrix, l, dfa.size);
        long ans = 0;
        for (int t : dfa.terminals) {
            ans = (ans + result[0][t]) % MODULO;
        }
        out.print(ans);
    }

    DeterministicAutomata nfaToDfa(NonDeterministicAutomata nfa, HashSet<Character> alphabet) {
        DeterministicAutomata dfa = new DeterministicAutomata(100);
        ArrayDeque<HashSet<Integer>> queue = new ArrayDeque<>();
        queue.add(new HashSet<>());
        queue.peekLast().add(0);
        dfa.addState(queue.getFirst());
        while (!queue.isEmpty()) {
            HashSet<Integer> popped = queue.pollFirst();
            for (char c : alphabet) {
                HashSet<Integer> newState = new HashSet<>();
                for (int state : popped) {
                    LinkedList<Integer> can = nfa.canGo(state, c);
                    if (can != null) {
                        newState.addAll(can);
                    }
                }
                if (!dfa.statesSets.containsKey(newState)) {
                    queue.add(newState);
                    dfa.addState(newState);
                }
                dfa.addTransfer(popped, newState);
            }
        }
        for (HashSet<Integer> state : dfa.statesSets.keySet()) {
            for (int t : nfa.terminals) {
                if (state.contains(t)) {
                    dfa.addTerminal(state);
                    break;
                }
            }
        }
        return dfa;
    }

    class DeterministicAutomata {
        HashSet<Integer> terminals;
        long[][] transferMatrix;

        int size;
        HashMap<HashSet<Integer>, Integer> statesSets;

        DeterministicAutomata(int n) {
            size = 0;
            terminals = new HashSet<>();
            transferMatrix = new long[n][n];
            statesSets = new HashMap<>();
        }

        void addTerminal(HashSet<Integer> state) {
            terminals.add(statesSets.get(state));
        }

        void addTransfer(HashSet<Integer> from, HashSet<Integer> to) {
            addTransfer(statesSets.get(from), statesSets.get(to));
        }

        void addTransfer(int from, int to) {
            transferMatrix[from][to]++;
        }
        void addState(HashSet<Integer> state) {
            statesSets.put(state, size++);
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

        LinkedList<Integer> canGo(int from, char c) {
            if (transfers[from].containsKey(c)) {
                return transfers[from].get(c);
            }
            return null;
        }

    }

    long[][] matrixToPower(long[][] matrix, int power, int size) {
        if (power == 1) {
            return matrix;
        }
        long[][] matrixP = matrixToPower(matrix, power / 2, size);
        long[][] aux = multiplyMatrices(matrixP, matrixP, size);
        if (power % 2 == 0) {
            return aux;
        }
        else {
            return multiplyMatrices(aux, matrix, size);
        }
    }


    long[][] multiplyMatrices(long[][] matrix1, long[][] matrix2, int size) {
        long[][] result = new long[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    result[i][j] = (result[i][j] +  matrix1[i][k] * matrix2[k][j]) % MODULO;
                }
            }
        }
        return result;
    }

    final int MODULO = 1000000007;
}