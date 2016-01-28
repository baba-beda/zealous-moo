import java.io.*;
import java.util.*;

public class G {
    final int actions = 3;
    final int populationSize = 100;
    final int part = 5;
    final int iterationsToSmall = 1000;
    final int iterationsToBig = 2000;
    FastScanner in;
    PrintWriter out;
    Random rand = new Random();
    int states;
    int steps;
    boolean[][] desk;
    int m;
    int applesToEat;

    public static void main(String[] args) {
        new G().run();
    }

    void run() {
        try {
            in = new FastScanner(new File("artificial" + ".in"));
            out = new PrintWriter(new File("artificial" + ".out"));
            solve();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void solve() throws FileNotFoundException {
        PrintWriter code = new PrintWriter(new File("code.out"));
        for (int test = 5; test < 6; test++) {
            code.println("case " + (test + 1) + ":\n");
            m = in.nextInt();
            states = in.nextInt();
            steps = in.nextInt();
            applesToEat = 0;

            desk = new boolean[m][m];
            for (int i = 0; i < m; i++) {
                char[] str = in.next().toCharArray();
                for (int j = 0; j < m; j++) {
                    switch (str[j]) {
                        case '*':
                            desk[i][j] = true;
                            applesToEat++;
                            break;
                        default:
                            break;
                    }
                }
            }

            Automata[] next = new Automata[populationSize];
            for (int i = 0; i < populationSize; i++) {
                next[i] = new Automata(states);
                fillRandomly(next[i]);
            }
            Arrays.sort(next, (o1, o2) -> Double.compare(adaptationFunction(o2, false), adaptationFunction(o1, false)));
            int iteration = 1;
            int equalIterations = 1;
            int lastEaten = -1;
            int sinceLastSmall = 0;
            int sinceLastBig = 0;

            while (next[0].eatenApples != applesToEat) {
                sinceLastBig++;
                sinceLastSmall++;
                if (next[0].eatenApples <= lastEaten) {
                    equalIterations++;
                } else {
                    equalIterations = 1;
                }
                lastEaten = next[0].eatenApples;

                if (equalIterations >= iterationsToBig && sinceLastBig >= 500) {
                    bigMutation(next);
                    System.out.println("big mutation occurred");
                    sinceLastBig = 0;


                }
                if (equalIterations >= iterationsToSmall && sinceLastSmall >= 100) {
                    smallMutation(next);
                    System.out.println("small mutation occurred");
                    sinceLastSmall = 0;

                }
                System.out.println((test + 1) + ":" + (iteration++) + " " + next[0].eatenApples + "/" + applesToEat + " " + next[0].lastEatenStep + "/" + steps + " " +  String.format("%f2", adaptationFunction(next[0], false)));

                next = nextPopulation(next);
            }

            renumerate(next[0]);
            adaptationFunction(next[0], true);

            String answer = "";
            for (int i = 0; i < states; i++) {
                int to0 = next[0].transfers[i][0] + 1;
                int to1 = next[0].transfers[i][1] + 1;
                char ac0 = next[0].outs[i][0] == 1 ? 'L' : (next[0].outs[i][0] == 0 ? 'M' : 'R');
                char ac1 = next[0].outs[i][1] == 1 ? 'L' : (next[0].outs[i][1] == 0 ? 'M' : 'R');
                System.out.println(to0 + " " + to1 + " " + ac0 + " " + ac1);
                answer += (to0 + " " + to1 + " " + ac0 + " " + ac1) + "\\n";
            }
            code.println("out.println(" + "\"" + answer + "\"" + ");\nbreak;\n");
        }
        code.close();
    }

    void renumerate(Automata auto) {
        if (auto.start == 0) {
            return;
        }
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(auto.start, 0);
        ArrayDeque<Integer> statesToChange = new ArrayDeque<>();
        statesToChange.push(auto.start);
        int[][] transfers = new int[states][2];
        int[][] outs = new int[states][2];
        while (!statesToChange.isEmpty()) {
            int curState = statesToChange.poll();
            int to0 = auto.transfers[curState][0];
            int to1 = auto.transfers[curState][1];
            if (!map.containsKey(to0)) {
                map.put(to0, map.size());
                statesToChange.push(to0);
            }
            if (!map.containsKey(to1)) {
                map.put(to1, map.size());
                statesToChange.push(to1);
            }
        }
        for (int i = 0; i < states; i++) {
            int to0 = auto.transfers[i][0];
            int to1 = auto.transfers[i][1];
            transfers[map.get(i)][0] = map.get(to0);
            transfers[map.get(i)][1] = map.get(to1);
            outs[map.get(i)] = auto.outs[i];
        }
        auto.transfers = transfers;
        auto.outs = outs;
        auto.start = 0;
    }

    void smallMutation(Automata[] population) {
        for (int i = populationSize / 10; i < populationSize; i++) {
            population[i] = mutation(population[i]);
        }
        Arrays.sort(population, (o1, o2) -> Double.compare(adaptationFunction(o2, false), adaptationFunction(o1, false)));
    }

    void bigMutation(Automata[] population) {
        for (int i = 0; i < populationSize; i++) {
            int fate = Math.abs(rand.nextInt()) % 2; // 0 - mutation, 1 - new random
            switch (fate) {
                case 0:
                    population[i] = mutation(population[i]);
                    break;
                case 1:
                    population[i] = new Automata(states);
                    fillRandomly(population[i]);
                    break;
            }
        }
        Arrays.sort(population, (o1, o2) -> Double.compare(adaptationFunction(o2, false), adaptationFunction(o1, false)));
    }

    Automata[] nextPopulation(Automata[] population) {
        Automata[] next = new Automata[populationSize];
        System.arraycopy(population, 0, next, 0, populationSize / part);
        int curNextSize = populationSize / part;
        while (curNextSize != populationSize) {
            int firstParent = rand.nextInt(populationSize / part);
            int secondParent = rand.nextInt(populationSize / part);
            Automata[] children = new Automata[2];
            Automata[] parents = new Automata[2];
            parents[0] = population[firstParent];
            parents[1] = population[secondParent];

            int action = parents[0].equals(parents[1]) ? 0 : Math.abs(rand.nextInt()) % 2;

            switch (action) {
                case 1:
                    children = crossover(parents);
                    break;
                case 0:
                    children[0] = mutation(parents[0]);
                    children[1] = mutation(parents[1]);
                    break;
            }
            next[curNextSize++] = children[0];
            next[curNextSize++] = children[1];
        }
        Arrays.sort(next, (o1, o2) -> Double.compare(adaptationFunction(o2, false), adaptationFunction(o1, false)));

        return next;
    }

    Automata[] crossover(Automata[] parents) {
        Automata[] children = new Automata[2];
        int crossoverType = 0; // 0 - random, 1 - one-point
        switch (crossoverType) {
            case 0:
                children = randomCrossover(parents);
                break;
            case 1:
                children = onePointCrossover(parents);
                break;
        }
        return children;
    }

    Automata[] onePointCrossover(Automata[] parents) {
        int point = rand.nextInt(states - 1) + 1;
        Automata[] children = new Automata[2];
        children[0] = new Automata(states);
        children[1] = new Automata(states);
        for (int i = 0; i < point; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    children[j].transfers[i][k] = parents[j].transfers[i][k];
                    children[j].outs[i][k] = parents[j].transfers[i][k];
                }
            }
        }
        for (int i = point; i < states; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    children[j].transfers[i][k] = parents[1 - j].transfers[i][k];
                    children[j].outs[i][k] = parents[1 - j].transfers[i][k];
                }
            }
        }
        return children;
    }

    Automata[] randomCrossover(Automata[] parents) {
        Automata[] children = new Automata[2];
        children[0] = new Automata(states);
        children[1] = new Automata(states);
        int starts = Math.abs(rand.nextInt()) % 2;
        children[0].start = parents[starts].start;
        children[1].start = parents[1 - starts].start;

        for (int i = 0; i < states; i++) {
            int zeroTransfer = Math.abs(rand.nextInt()) % 2;
            int oneTransfer = Math.abs(rand.nextInt()) % 2;
            children[0].transfers[i][0] = parents[zeroTransfer].transfers[i][0];
            children[1].transfers[i][0] = parents[1 - zeroTransfer].transfers[i][0];
            children[0].transfers[i][1] = parents[oneTransfer].transfers[i][1];
            children[1].transfers[i][1] = parents[1 - oneTransfer].transfers[i][1];

            children[0].outs[i][0] = parents[zeroTransfer].outs[i][0];
            children[1].outs[i][0] = parents[1 - zeroTransfer].outs[i][0];
            children[0].outs[i][1] = parents[oneTransfer].outs[i][1];
            children[1].outs[i][1] = parents[1 - oneTransfer].outs[i][1];
        }
        return children;
    }

    Automata mutation(Automata parent) {
        Automata result = new Automata(states);
        int mutationType = states > 1 ? Math.abs(rand.nextInt()) % 5 : Math.abs(rand.nextInt()) % 2; // 0 - make new random, 1 - change action in transfer, 2 - change start, 3 - change to-state of transfer
        switch (mutationType) {
            case 0:
                fillRandomly(result);
                break;
            case 1:
                result = changeAction(parent);
                break;
            case 2:
                result = changeToState(parent, false);
                break;
            case 3:
                result = changeAutomataStart(parent);
                break;
            case 4:
                result = changeToState(parent, true);
                break;
        }
        return result;
    }

    Automata changeAutomataStart(Automata automata) {
        int otherStart = rand.nextInt(states);
        while (otherStart == automata.start && states > 1) {
            otherStart = Math.abs(rand.nextInt()) % states;
        }
        Automata result = new Automata(automata);
        result.start = otherStart;
        return result;
    }

    Automata changeAction(Automata automata) {
        int state = Math.abs(rand.nextInt()) % states;
        int action = Math.abs(rand.nextInt()) % actions - 1;
        while (action == automata.outs[state][0]) {
            state = Math.abs(rand.nextInt()) % states;
            action = Math.abs(rand.nextInt()) % actions - 1;
        }
        Automata result = new Automata(automata);
        result.outs[state][0] = action;
        return result;
    }

    Automata changeToState(Automata automata, boolean start) {
        int state = start ? automata.start : Math.abs(rand.nextInt()) % states;
        int to = Math.abs(rand.nextInt()) % states;
        int condition = Math.abs(rand.nextInt()) % 2;
        while (to == automata.transfers[state][condition] && states > 1) {
            state = start ? automata.start : Math.abs(rand.nextInt()) % states;
            to = Math.abs(rand.nextInt()) % states;
            condition = Math.abs(rand.nextInt()) % 2;
        }
        Automata result = new Automata(automata);
        result.transfers[state][condition] = to;
        return result;
    }

    void fillRandomly(Automata auto) {
        for (int i = 0; i < auto.states; i++) {
            auto.transfers[i][0] = Math.abs(rand.nextInt()) % states;
            auto.transfers[i][1] = Math.abs(rand.nextInt()) % states;
            auto.outs[i][0] = Math.abs(rand.nextInt()) % actions - 1;
        }
        auto.start = Math.abs(rand.nextInt()) % states;
    }

    double adaptationFunction(Automata auto, boolean show) {
        int curState = auto.start;
        int curPosX = 0;
        int curPosY = 0;
        int curDirection = 0; // 0 - right, 1 - up, 2 - left, 3 - down
        int eaten = 0;
        int lastFood = -1;
        boolean[][] deskCopy = new boolean[m][m];
        int[][] xystep = new int[steps + 2][2];
        for (int i = 0; i < m; i++) {
            System.arraycopy(desk[i], 0, deskCopy[i], 0, m);
        }
        for (int i = 0; i <= steps; i++) {
            xystep[i][0] = curPosX;
            xystep[i][1] = curPosY;
            if (deskCopy[curPosX][curPosY]) {
                eaten++;
                lastFood = i;
                deskCopy[curPosX][curPosY] = false;
            }
            if (eaten == applesToEat) {
                break;
            }
            int nextX = (curPosX + (curDirection % 2) * (curDirection == 1 ? -1 : 1)) % m;
            int nextY = (curPosY + ((curDirection + 1) % 2) * (curDirection == 0 ? 1 : -1)) % m;
            nextX = nextX == -1 ? m - 1 : nextX;
            nextY = nextY == -1 ? m - 1 : nextY;
            boolean isFood = deskCopy[nextX][nextY];

            boolean forward = auto.outs[curState][isFood ? 1 : 0] == 0;

            curDirection += auto.outs[curState][isFood ? 1 : 0];
            curDirection = (curDirection + 4) % 4;
            curState = auto.transfers[curState][isFood ? 1 : 0];

            if (forward) {
                curPosX = nextX;
                curPosY = nextY;
            }
        }
        auto.eatenApples = eaten;
        auto.lastEatenStep = lastFood;
        if (show) {
            System.out.println(lastFood + "/" + steps);
            char[][] path = new char[m][m];
            for (int i = 0; i < m; i++) {
                Arrays.fill(path[i], 'O');
            }
            for (int i = 0; i < steps; i++) {
                path[xystep[i][0]][xystep[i][1]] = 'X';
            }

            for (int i = 0; i < m; i++) {
                System.out.print(path[i]);
                System.out.print(" ");
                for (int j = 0; j < m; j++) {
                    System.out.print(desk[i][j] ? '*' : '.');
                }
                System.out.println();
            }
        }
        return (eaten - (double) (lastFood) / steps);
    }

    class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        FastScanner(File f) {
            try {
                br = new BufferedReader(new FileReader(f));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }

    class Automata {
        int[][] transfers;
        int[][] outs;
        int states;
        int start;
        int eatenApples;
        int lastEatenStep;

        public Automata(int states) {
            this.start = 0;
            this.states = states;
            transfers = new int[states][2];
            outs = new int[states][2]; // 0 - move forward, 1 - left, -1 - right
        }

        public Automata(Automata automata) {
            this.start = automata.start;
            this.states = automata.states;
            this.transfers = new int[states][2];
            this.outs = new int[states][2];
            for (int i = 0; i < states; i++) {
                System.arraycopy(automata.transfers[i], 0, transfers[i], 0, 2);
                System.arraycopy(automata.outs[i], 0, outs[i], 0, 2);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Automata automata = (Automata) o;

            if (states != automata.states) return false;
            if (start != automata.start) return false;
            if (!Arrays.deepEquals(transfers, automata.transfers)) return false;
            return Arrays.deepEquals(outs, automata.outs);

        }

        @Override
        public int hashCode() {
            int result = Arrays.deepHashCode(transfers);
            result = 31 * result + Arrays.deepHashCode(outs);
            result = 31 * result + states;
            result = 31 * result + start;
            return result;
        }
    }
}