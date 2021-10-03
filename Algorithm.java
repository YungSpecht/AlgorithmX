import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.ArrayList;

public class Algorithm {

    public static boolean exactCover(int[][] matrix){
        
        //STEP 1:
        //if the matrix is empty terminate the function successfully
        if(matrix[0].length == 1){
            
            return true;
            //terminate successfully ?
        }
        else{
            //STEP 2
            //open an array the length of the amount of columns and store the amount of 1s in each column
            Integer[] columnsMinCalc = new Integer[matrix[0].length - 1];
            //traverse the matrix and find the amount of 1s in each column
            for(int c = 1; c < matrix[0].length; c++){
                int count = 0;
                for(int r = 1; r < matrix.length; r++){
                    if(matrix[r][c] == 1){
                        count++;
                    }
                    columnsMinCalc[c - 1]= count;
                }
            }

            //find the minimum of 1s inside the columns array
            int min = Collections.min(Arrays.asList(columnsMinCalc));
            
            if(min == 0){
                
                return false;
            }

            //find the first column containing the minimum of 1's out of af all columns of the matrix deterministically
            int selectedColumn = 0;
            for(int i = 0; i < columnsMinCalc.length; i++){
                if(columnsMinCalc[i] == min){
                    selectedColumn = i + 1;
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
                
                int row = relevantRows[i];
                
                //find out the columns that are to be deleted from the matrix and store them in an array
                Set<Integer> columnsDelete = new HashSet<Integer> ();
                for(int j= 1; i < matrix[row].length; j++){
                    if(matrix[row][i] == 1){
                    columnsDelete.add(i);
                    }
                }
                Integer[] columns2Bremoved = columnsDelete.toArray(new Integer[0]);
                
                //find out the rows to be deleted from the matrix and store them in an array
                Set<Integer> rowsDelete = new HashSet<Integer> ();
                for(int j = 0; j < columns2Bremoved.length; j++){
                    for(int k = 1; k < matrix.length; k++){
                        if(matrix[j][columns2Bremoved[i]] == 1){
                        rowsDelete.add(j);
                        }
                    }
                }
                Integer[] rows2Bremoved = rowsDelete.toArray(new Integer[0]);
           
                //create new matrix that is removed by the rows that are to be deleted
                int[][] newMatrix1 = remove_row(matrix, rows2Bremoved[0]);
                for(int j = 1; j < rows2Bremoved.length; j++){
                    newMatrix1 = remove_row(newMatrix1, rows2Bremoved[j]);
                }
                
                //create a new matrix that is reduced by the rows and columns that are to be deleted
                int[][] newMatrixFinal = remove_column(newMatrix1, columns2Bremoved[0]);
                for(int j = 1; j < columns2Bremoved.length; j++){
                    newMatrixFinal = remove_column(newMatrixFinal, columns2Bremoved[i]);
                }
           
           
                if(exactCover(newMatrixFinal)){
                    System.out.print(matrix[relevantRows[i]][0]);
                }
            
            
            
            
            }
        
                
                
        }
        return false;
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
    
    public static void main(String[] args) {
        int[][] matrix = {
            { 0 , 1 , 2 , 3 , 4 },
            { 1 , 1 , 0 , 0 , 1 },
            { 2 , 1 , 1 , 1 , 0 },
            { 3 , 0 , 1 , 1 , 0 },
            { 4 , 0 , 0 , 1 , 1 }
        };

        if(exactCover(matrix)){
            System.out.print("Exactcover is provided by: ");
        }
        else{
            System.out.println("No exact cover possible");
        }
        
    }
}
