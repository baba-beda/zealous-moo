package lab_automata;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by  baba_beda on 9/18/15.
 */
public class Problem4 {
    public static void main(String[] args) {
        new Problem4().run();
    }

    Scanner in;
    PrintWriter out;

    void run() {
        try {
            in = new Scanner(new FileInputStream(new File("problem4" + ".in")));
            out = new PrintWriter(new File("problem4" + ".out"));
            solve();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void solve() {
        int n = in.nextInt();
        int m = in.nextInt();
        int k = in.nextInt();
        int l = in.nextInt();
        HashSet<Integer> terminals = new HashSet<>();
        for (int i = 0; i < k; i++) {
            terminals.add(in.nextInt() - 1);
        }
        long[][] transferMatrix = new long[n][n];
        for (int i = 0; i < m; i++) {
            int from = in.nextInt() - 1;
            int to = in.nextInt() - 1;
            char c = in.next().charAt(0);
            transferMatrix[from][to] += 1;
        }
        long[][] result = matrixToPower(transferMatrix, l);
        long ans = 0;
        for (int t : terminals) {
            ans = (ans + result[0][t]) % MODULO;
        }
        out.print(ans);
    }

    long[][] matrixToPower(long[][] matrix, int power) {
        if (power == 1) {
            return matrix;
        }
        long[][] matrixP = matrixToPower(matrix, power / 2);
        long[][] aux = multiplyMatrices(matrixP, matrixP);
        if (power % 2 == 0) {
            return aux;
        }
        else {
            return multiplyMatrices(aux, matrix);
        }
    }


    long[][] multiplyMatrices(long[][] matrix1, long[][] matrix2) {
        int n = matrix1.length;
        long[][] result = new long[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    result[i][j] = (result[i][j] +  matrix1[i][k] * matrix2[k][j]) % MODULO;
                }
            }
        }
        return result;
    }

    final int MODULO = 1000000007;

}