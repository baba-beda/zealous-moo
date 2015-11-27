import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by  baba_beda on 9/22/15.
 */
public class Hull {
    public static void main(String[] args) {
        new Hull().run();
    }

    Scanner in;
    PrintWriter out;

    void run() {
        try {
            File defaultInput = new File("hull.in");
            if (defaultInput.exists()) {
                in = new Scanner(new FileInputStream(defaultInput));
                out = new PrintWriter(new File("hull.out"));
            }
            else {
                in = new Scanner(System.in);
                out = new PrintWriter(System.out);
            }
            solve();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    Point p0;
    void solve() {
        int n = in.nextInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new Point(in.nextLong(), in.nextLong());
        }
        Arrays.sort(points, new XYComparator());
        p0 = points[0];
        ArrayList<Point> rightPoints = new ArrayList<>();
        for (int i = 1; i < n; i++) {
            rightPoints.add(points[i]);
        }
        Collections.sort(rightPoints, new PolarComparator());
        rightPoints.add(p0);
        Arrays.sort(points, new PolarComparator());
        Stack<Point> stack = new Stack<>();
        stack.push(p0);
        stack.push(rightPoints.get(0));

        for (int i = 1; i < n; i++) {
            while (stack.size() >= 2 && orientation(nextToTop(stack), top(stack), rightPoints.get(i)) != -1) {
                stack.pop();
            }
            if (stack.size() >= 2 && orientation(nextToTop(stack), top(stack), rightPoints.get(i)) == 0) {
                stack.pop();
            }
            stack.push(rightPoints.get(i));
        }
        stack.pop();
        out.println(stack.size());
        while (!stack.isEmpty()) {
            Point p = stack.pop();
            out.println(p.x + " " + p.y);
        }

    }

    Point top(Stack<Point> stack) {
        return stack.peek();
    }

    Point nextToTop(Stack<Point> stack) {
        return stack.get(stack.size() - 2);
    }

    class PolarComparator implements Comparator<Point> {
        @Override
        public int compare(Point o1, Point o2) {
            int or = orientation(p0, o1, o2);
            if (or != 0) {
                return or;
            }
            long r1 = (p0.x - o1.x) * (p0.x - o1.x) + (p0.y - o1.y) * (p0.y - o1.y); // distance
            long r2 = (p0.x - o2.x) * (p0.x - o2.x) + (p0.y - o2.y) * (p0.y - o2.y);
            Point auxPoint = new Point(p0.x + 1, p0.y);
            if (orientation(p0, auxPoint, o1) >= 0) {
                return Long.compare(r1, r2);
            }
            return Long.compare(r2, r1);
        }
    }

    class XYComparator implements Comparator<Point> {
        @Override
        public int compare(Point o1, Point o2) {
            if (o1.x != o2.x) {
                return Long.compare(o1.x, o2.x);
            }
            return Long.compare(o1.y, o2.y);
        }
    }
    
    class Point {
        long x, y;

        public Point(long x, long y) {
            this.x = x;
            this.y = y;
        }
    }
    
    int orientation(Point a, Point b, Point c) {
        long v = (c.x - a.x) * (b.y - a.y) - (c.y - a.y) * (b.x - a.x);
        if (v < 0) {
            return -1; //left rotation
        }
        if (v > 0) {
            return 1; //right rotation
        }
        return 0;
    }
}