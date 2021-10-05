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
            int[] possibleSolution = { remainingRows[0]};
			partialSolution = add(partialSolution, possibleSolution);
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
        } 
        Arrays.sort(columnsMinCalc);

        //find the minimum of 1s inside the columns array
        int min = columnsMinCalc[0];
        
            
        if(min == 0){
            return new int[0];
        }

        //find the first column containing the minimum of 1's out of af all columns of the matrix deterministically
        int selectedColumn = 0;
        int counter;
        
        for(int c = 1; c < matrix[0].length; c++){
            counter = 0;
            for(int r = 0; r < matrix.length; r++){
               if(matrix[r][c] == 1){
                counter++;
               }
            }
            if(counter == min){
                selectedColumn = c;
                break;
            }
        }

            
        //STEP 3
        //create an array with the length of all possible rows containing a 1 in the selected column 
        int[] relevantRows = new int[min];
        //traverse the selected column for all the rows containing 1s and store the row inside the array
        int helper = 0;
        for(int i = 0; i < matrix.length; i++){
            if(matrix[i][selectedColumn] == 1){
                relevantRows[helper] = i;
                helper++;
            }
        }

        //System.out.println(Arrays.toString(relevantRows));


        //for each relevant row we check wether it results in exact Cover
        for(int i = 0; i < relevantRows.length; i++){
            
            int[] branch = { matrix[relevantRows[i]][0] };
            partialSolution = add(partialSolution, branch);

            //System.out.println(Arrays.toString(partialSolution));

                  
                
            //find out the columns that are to be deleted from the matrix and store them in an array
            Set<Integer> columnsDelete = new HashSet<Integer> ();
            for(int j= 1; j < matrix[0].length; j++){
                if(matrix[relevantRows[i]][j] == 1){
                columnsDelete.add(j);
                }
            }
            int[]columns2Bremoved = toInt(columnsDelete);
            
            //System.out.println(Arrays.toString(columns2Bremoved));
                
            //find out the rows to be deleted from the matrix and store them in an array
            Set<Integer> rowsDelete = new HashSet<Integer> ();
            for(int j = 0; j < columns2Bremoved.length; j++){
                for(int k = 0; k < matrix.length; k++){
                    if(matrix[k][columns2Bremoved[j]] == 1){
                        rowsDelete.add(k);
                    }
                }
            }
            int[]rows2Bremoved = toInt(rowsDelete);
            //System.out.println(Arrays.toString(rows2Bremoved));
           
            int[][] newMatrix = matrix.clone();
            //create new matrix that is reduced by the rows that are to be deleted
            int[][]newMatrix1 = remove_row(newMatrix, rows2Bremoved);

            int[][]newMatrixFinal = remove_col(newMatrix1, columns2Bremoved);

            /*
            for(int k = 0; k < newMatrixFinal.length; k++){
                for(int l = 0; l < newMatrixFinal[k].length; l++){
                    System.out.print(newMatrixFinal[k][l] + " ");
                }
                System.out.println();

            }
            */
            
                
            int[] newPartialSolution = exactCover(newMatrixFinal);
            //System.out.println(Arrays.toString(newPartialSolution));

            if (newPartialSolution.length != 0) {
				partialSolution = add(partialSolution, newPartialSolution);
				return partialSolution;
			}

            // remove row r we added earlier
			partialSolution = remove_last_element(partialSolution);
                
           
                
            
        }
        return new int[0];
    }

    public static int[] toInt(Set<Integer> set) {
        int[] a = new int[set.size()];
        int i = 0;
        for (Integer val : set) a[i++] = val;
        return a;
      }

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

	public static int[][] remove_row(int in[][], int rows[]) {
		int[][] out = in.clone();

		Arrays.sort(rows);
		for (int i = 0; i < rows.length; i++) {
			out = remove_row(out, rows[i] - i);
		}

		return out;
	}

	public static int[][] remove_col(int in[][], int column) {
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

	public static int[][] remove_col(int in[][], int columns[]) {
		int[][] out = in.clone();

		Arrays.sort(columns);
		for (int i = 0; i < columns.length; i++) {
			out = remove_col(out, columns[i] - i);
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
			for (int j = 1; j < matrix[0].length; j++) {
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
            { 2 , 0 , 1 , 1 , 1 },
            { 3 , 0 , 1 , 1 , 0 },
            { 4 , 1 , 1 , 0 , 1 }
        };
        
        int[] answers = exactCover(matrix);
        
        
        for(int i = 0; i < answers.length; i++){
            System.out.print(answers[i] + " ");
        }
        System.out.println();
    }
}