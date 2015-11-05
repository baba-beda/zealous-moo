package lab_cf_grammar;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by  baba_beda on 11/3/15.
 */
public class A {
    public static void main(String[] args) {
        new A().run();
    }

    Scanner in;
    PrintWriter out;

    void run() {
        try {
            in = new Scanner(new FileInputStream(new File("automaton" + ".in")));
            out = new PrintWriter(new File("automaton" + ".out"));
            solve();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void solve() {
        CFGrammar grammar = new CFGrammar();
        int n = in.nextInt();
        char st = in.next().charAt(0);
        for (int i = 0; i < n; i++) {
            char from = in.next().charAt(0);
            in.next();
            String to = in.next();
            grammar.addTransfer(from, to);
        }
        int m = in.nextInt();
        for (int i = 0; i < m; i++) {
            if (grammar.accepts(in.next(), st)) {
                out.println("yes");
            }
            else {
                out.println("no");
            }
        }
    }

    class CFGrammar {
        HashMap<Character, ArrayList<String>> transfers = new HashMap<>();
        void addTransfer(char from, String to) {
            if (!transfers.containsKey(from)) {
                transfers.put(from, new ArrayList<>());
            }
            transfers.get(from).add(to);
        }

        boolean accepts(String word, char st) {
            char[] wordA = word.toCharArray();
            HashSet<Character> accessible = new HashSet<>();
            accessible.add(st);
            HashSet<Character> aux = new HashSet<>();
            boolean result = true;
            for (int i = 0; i < word.length() - 1; i++) {
                for (char state : accessible) {
                    for (String to : transfers.get(state)) {
                        if (to.charAt(0) == wordA[i] && to.length() == 2) {
                            aux.add(to.charAt(1));
                        }
                    }
                }
                accessible = aux;
                if (aux.isEmpty()) {
                    result = false;
                    break;
                }
            }
            for (char state : accessible) {
                for (String to : transfers.get(state)) {
                    if (to.charAt(0) == wordA[word.length() - 1] && to.length() == 1) {
                        return result;
                    }
                }
            }
            return false;
        }
    }
}
