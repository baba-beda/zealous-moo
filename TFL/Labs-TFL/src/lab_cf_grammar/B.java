package lab_cf_grammar;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by  baba_beda on 11/3/15.
 */
public class B {
    public static void main(String[] args) {
        new B().run();
    }

    Scanner in;
    PrintWriter out;

    void run() {
        try {
            in = new Scanner(new FileInputStream(new File("epsilon" + ".in")));
            out = new PrintWriter(new File("epsilon" + ".out"));
            solve();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void solve() {
        CFGrammar grammar = parseGrammar();
        grammar.checkEps();
        ArrayList<Character> arrayList = new ArrayList<>(grammar.epsRules);
        Collections.sort(arrayList);
        for (char c : arrayList) {
            out.print(c + " ");
        }
    }

    CFGrammar parseGrammar() {
        int n = in.nextInt();
        CFGrammar grammar = new CFGrammar(in.next().charAt(0));
        in.nextLine();
        for (int i = 0; i < n; i++) {
            String[] rule = in.nextLine().replaceAll(" ", "").split("->");
            String right = "";
            if (rule.length == 2) {
                right = rule[1];
            }
            grammar.addRule(rule[0].charAt(0), right);
            if (rule.length == 1) {
                grammar.addEpsRule(rule[0].charAt(0));
            }
        }
        return grammar;
    }

    class CFGrammar {
        HashMap<Character, HashSet<String>> rules = new HashMap<>();
        HashSet<Character> epsRules = new HashSet<>();
        char st;

        CFGrammar(char st) {
            this.st = st;
        }

        boolean isNonTerminal(char c) {
            return Character.isUpperCase(c);
        }

        void addEpsRule(char c) {
            epsRules.add(c);
        }
        void addRule(char left, String right) {
            if (!rules.containsKey(left)) {
                rules.put(left, new HashSet<>());
            }
            rules.get(left).add(right);
        }

        void checkEps() {
            HashSet<Character> aux = new HashSet<>();
            aux.addAll(epsRules);
            while (true) {
                for (Map.Entry<Character, HashSet<String>> entry : rules.entrySet()) {
                    for (String right : entry.getValue()) {
                        boolean auxEps = true;
                        for (char c : right.toCharArray()) {
                            auxEps &= epsRules.contains(c);
                        }
                        if (auxEps) {
                            aux.add(entry.getKey());
                            break;
                        }
                    }
                }
                if (aux.equals(epsRules)) {
                    break;
                }
                else {
                    epsRules = new HashSet<>(aux);
                }
            }
        }
    }
}