package lab_automata_minimization;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.*;

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
        a.markReachable(1);
        a.addDevilishTransfers();
        a.addReturnTransfers();
        boolean[][] marked = buildTable(a);

        int[] component = new int[a.states + 1];
        Arrays.fill(component, -1);
        for (int i = 0; i <= a.states; i++) {
            if (!marked[0][i]) {
                component[i] = 0;
            }
        }
        int componentsCount = 0;
        for (int i = 1; i <= a.states; i++) {
            if (!a.reachableFromStart[i]) {
                continue;
            }
            if (component[i] == -1) {
                componentsCount++;
                component[i] = componentsCount;
                for (int j = i + 1; j <= a.states; j++) {
                    if (!marked[i][j]) {
                        component[j] = componentsCount;
                    }
                }
            }
        }
        Automata ans = buildDFA(a, component, componentsCount);
        System.out.println(ans.states + " " + ans.countTransfers() + " " + ans.terminals.size());
        out.println(ans.states + " " + ans.countTransfers() + " " + ans.terminals.size());
        for (int st : ans.terminals) {
            System.out.print(st + " ");
            out.print(st + " ");
        }
        System.out.println();
        out.println();
        for (int i = 1; i <= ans.states; i++) {
            for (Map.Entry<Character, Integer> entry : ans.transfers[i].entrySet()) {
                System.out.println(i + " " + entry.getValue() + " " + entry.getKey());
                out.println(i + " " + entry.getValue() + " " + entry.getKey());
            }
        }
    }

    boolean[][] buildTable(Automata a) {
        boolean[][] marked = new boolean[a.states + 1][a.states + 1];
        ArrayDeque<Pair> queue = new ArrayDeque<>();

        for (int i = 0; i <= a.states; i++) {
            for (int j = 0; j <= a.states; j++) {
                if (!marked[i][j] && a.isTerminal(i) != a.isTerminal(j)) {
                    marked[i][j] = marked[j][i] = true;
                    queue.add(new Pair(i, j));
                }
            }
        }
        while (!queue.isEmpty()) {
            Pair pair = queue.poll();
            int u = pair.fst;
            int v = pair.snd;
            for (char c : a.alphabet) {
                if (a.returnTransfers[u].containsKey(c)) {
                    for (int r : a.returnTransfers[u].get(c)) {
                        if (a.returnTransfers[v].containsKey(c)) {
                            for (int s : a.returnTransfers[v].get(c)) {
                                if (!marked[r][s]) {
                                    marked[r][s] = marked[s][r] = true;
                                    queue.add(new Pair(r, s));
                                }
                            }
                        }
                    }
                }
            }
        }

        return marked;
    }

    Automata readAuto() {
        int n = in.nextInt();
        int m = in.nextInt();
        int k = in.nextInt();
        Automata a = new Automata(n);
        for (int i = 0; i < k; i++) {
            a.addTerminal(in.nextInt());
        }
        for (int i = 0; i < m; i++) {
            int from = in.nextInt();
            int to = in.nextInt();
            char c = in.next().charAt(0);
            a.addTransfer(from, to, c);
            a.alphabet.add(c);

        }
        return a;
    }

    Automata buildDFA(Automata source, int[] component, int componentsCount) {
        Automata result = new Automata(componentsCount);
        HashMap<Integer, HashSet<Integer>> componentsAux = new HashMap<>();
        for (int i = 1; i < component.length; i++) {
            if (!componentsAux.containsKey(component[i])) {
                componentsAux.put(component[i], new HashSet<>());
            }
            componentsAux.get(component[i]).add(i);
        }
        for (Map.Entry<Integer, HashSet<Integer>> entry : componentsAux.entrySet()) {
            if (entry.getKey() > 0) {
                for (int r : entry.getValue()) {
                    if (source.isTerminal(r)) {
                        result.addTerminal(entry.getKey());
                        break;
                    }
                }
            }
        }
        for (int i = 1; i <= source.states; i++) {
            for (Map.Entry<Character, Integer> entry : source.transfers[i].entrySet()) {
                if (entry.getValue() > 0 && component[i] > 0 && component[entry.getValue()] > 0) {
                    result.addTransfer(component[i], component[entry.getValue()], entry.getKey());
                }
            }
        }
        return result;
    }
    class Automata {
        HashSet<Integer> terminals;
        HashMap<Character, Integer>[] transfers;
        HashMap<Character, HashSet<Integer>>[] returnTransfers;
        boolean[] reachableFromStart;
        HashSet<Character> alphabet;
        int states;
        int transfersCount;
        Automata(int states) {
            terminals = new HashSet<>();
            this.states = states;
            transfers = new HashMap[states + 1];
            returnTransfers = new HashMap[states + 1];
            for (int i = 0; i <= states; i++) {
                transfers[i] = new HashMap<>();
                returnTransfers[i] = new HashMap<>();
            }
            reachableFromStart = new boolean[states + 1];
            alphabet = new HashSet<>();

        }
        void addTransfer(int from, int to, char c) {
            transfers[from].put(c, to);
        }

        void addDevilishTransfers() {
            for (int st = 0; st <= states; st++) {
                for (char c : alphabet) {
                    if (!transfers[st].containsKey(c)) {
                        transfers[st].put(c, 0);
                    }
                }
            }
        }

        int countTransfers() {
            transfersCount = 0;
            for (int i = 1; i <= states; i++) {
                transfersCount += transfers[i].size();
            }
            return transfersCount;
        }
        void addReturnTransfers() {
            for (int st = 0; st <= states; st++) {
                for (char c : alphabet) {
                    int to = canGo(st, c);
                    if (!returnTransfers[to].containsKey(c)) {
                        returnTransfers[to].put(c, new HashSet<>());
                    }
                    returnTransfers[to].get(c).add(st);
                }
            }
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
            return 0;
        }

        void markReachable(int v) {
            reachableFromStart[v] = true;
            for (char c : alphabet) {
                int to = canGo(v, c);
                if (!reachableFromStart[to]) {
                    markReachable(to);
                }
            }
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