package lab_cf_grammar;


import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by  baba_beda on 11/5/15.
 */
public class E {
    public static void main(String[] args) {
        new E().run();
    }

    Scanner in;
    PrintWriter out;

    void run() {
        try {
            in = new Scanner(new FileInputStream(new File("cf" + ".in")));
            out = new PrintWriter(new File("cf" + ".out"));
            solve();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void solve() {
        CFGrammar grammar = parseGrammar();
        grammar.deleteLongRules();
    }

    CFGrammar parseGrammar() {
        int n = in.nextInt();
        CFGrammar grammar = new CFGrammar(in.next());
        in.nextLine();
        for (int i = 0; i < n; i++) {
            String[] rule = in.nextLine().replaceAll(" ", "").split("->");
            String right = "";
            if (rule.length == 2) {
                right = rule[1];
            }
            grammar.addRule(rule[0], right);
            boolean containsOnlyTerminals = true;
            for (char c : right.toCharArray()) {
                if (Character.isUpperCase(c)) {
                    containsOnlyTerminals = false;
                    break;
                }
            }
            if (containsOnlyTerminals) {
                grammar.generatingRules.add(rule[0]);
            }
            if (rule.length == 1) {
                grammar.epsRules.add(rule[0]);
            }
        }
        return grammar;
    }

    class CFGrammar {
        HashMap<String, HashSet<ArrayList<String>>> rules = new HashMap<>();
        HashSet<String> generatingRules = new HashSet<>();
        HashSet<String> reachableRules = new HashSet<>();
        HashSet<String> nonTerminals = new HashSet<>();
        HashSet<String> epsRules = new HashSet<>();

        String st;

        CFGrammar(String st) {
            this.st = st;
        }

        boolean isNonTerminal(char c) {
            return Character.isUpperCase(c);
        }

        void addRule(String left, String right) {
            if (!rules.containsKey(left)) {
                rules.put(left, new HashSet<>());
            }
            ArrayList<String> lexems = new ArrayList<>();
            for (char c : right.toCharArray()) {
                lexems.add(Character.toString(c));
            }
            rules.get(left).add(lexems);
        }

        void deleteLongRules() {
            HashMap<String, HashSet<ArrayList<String>>> newRules = new HashMap<>();
            ArrayList<String> aux = new ArrayList<>();
            for (Map.Entry<String, HashSet<ArrayList<String>>> entry : rules.entrySet()) {
                newRules.put(entry.getKey(), new HashSet<>());
                int count = 0;
                for (ArrayList<String> right : entry.getValue()) {
                    if (right.size() > 2) {
                        String lastSt = entry.getKey();
                        for (int i = 0; i < right.size() - 2; i++) {
                            String newSt = entry.getKey() + Integer.toString(count++);
                            aux.add(right.get(i));
                            aux.add(newSt);
                            newRules.get(lastSt).add((ArrayList<String>)aux.clone());
                            lastSt = newSt;
                            newRules.put(newSt, new HashSet<>());
                            aux.clear();
                        }
                        aux.add(right.get(right.size() - 2));
                        aux.add(right.get(right.size() - 1));
                        newRules.get(lastSt).add((ArrayList<String>)aux.clone());
                        aux.clear();
                    }
                    else {
                        newRules.get(entry.getKey()).add(right);
                    }
                }
            }
            rules = new HashMap<>(newRules);
        }

        void checkEps() {
            HashSet<String> aux = new HashSet<>();
            aux.addAll(epsRules);
            while (true) {
                for (Map.Entry<String, HashSet<ArrayList<String>>> entry : rules.entrySet()) {
                    for (ArrayList<String> right : entry.getValue()) {
                        boolean auxEps = true;
                        for (String c : right) {
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

        void deleteEpsRules() {
            
        }

    }
}