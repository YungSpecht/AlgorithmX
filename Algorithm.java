import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.ArrayList;

public class Algorithm {

    public static int[] exactCover(int[][] matrix){
        
        int[] partialSolution = new int[0];
        int[] remainingRows = check_if_solved(matrix);

        if (remainingRows.length > 0) {
			// add the remaining rows to the partialSolution
			partialSolution = add(partialSolution, remainingRows);
			return partialSolution; // success!
		}

		if (matrix.length == 0){
			return new int[0]; // failure, abandon branch
        }

    
        //STEP 2
        //open an array the length of the amount of columns and store the amount of 1s in each column
        int[] columnsMinCalc = new int[matrix[0].length - 1];
        //traverse the matrix and find the amount of 1s in each column
        for(int c = 1; c < matrix[0].length; c++){
            int count = 0;
            for(int r = 0; r < matrix.length; r++){
                if(matrix[r][c] == 1){
                    count++;
                }
                columnsMinCalc[c - 1]= count;
            }
            Arrays.sort(columnsMinCalc);

            //find the minimum of 1s inside the columns array
            int min = columnsMinCalc[0];
            
            if(min == 0){
                
                return new int[0];
            }

            //find the first column containing the minimum of 1's out of af all columns of the matrix deterministically
            int selectedColumn = 0;
            for(int i = 0; i < columnsMinCalc.length; i++){
                if(columnsMinCalc[i] == min){
                    selectedColumn = columnsMinCalc[i];
                    break;
                }
            }
            
            //STEP 3
            //create an array with the length of all possible rows containing a 1 in the selected column 
            int[] relevantRows = new int[min];
            int helper = 0;
            //traverse the selected column for all the rows containing 1s and store the row inside the array
            for(int i = 1; i < matrix.length; i++){
                if(matrix[i][selectedColumn] == 1){
                    relevantRows[helper++] = i;
                }
            }

            //for each relevant row we check wether it results in exact Cover
            for(int i = 0; i < relevantRows.length; i++){
                int[] branch = { relevantRows[i] };
                
                partialSolution = add(partialSolution, branch);                
                
                //find out the columns that are to be deleted from the matrix and store them in an array
                Set<Integer> columnsDelete = new HashSet<Integer> ();
                for(int j= 1; i < matrix[relevantRows[i]].length; j++){
                    if(matrix[relevantRows[i]][i] == 1){
                    columnsDelete.add(j);
                    }
                }
                Integer[] columns2Bremoved = columnsDelete.toArray(new Integer[0]);
                
                //find out the rows to be deleted from the matrix and store them in an array
                Set<Integer> rowsDelete = new HashSet<Integer> ();
                for(int j = 0; j < columns2Bremoved.length; j++){
                    for(int k = 0; k < matrix.length; k++){
                        if(matrix[columns2Bremoved[j]][k] == 1){
                        rowsDelete.add(j);
                        }
                    }
                }
                Integer[] rows2Bremoved = rowsDelete.toArray(new Integer[0]);
           
                int[][] newMatrix = matrix.clone();
                //create new matrix that is reduced by the rows that are to be deleted
                for(int j = 0; j < rows2Bremoved.length; j++){
                    newMatrix = remove_row(newMatrix, rows2Bremoved[j]);
                }
                
                //create a new matrix that is reduced by the rows and columns that are to be deleted
                int[][] newMatrixFinal = newMatrix.clone();
                for(int j = 0; j < columns2Bremoved.length; j++){
                    newMatrixFinal = remove_column(newMatrixFinal, columns2Bremoved[j]);
                }
                
                int[] newPartialSolution = exactCover(newMatrixFinal);

                if (newPartialSolution.length != 0) {
					partialSolution = add(partialSolution, newPartialSolution);
					return partialSolution;
				}

                // remove row r we added earlier
				partialSolution = remove_last_element(partialSolution);
                
           
                
            
            }
        
                
                
        }
        return new int[0];
    }

    //method for removing a row of a matrix
    public static int[][] remove_row(int in[][], int row) {
        if (row > in.length - 1 || row < 0)
            return in; // row out of bounds
    
        int[][] out = new int[in.length - 1][];
    
        for (int i = 0; i < row; i++) {
            out[i] = in[i];
        }
        for (int i = row; i < out.length; i++) {
            out[i] = in[i + 1];
        }
    
        return out;
    }
    
    //method for removing a column of a matrix
    public static int[][] remove_column(int in[][], int column) {
        if (column > in[0].length - 1)
            return in; // column out of bounds
    
        int[][] out = new int[in.length][];
    
        for (int i = 0; i < out.length; i++) {
            out[i] = new int[in[0].length - 1];
    
            for (int j = 0; j < column; j++) {
                out[i][j] = in[i][j];
            }
            for (int j = column; j < out[0].length; j++) {
                out[i][j] = in[i][j + 1];
            }
        }
    
        return out;
    }

    public static int[] add(int[] A, int[] B) {
		int[] C = new int[A.length + B.length];

		for (int i = 0; i < A.length; i++) {
			C[i] = A[i];
		}
		for (int i = 0; i < B.length; i++) {
			C[i + A.length] = B[i];
		}

		return C;
	}
    
    private static int[] check_if_solved(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j] == 0)
					return new int[0];
			}
		}

		int[] remainingRows = new int[matrix.length];

		for (int i = 0; i < matrix.length; i++) {
			remainingRows[i] = matrix[i][0];
		}

		return remainingRows;
	}

    public static int[] remove_last_element(int[] array) {
		int[] newArray = new int[array.length - 1];

		for (int i = 0; i < newArray.length; i++) {
			newArray[i] = array[i];
		}
		return newArray;
	}
    
    public static void main(String[] args) {
        int[][] matrix = {
            { 1 , 1 , 0 , 0 , 1 },
            { 2 , 1 , 1 , 1 , 0 },
            { 3 , 0 , 1 , 1 , 0 },
            { 4 , 0 , 0 , 1 , 1 }
        };
        
        int[] answers = exactCover(matrix);

        for(int i = 0; i < answers.length; i++){
            System.out.print(answers[i] + " ");
            System.out.println();
        }
        
    }
}
