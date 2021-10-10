package AlgorithmX;

import java.util.Arrays;

public class AlgorithmX {
    public static void main(String[] args) {
        int[][] input = {
            { 0 , 1 , 1 , 0 },
            { 1 , 1 , 1 , 0 },
            { 1 , 0 , 0 , 1 },
            { 0 , 0 , 1 , 1 },
        };
        
        int[][] data = AlgoUtils.prepare_matrix(input);
        int[] answers = algo_x(data);
        System.out.println(Arrays.toString(answers));
    }

    public static int[]algo_x(int[][] matrix){
        int[] partialSolution = new int[0];

        //BASE CASE: If the matrix is empty the current partial solutin is valid
        if(matrix[0].length == 1){
            return new int[0];
        }

        //Select the first colum containing the least amount of 1's
        int minColumn = AlgoUtils.get_min_column(matrix);

        //If any column contains no 1's at all the branch gets terminated
        if(minColumn == -1){
            int[] terminateBranch = {-666};
            return terminateBranch;
        }
        
        for(int r = 1; r < matrix.length; r++){
            //A row is selected such that matrix[r][selectedColumn] == 1
            if(matrix[r][minColumn] == 1){
                //The current row get's added to the partial solution
                partialSolution = ArrayUtils.add_element(partialSolution, matrix[r][0]);
                
                //The rows and columns that have to be deleted get stored in seperate arrays
                int[]columns2delete = AlgoUtils.get_relevant_columns(matrix[r]);
                int[]rows2delete = AlgoUtils.get_relevant_rows(matrix, columns2delete);

                //The reduced matrix is created
                int[][] newMatrix = matrix.clone();
                newMatrix = ArrayUtils.remove_row(newMatrix, rows2delete);
                newMatrix = ArrayUtils.remove_col(newMatrix, columns2delete);

                //Recursive call on the reduced matrix
                int[] newPartialSolution = algo_x(newMatrix);
                
                //Add the new partial solution to the current one if successful
                if(newPartialSolution.length == 0){
                    return partialSolution;
                }
                if(newPartialSolution[0] != -666){
                    partialSolution = ArrayUtils.add(partialSolution, newPartialSolution);
                    return partialSolution;
                }

                //Remove current row from partial solution and move on to next row if unsuccessful
                partialSolution = ArrayUtils.remove_last_element(partialSolution);
            }
        }
        return new int[0];
    }
}
