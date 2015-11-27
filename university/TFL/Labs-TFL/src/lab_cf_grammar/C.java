package lab_cf_grammar;


import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by  baba_beda on 11/5/15.
 */
public class C {
    public static void main(String[] args) {
        new C().run();
    }

    Scanner in;
    PrintWriter out;

    void run() {
        try {
            in = new Scanner(new FileInputStream(new File("useless" + ".in")));
            out = new PrintWriter(new File("useless" + ".out"));
            solve();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void solve() {
        CFGrammar grammar = parseGrammar();
        for (char c : grammar.getUseless()) {
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
            boolean containsOnlyTerminals = true;
            for (char c : right.toCharArray()) {
                if (Character.isUpperCase(c)) {
                    containsOnlyTerminals = false;
                    break;
                }
            }
            if (containsOnlyTerminals) {
                grammar.generatingRules.add(rule[0].charAt(0));
            }
        }
        return grammar;
    }

    class CFGrammar {
        HashMap<Character, HashSet<String>> rules = new HashMap<>();
        HashSet<Character> generatingRules = new HashSet<>();
        HashSet<Character> reachableRules = new HashSet<>();
        HashSet<Character> nonTerminals = new HashSet<>();


        char st;

        CFGrammar(char st) {
            this.st = st;
        }

        boolean isNonTerminal(char c) {
            return Character.isUpperCase(c);
        }
        void addRule(char left, String right) {
            if (!rules.containsKey(left)) {
                rules.put(left, new HashSet<>());
            }
            rules.get(left).add(right);
        }

        void checkGenerating() {
            HashSet<Character> aux = new HashSet<>(generatingRules);
            while (true) {
                for(Map.Entry<Character, HashSet<String>> entry : rules.entrySet()) {
                    for (String right : entry.getValue()) {
                        boolean auxGen = true;
                        for (char c : right.toCharArray()) {
                            auxGen &= generatingRules.contains(c);
                        }
                        if (auxGen) {
                            aux.add(entry.getKey());
                            break;
                        }
                    }
                }
                if (aux.equals(generatingRules)) {
                    break;
                }
                else {
                    generatingRules = new HashSet<>(aux);
                }
            }
        }
        void checkReachable() {
            reachableRules.add(st);
            HashSet<Character> aux = new HashSet<>(reachableRules);
            while (true) {
                for(Map.Entry<Character, HashSet<String>> entry : rules.entrySet()) {
                    if (reachableRules.contains(entry.getKey())) {
                        for (String right : entry.getValue()) {
                            for (char c : right.toCharArray()) {
                                if (isNonTerminal(c)) {
                                    aux.add(c);
                                }
                            }
                        }
                    }
                }
                if (aux.equals(reachableRules)) {
                    break;
                }
                else {
                    reachableRules = new HashSet<>(aux);
                }
            }
        }


        void collectNonTerminals() {
            for (Map.Entry<Character, HashSet<String>> entry : rules.entrySet()) {
                nonTerminals.add(entry.getKey());
                for (String right : entry.getValue()) {
                    for (char c : right.toCharArray()) {
                        if (isNonTerminal(c)) {
                            nonTerminals.add(c);
                        }
                    }
                }
            }
        }

        ArrayList<Character> getUseless() {
            collectNonTerminals();
            checkGenerating();
            checkReachable();
            ArrayList<Character> ans = new ArrayList<>();
            for (char c : nonTerminals) {
                if (!generatingRules.contains(c) || !reachableRules.contains(c)) {
                    ans.add(c);
                }
            }
            Collections.sort(ans);
            return ans;
        }
    }
}