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
        int n = in.nextInt();
        char st = in.next().charAt(0);
        in.nextLine();
        CFGrammar grammar = new CFGrammar();
        for (int i = 0; i < n; i++) {
            String rule = in.nextLine();
            rule = rule.replaceAll(" ", "");
            String[] splitted = rule.split("->");
            String right = "";
            if (splitted.length == 2) {
                right = splitted[1];
            }
            grammar.addRule(splitted[0].charAt(0), right);
        }
        ArrayList<Character> ans = new ArrayList<>();
        ans.addAll(grammar.getEpsRules(st));
        Collections.sort(ans);

        for (char c : ans) {
            out.print(c + " ");
        }
    }

    class CFGrammar {
        HashMap<Character, HashSet<String>> rules = new HashMap<>();
        HashSet<Character> epsRules = new HashSet<>();
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

        boolean isEpsBfs(char st) {

            return false;
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