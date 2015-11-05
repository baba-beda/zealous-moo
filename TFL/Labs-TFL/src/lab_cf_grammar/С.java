package lab_cf_grammar;


import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by  baba_beda on 11/5/15.
 */
public class С {
    public static void main(String[] args) {
        new С().run();
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
        int n = in.nextInt();
        char st = in.next().charAt(0);
        CFGrammar grammar = new CFGrammar();
        HashSet<Character> leftSides = new HashSet<>();
        in.nextLine();
        for (int i = 0; i < n; i++) {
            String[] rule = in.nextLine().replaceAll(" ", "").split("->");
            String right = "";
            if (rule.length == 2) {
                right = rule[1];
            }
            grammar.addRule(rule[0].charAt(0), right);
            leftSides.add(rule[0].charAt(0));
        }
        ArrayList<Character> ans = new ArrayList<>();
        grammar.goDeeper(st);
        for (char c : grammar.nonTerminals) {
            if (!grammar.rules.containsKey(c)) {
                ans.add(c);
            }
        }
        for (char c : leftSides) {
            if (!grammar.used.contains(c)) {
                ans.add(c);
            }
        }
        Collections.sort(ans);
        for (char c : ans) {
            System.out.print(c + " ");
        }
    }

    class CFGrammar {
        HashMap<Character, HashSet<String>> rules = new HashMap<>();
        HashSet<Character> epsRules = new HashSet<>();
        HashSet<Character> used = new HashSet<>();
        HashSet<Character> nonTerminals = new HashSet<>();

        void goDeeper(char c) {
            used.add(c);
            if (rules.containsKey(c)) {
                for (String s : rules.get(c)) {
                    for (char ch : s.toCharArray()) {
                        if (!isTerminal(ch) && !used.contains(ch)) {
                            nonTerminals.add(ch);
                            goDeeper(ch);
                        }
                    }
                }
            }
        }
        void addRule(char c, String next) {
            if (!rules.containsKey(c)) {
                rules.put(c, new HashSet<>());
            }
            rules.get(c).add(next);
        }
        boolean isTerminal(char c) {
            return Character.isLowerCase(c);
        }

        public HashSet<Character> getEpsRules(char st) {
            isEps(st);
            return epsRules;
        }

        boolean isEps (char c) {
            if (isTerminal(c)) {
                return false;
            }
            if (rules.get(c).contains("") || epsRules.contains(c)) {
                return true;
            }
            boolean ans = false;
            for (String right : rules.get(c)) {
                boolean auxAns = true;
                for (char ch : right.toCharArray()) {
                    boolean is = isEps(ch);
                    auxAns &= is;
                    if (is) {
                        epsRules.add(ch);
                    }
                }
                ans |= auxAns;
            }
            if (ans) {
                epsRules.add(c);
            }
            return ans;
        }
    }
}