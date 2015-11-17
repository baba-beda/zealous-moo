import java.io.File;
import java.io.FileInputStream;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by  baba_beda on 11/12/15.
 */
public class C {
    public static void main(String[] args) {
        new C().run();
    }

    Scanner in;
    PrintWriter out;

    void run() {
        try {
            in = new Scanner(new FileInputStream(new File("trees" + ".in")));
            out = new PrintWriter(new File("trees" + ".out"));
            solve();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    Node[] nodes;
    void solve() {
        int n = in.nextInt();
        nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            String s = in.next();
            nodes[i].predicate = in.nextInt();
            if (s.equals("choice")) {
                nodes[i].next0 = in.nextInt() - 1;
                nodes[i].next1 = in.nextInt() - 1;
            }
            else {
                nodes[i].next0 = -1;
                nodes[i].next1 = -1;
            }
        }
        dfs(0, new HashMap<>());
        bfsPrint();
    }

    void dfs (int curN, HashMap<Integer, Boolean> predicateValues) {
        while (predicateValues.containsKey(nodes[curN].predicate) && nodes[curN].next0 >= 0) {
            if (predicateValues.get(nodes[curN].predicate)) {
                nodes[curN] = nodes[nodes[curN].next1];
            }
            else {

                nodes[curN] = nodes[nodes[curN].next0];
            }
        }
        if (nodes[curN].next0 >= 0) {
            HashMap<Integer, Boolean> left = new HashMap<>(predicateValues);
            left.put(nodes[curN].predicate, false);
            dfs(nodes[curN].next0, left);
            HashMap<Integer, Boolean> right = new HashMap<>(predicateValues);
            right.put(nodes[curN].predicate, true);
            dfs(nodes[curN].next1, right);
        }
    }

    void bfsPrint() {
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        ArrayList<String> ans = new ArrayList<>();
        queue.add(0);
        int count = 0;
        while(!queue.isEmpty()) {
            int cur = queue.poll();
            if (nodes[cur].next0 >= 0) {
                count++;
                ans.add("choice " + nodes[cur].predicate + " " + (cur + count + 1) + " " + (cur + count + 2));
                queue.add(nodes[cur].next0);
                queue.add(nodes[cur].next1);
            }
            else {
                count--;
                ans.add("leaf " + nodes[cur].predicate);
            }
        }
        out.println(ans.size());
        for (String s : ans) {
            out.println(s);
        }
    }



    class Node {
        int predicate;
        int next0, next1;
    }
}