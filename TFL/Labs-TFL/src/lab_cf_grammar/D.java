package lab_cf_grammar;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by  baba_beda on 11/5/15.
 */
public class D {
    Scanner in;
    PrintWriter out;
    int MODULO = 1000000007;

    public static void main(String[] args) {
        new D().run();
    }

    void run() {
        try {
            in = new Scanner(new FileInputStream(new File("nfc" + ".in")));
            out = new PrintWriter(new File("nfc" + ".out"));
            solve();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void solve() {
        CFGrammar grammar = parseGrammar();
        String word = in.next();
        out.print(grammar.checkWord(word));
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
        }
        return grammar;
    }

    class CFGrammar {
        HashMap<Character, HashSet<String>> rules = new HashMap<>();
        HashMap<Character, Integer> nonTerminals = new HashMap<>();
        long[][][] dynamic;

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


        void collectNonTerminals() {
            int count = 0;
            for (Map.Entry<Character, HashSet<String>> entry : rules.entrySet()) {
                if (!nonTerminals.containsKey(entry.getKey())) {
                    nonTerminals.put(entry.getKey(), count++);
                }
                for (String right : entry.getValue()) {
                    for (char c : right.toCharArray()) {
                        if (isNonTerminal(c) && !nonTerminals.containsKey(c)) {
                            nonTerminals.put(c, count++);
                        }
                    }
                }
            }
        }

        int checkWord(String word) {
            collectNonTerminals();
            dynamic = new long[nonTerminals.size()][word.length()][word.length()];
            fillDynamic(word);
            return (int) (dynamic[nonTerminals.get(st)][0][word.length() - 1] % MODULO);
        }

        void fillDynamic(String word) {
            char[] wordCh = word.toCharArray();
            int n = word.length();
            for (int i = 0; i < n; i++) {
                for (Map.Entry<Character, HashSet<String>> entry : rules.entrySet()) {
                    if (entry.getValue().contains(Character.toString(wordCh[i]))) {
                        dynamic[nonTerminals.get(entry.getKey())][i][i]++;
                    }
                }
            }
            for (int m = 1; m < n; m++) {
                for (int i = 0; i < n - m; i++) {
                    int j = i + m;
                    for (Map.Entry<Character, HashSet<String>> entry : rules.entrySet()) {
                        int a = nonTerminals.get(entry.getKey());
                        for (String right : entry.getValue()) {
                            if (right.length() == 2) {
                                int b = nonTerminals.get(right.charAt(0));
                                int c = nonTerminals.get(right.charAt(1));
                                for (int k = i; k < j; k++) {
                                    dynamic[a][i][j] +=
                                            dynamic[b][i][k] * dynamic[c][k + 1][j];
                                    dynamic[a][i][j] %= MODULO;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
