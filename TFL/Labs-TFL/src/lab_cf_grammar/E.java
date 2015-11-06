package lab_cf_grammar;


import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

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
        grammar.transformToNormalForm();
        if (grammar.checkWord(in.next())) {
            System.out.println("yes");
            out.print("yes");
        }
        else {
            System.out.println("no");
            out.print("no");
        }
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
        HashMap<String, Integer> nonTerminals = new HashMap<>();
        HashSet<String> epsRules = new HashSet<>();
        HashSet<ChainRule> chainRules = new HashSet<>();
        HashMap<String, String> termsToNonTerms = new HashMap<>();
        boolean[][][] dynamic;

        String st;

        CFGrammar(String st) {
            this.st = st;
        }

        boolean isNonTerminal(String s) {
            if (s.isEmpty()) {
                return false;
            }
            return Character.isUpperCase(s.charAt(0));
        }

        void addRule(String left, String right) {
            if (!rules.containsKey(left)) {
                rules.put(left, new HashSet<>());
            }
            ArrayList<String> lexems = new ArrayList<>();
            for (char c : right.toCharArray()) {
                lexems.add(Character.toString(c));
            }
            if (right.equals("")) {
                lexems.add("");
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
                        String s1 = right.get(right.size() - 2);
                        String s2 = right.get(right.size() - 1);
                        if (!isNonTerminal(s1) && !isNonTerminal(s2)) {
                            String newSt = entry.getKey() + Integer.toString(count++);
                            aux.add(s1);
                            aux.add(newSt);
                            newRules.get(lastSt).add((ArrayList<String>) aux.clone());
                            aux.clear();
                            aux.add(s2);
                            newRules.put(newSt, new HashSet<>());
                            newRules.get(newSt).add((ArrayList<String>) aux.clone());
                            aux.clear();
                        }
                        else {
                            aux.add(s1);
                            aux.add(s2);
                            newRules.get(lastSt).add((ArrayList<String>) aux.clone());
                            aux.clear();
                        }
                    }
                    else {
                        newRules.get(entry.getKey()).add(right);
                    }
                }
            }
            rules = new HashMap<>(newRules);
            printRules();
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
            checkEps();
            HashMap<String, HashSet<ArrayList<String>>> newRules = new HashMap<>();
            for (Map.Entry<String, HashSet<ArrayList<String>>> entry : rules.entrySet()) {
                for (ArrayList<String> right : entry.getValue()) {
                    HashSet<ArrayList<String>> whatToAdd = new HashSet<>();
                    for (String term : right) {
                        if (term.equals("")) {
                            continue;
                        }
                        if (whatToAdd.isEmpty()) {
                            whatToAdd.add(new ArrayList<>());
                        }
                        if (!epsRules.contains(term)) {
                            for (ArrayList<String> list : whatToAdd) {
                                list.add(term);
                            }
                        }
                        else {
                            HashSet<ArrayList<String>> aux = new HashSet<>();
                            for (ArrayList<String> list : whatToAdd) {
                                ArrayList<String> auxList = new ArrayList<>(list);
                                auxList.add(term);
                                aux.add(auxList);
                            }
                            whatToAdd.addAll(aux);
                        }
                    }
                    if (whatToAdd.isEmpty()) {
                        continue;
                    }
                    if (!newRules.containsKey(entry.getKey())) {
                        newRules.put(entry.getKey(), new HashSet<>());
                    }
                    newRules.get(entry.getKey()).addAll(whatToAdd);
                }
            }
            HashMap<String, HashSet<ArrayList<String>>> newNewRules = new HashMap<>();
            for (Map.Entry<String, HashSet<ArrayList<String>>> entry : newRules.entrySet()) {
                HashSet<ArrayList<String>> newRight = new HashSet<>();
                for (ArrayList<String> right : entry.getValue()) {
                    if (!right.isEmpty()) {
                        newRight.add(right);
                    }
                }
                if (!newRight.isEmpty()) {
                    newNewRules.put(entry.getKey(), newRight);
                }
            }
            if (epsRules.contains(st)) {
                String newSt = st + "'";
                newNewRules.put(newSt, new HashSet<>());
                ArrayList<String> aux = new ArrayList<>();
                aux.add(st);
                newNewRules.get(newSt).add((ArrayList<String>) aux.clone());
                aux.clear();
                aux.add("");
                newNewRules.get(newSt).add(aux);
                st = newSt;
            }

            rules = new HashMap<>(newNewRules);
            printRules();
        }

        void checkChainRules() {
            collectNonTerminals();
            chainRules.addAll(nonTerminals.keySet().stream().map(s -> new ChainRule(s, s)).collect(Collectors.toList()));
            HashSet<ChainRule> aux = new HashSet<>(chainRules);
            while (true) {
                for (Map.Entry<String, HashSet<ArrayList<String>>> entry : rules.entrySet()) {
                    for (ArrayList<String> right : entry.getValue()) {
                        if (right.size() == 1 && isNonTerminal(right.get(0))) {
                            for (ChainRule chainRule : chainRules) {
                                if (chainRule.right.equals(entry.getKey())) {
                                    aux.add(new ChainRule(chainRule.left, right.get(0)));
                                }
                            }
                        }
                    }
                }
                if (aux.equals(chainRules)) {
                    break;
                }
                else {
                    chainRules = new HashSet<>(aux);
                }
            }
        }

        void deleteChainRules() {
            checkChainRules();
            HashMap<String, HashSet<ArrayList<String>>> newRules = new HashMap<>();
            for (ChainRule rule : chainRules) {
                if (!newRules.containsKey(rule.left)) {
                    newRules.put(rule.left, new HashSet<>());
                }
                if (rules.containsKey(rule.right)) {
                    for (ArrayList<String> right : rules.get(rule.right)) {
                        if (right.size() != 1 || !isNonTerminal(right.get(0))) {
                            newRules.get(rule.left).add(right);
                        }
                    }
                }
            }
            rules = new HashMap<>(newRules);
            printRules();
        }

        void checkGenerating() {
            for (Map.Entry<String, HashSet<ArrayList<String>>> entry : rules.entrySet()) {
                for (ArrayList<String> right : entry.getValue()) {
                    boolean containsOnlyTerminals = true;
                    for (String s : right) {
                        containsOnlyTerminals &= (!isNonTerminal(s));
                    }
                    if (containsOnlyTerminals) {
                        generatingRules.add(entry.getKey());
                        break;
                    }
                }
            }
            HashSet<String> aux = new HashSet<>(generatingRules);
            while (true) {
                for (Map.Entry<String, HashSet<ArrayList<String>>> entry : rules.entrySet()) {
                    for (ArrayList<String> right : entry.getValue()) {
                        boolean auxGen = true;
                        for (String s : right) {
                            auxGen &= (generatingRules.contains(s) || !isNonTerminal(s));
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
            HashSet<String> aux = new HashSet<>(reachableRules);
            while (true) {
                for (Map.Entry<String, HashSet<ArrayList<String>>> entry : rules.entrySet()) {
                    if (reachableRules.contains(entry.getKey())) {
                        for (ArrayList<String> right : entry.getValue()) {
                            for (String s : right) {
                                if (isNonTerminal(s)) {
                                    aux.add(s);
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

        void deleteUselessRules() {
            checkGenerating();
            checkReachable();
            HashMap<String, HashSet<ArrayList<String>>> newRules = new HashMap<>();
            for (Map.Entry<String, HashSet<ArrayList<String>>> entry : rules.entrySet()) {
               if (generatingRules.contains(entry.getKey()) && reachableRules.contains(entry.getKey())) {
                   if (!newRules.containsKey(entry.getKey())) {
                       newRules.put(entry.getKey(), new HashSet<>());
                   }
                   for (ArrayList<String> right : entry.getValue()) {
                       boolean aux = true;
                       for (String rule : right) {
                           if (isNonTerminal(rule)) {
                               aux &= (generatingRules.contains(rule) && reachableRules.contains(rule));
                           }
                       }
                       if (aux) {
                           newRules.get(entry.getKey()).add(right);
                       }
                   }
               }
            }
            rules = new HashMap<>(newRules);
            printRules();
        }

        void transformToNormalForm() {
            System.out.println("Start rules");
            printRules();
            System.out.println("Deleting long rules");
            deleteLongRules();
            System.out.println("Deleting eps rules");
            deleteEpsRules();
            System.out.println("Deleting chain rules");
            deleteChainRules();
            System.out.println("Deleting useless rules");
            deleteUselessRules();
            System.out.println("Changing terminals in rules to NonTerminals");
            changeTerminalsToNon();

        }

        void changeTerminalsToNon() {
            for (Map.Entry<String, HashSet<ArrayList<String>>> entry : rules.entrySet()) {
                for (ArrayList<String> right : entry.getValue()) {
                    for (int i = 0; i < right.size(); i++) {
                        String s = right.get(i);
                        if (!isNonTerminal(s) && right.size() > 1) {
                            if (!termsToNonTerms.containsKey(s)) {
                                String newSt = Character.toUpperCase(s.charAt(0)) + "''";
                                termsToNonTerms.put(s, newSt);
                            }
                            right.set(i, termsToNonTerms.get(s));
                        }
                    }
                }
            }

            for (Map.Entry<String, String> entry : termsToNonTerms.entrySet()) {
                ArrayList<String> aux = new ArrayList<>();
                rules.put(entry.getValue(), new HashSet<>());
                aux.add(entry.getKey());
                rules.get(entry.getValue()).add(aux);
            }
            printRules();
        }

        void collectNonTerminals() {
            int count = 0;
            for (Map.Entry<String, HashSet<ArrayList<String>>> entry : rules.entrySet()) {
                if (!nonTerminals.containsKey(entry.getKey())) {
                    nonTerminals.put(entry.getKey(), count++);
                }
                for (ArrayList<String> right : entry.getValue()) {
                    for (String s : right) {
                        if (isNonTerminal(s) && !nonTerminals.containsKey(s)) {
                            nonTerminals.put(s, count++);
                        }
                    }
                }
            }
        }
        void printRules() {
            for (Map.Entry<String, HashSet<ArrayList<String>>> entry : rules.entrySet()) {
                System.out.print(entry.getKey() + " -> ");
                for (ArrayList<String> right : entry.getValue()) {
                    for (String s : right) {
                        if (s.isEmpty()) {
                            System.out.print("eps");
                        }
                        else {
                            System.out.print(s);
                        }
                    }
                    System.out.print(" | ");
                }
                System.out.println();
            }
        }

        boolean checkWord(String word) {
            collectNonTerminals();
            dynamic = new boolean[nonTerminals.size()][word.length()][word.length()];
            fillDynamic(word);
            return  (dynamic[nonTerminals.get(st)][0][word.length() - 1]);
        }

        void fillDynamic(String word) {
            char[] wordCh = word.toCharArray();
            int n = word.length();
            for (int i = 0; i < n; i++) {
                for (Map.Entry<String, HashSet<ArrayList<String>>> entry : rules.entrySet()) {
                    ArrayList<String> aux = new ArrayList<>();
                    aux.add(Character.toString(wordCh[i]));
                    if (entry.getValue().contains(aux)) {
                        dynamic[nonTerminals.get(entry.getKey())][i][i] = true;
                    }
                }
            }
            for (int m = 1; m < n; m++) {
                for (int i = 0; i < n - m; i++) {
                    int j = i + m;
                    for (Map.Entry<String, HashSet<ArrayList<String>>> entry : rules.entrySet()) {
                        int a = nonTerminals.get(entry.getKey());
                        for (ArrayList<String> right : entry.getValue()) {
                            if (right.size() == 2) {
                                int b = nonTerminals.get(right.get(0));
                                int c = nonTerminals.get(right.get(1));
                                for (int k = i; k < j; k++) {
                                    dynamic[a][i][j] |=
                                            dynamic[b][i][k] & dynamic[c][k + 1][j];
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    class ChainRule {
        String left, right;

        public ChainRule(String left, String right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return "(" + left + ", " + right + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ChainRule chainRule = (ChainRule) o;

            return left.equals(chainRule.left) && right.equals(chainRule.right);

        }

        @Override
        public int hashCode() {
            int result = left.hashCode();
            result = 31 * result + right.hashCode();
            return result;
        }
    }
}