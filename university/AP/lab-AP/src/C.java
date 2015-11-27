import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by  baba_beda on 11/18/15.
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

    void solve() {
        int n = in.nextInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            String type = in.next();
            nodes[i].predicate = in.nextInt();
            if (type.equals("choice")) {
                nodes[i].next0 = in.nextInt() - 1;
                nodes[i].next1 = in.nextInt() - 1;
            }
        }
        dfs(nodes, 0, new HashMap<>());
        bfsPrint(nodes);
    }

    void dfs(Node[] nodes, int cur, HashMap<Integer, Boolean> curEst) {
        while (curEst.containsKey(nodes[cur].predicate) && nodes[cur].next0 >= 0) {
            if (curEst.get(nodes[cur].predicate)) {
                nodes[cur] = nodes[nodes[cur].next1];
            } else {
                nodes[cur] = nodes[nodes[cur].next0];
            }
        }
        if (nodes[cur].next0 >= 0) {

            curEst.put(nodes[cur].predicate, false);
            dfs(nodes, nodes[cur].next0, curEst);
            curEst.remove(nodes[cur].predicate);
            curEst.put(nodes[cur].predicate, true);
            dfs(nodes, nodes[cur].next1, curEst);
            curEst.remove(nodes[cur].predicate);
        }
    }

    void bfsPrint(Node[] nodes) {
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        ArrayList<String> ans = new ArrayList<>();
        int count = 0;
        queue.add(0);
        int i = 0;
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            if (nodes[cur].next0 >= 0) {
                count++;
                ans.add("choice " + nodes[cur].predicate + " " + (i + count + 1) + " " + (i + count + 2));
                queue.add(nodes[cur].next0);
                queue.add(nodes[cur].next1);
            }
            else {
                count--;
                ans.add("leaf " + nodes[cur].predicate);
            }
            i++;
        }
        out.println(ans.size());
        for (String s : ans) {
            out.println(s);
        }
    }

    class Node {
        int predicate;
        int next0, next1;
        Node() {
            predicate = 0;
            next0 = -1;
            next1 = -1;
        }
    }
}