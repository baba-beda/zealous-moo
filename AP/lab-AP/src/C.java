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
        }
        for (int i = 0; i < n; i++) {
            String s = in.next();
            nodes[i].predicate = in.nextInt();
            nodes[i].number = i;
            if (s.equals("choice")) {
                int next0 = in.nextInt() - 1;
                int next1 = in.nextInt() - 1;
                nodes[i].left = nodes[next0];
                nodes[i].right = nodes[next1];
                nodes[i].next0 = next0;
                nodes[i].next1 = next1;
            }
            else {
                nodes[i].isLeaf = true;
            }
        }
        dfs(0, new HashMap<>());
        HashSet<Node> newNodes = new HashSet<>();
        newNodes.addAll(Arrays.asList(nodes).subList(0, n));
        Node[] ans = new Node[newNodes.size()];
        ans[0] = nodes[0];
        int size = 1;
        for (int i = 0; i < newNodes.size(); i++) {
            if (ans[i] != null && !ans[i].isLeaf) {
                ans[size++] = nodes[nodes[ans[i].number].next0];
                ans[i].next0 = size;
                ans[size++] = nodes[nodes[ans[i].number].next1];
                ans[i].next1 = size;
            }
        }
        ArrayList<String> finalAns = new ArrayList<>();
        for (int i = 0; i < newNodes.size(); i++) {
            if (ans[i] != null) {
                String s;
                if (ans[i].isLeaf) {
                    s = "leaf " + ans[i].predicate;
                }
                else {
                    s = "choice " + ans[i].predicate + " " + ans[i].next0 + " " + ans[i].next1;
                }
                finalAns.add(s);
            }
        }
        out.println(finalAns.size());
        for (String s : finalAns) {
            out.println(s);
        }
    }

    void dfs (int curN, HashMap<Integer, Boolean> predicateValues) {
        while (predicateValues.containsKey(nodes[curN].predicate) && !nodes[curN].isLeaf) {
            if (predicateValues.get(nodes[curN].predicate)) {
                nodes[curN] = nodes[curN].right;
            }
            else {
                nodes[curN] = nodes[curN].left;
            }
        }
        if (!nodes[curN].isLeaf) {
            HashMap<Integer, Boolean> left = new HashMap<>(predicateValues);
            left.put(nodes[curN].predicate, false);
            dfs(nodes[curN].next0, left);
            HashMap<Integer, Boolean> right = new HashMap<>(predicateValues);
            right.put(nodes[curN].predicate, true);
            dfs(nodes[curN].next1, right);
        }
    }




    class Node {
        int predicate;
        Node left, right;
        int next0, next1;
        boolean isLeaf;
        int number;
        Node() {

        }
        Node(Node node) {
            this.predicate = node.predicate;
            this.left = node.left;
            this.right = node.right;
            this.isLeaf = node.isLeaf;
            this.number = node.number;
            this.next0 = next0;
            this.next1 = next1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            return predicate == node.predicate && next0 == node.next0 && next1 == node.next1 && isLeaf == node.isLeaf && !(left != null ? !left.equals(node.left) : node.left != null) && !(right != null ? !right.equals(node.right) : node.right != null);

        }

        @Override
        public int hashCode() {
            int result = predicate;
            result = 31 * result + (left != null ? left.hashCode() : 0);
            result = 31 * result + (right != null ? right.hashCode() : 0);
            result = 31 * result + next0;
            result = 31 * result + next1;
            result = 31 * result + (isLeaf ? 1 : 0);
            return result;
        }
    }
}