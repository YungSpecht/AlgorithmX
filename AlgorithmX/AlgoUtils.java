package AlgorithmX;

import java.util.Arrays;

public class AlgoUtils {
    
    public static int get_min_column(int[][] matrix){
        
        int[] columnsMinCalc = new int[matrix[0].length - 1];

        for(int c = 1; c < matrix[0].length; c++){
            int count = 0;
            for(int r = 1; r < matrix.length; r++){
                if(matrix[r][c] == 1){
                    count++;
                }
            }
            columnsMinCalc[c - 1]= count;
        }

        Arrays.sort(columnsMinCalc);
        if(columnsMinCalc[0] == 0){
            return -1;
        }

        int counter;
        for(int c = 1; c < matrix[0].length; c++){
            counter = 0;
            for(int r = 1; r < matrix.length; r++){
               if(matrix[r][c] == 1){
                counter++;
               }
            }
            if(counter == columnsMinCalc[0]){
                return c;
            }
        }
        return -1;
    }

    public static int[] get_relevant_columns(int[] row) {
        int[] relevantCols = new int[0];
        for(int i = 1; i < row.length; i++){
            if(row[i] == 1){
                relevantCols = ArrayUtils.add_distinct_element(relevantCols, i);
            }
        }
        return relevantCols;
    }

    public static int[] get_relevant_rows(int[][] matrix, int[]cols2check){
        int[]relevantRows = new int[0];
        for(int i = 0; i < cols2check.length; i++){
            for(int r = 1; r < matrix.length; r++){
                if(matrix[r][cols2check[i]] == 1){
                    relevantRows = ArrayUtils.add_distinct_element(relevantRows, r);
                }
            }
        }
        return relevantRows;
    }

    public static int[][] prepare_matrix(int[][] matrix) {
        int[][] columnHeader = new int[1][matrix[0].length + 1];
        for (int i = 0; i < matrix[0].length + 1; i++){
            columnHeader[0][i] = i;
        }
        int[][] newMatrix = new int[matrix.length][0];
		for (int i = 0; i < matrix.length; i++) {
			newMatrix[i] = ArrayUtils.add_element_to_start(matrix[i], i + 1);
		}
		return ArrayUtils.add(columnHeader, newMatrix);
	}
}
