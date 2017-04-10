/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solution;

/**
 *
 * @author mafe
 */
import java.io.*;
import java.util.*;

public class Solution {
    long[][][] tree;
    long[][][] nums;
    private int dimensions = 0;
    
    public Solution(int dimensions) {
        if (dimensions == 0) return;
        this.dimensions = dimensions;
        tree = new long[dimensions+1][dimensions+1][dimensions+1];
        nums = new long[dimensions][dimensions][dimensions];
    }
    
    public void update(int x, int y, int z, int value) {
        long delta = value - nums[x][y][z];
        nums[x][y][z] = value;
        for (int i = x + 1; i <= dimensions; i += i++) {
            for (int j = y + 1; j <= dimensions; j += j ++) {
                for (int k = z + 1; k <= dimensions; k += k++) {
                    tree[i][j][k] +=  delta;
                }
            }
        }
    }
    
    public void query(int x1, int y1, int z1, int x2, int y2, int z2) {
        long result = sum(x2+1,y2+1,z2+1) - sum(x1,y1,z1) - sum(x1,y2+1,z2+1) - sum(x2+1,y1,z2+1) - sum(x2+1,y2+1,z1) + sum(x1,y1,z2+1) + sum(x1,y2+1,z1) + sum(x2+1,y1,z1);
        System.out.println(result);
    }
    
    public long sum(int x, int y, int z) {
        long sum = 0l;
        for (int i = x; i > 0; i -= i & (-i)) {
            for (int j = y; j > 0; j -= j & (-j)) {
                for (int k = z; k > 0; k -= k & (-k)) {
                    sum += tree[i][j][k];
                }
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner ob = new Scanner(System.in);
  Solution solution = null;
        int testcases = ob.nextInt();
        ob.nextLine();
        for (int i = 0; i < testcases; i++) {
            String numsLine = ob.nextLine();
            String[] numsLineParts = numsLine.trim().split(" ");
            int dimensions = Integer.valueOf(numsLineParts[0]);
            int numOperations = Integer.valueOf(numsLineParts[1]);
            solution = new Solution(dimensions);
            for (int j = 0; j < numOperations; j++) {
                String line = ob.nextLine();
                String[] lineParts = line.split(" ");
                if (lineParts[0].equals("UPDATE")) {
                    solution.update(Integer.valueOf(lineParts[1])-1, Integer.valueOf(lineParts[2])-1,             Integer.valueOf(lineParts[3])-1, Integer.valueOf(lineParts[4]));
                }
                if (lineParts[0].equals("QUERY")) {
                    solution.query(Integer.valueOf(lineParts[1])-1, Integer.valueOf(lineParts[2])-1,             Integer.valueOf(lineParts[3])-1, Integer.valueOf(lineParts[4])-1, Integer.valueOf(lineParts[5])-1, Integer.valueOf(lineParts[6])-1);
                }
            }
        }
    }
}
