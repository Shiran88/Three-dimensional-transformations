//Shiran Golbar, 313196974
//Lev Levin, 342480456
import java.util.Arrays;

/**
 * This class represents a matrix(with any number of rows and columns).
 */
public class Matrix {
    private double[][] matrix;

    public double[][] getMatrixAs2dArray() {
        return matrix.clone();
    }

    /**
     * Matrix constructor.
     * @param matrix - 2d array with values for matrix entry, so that matrix[i][j] contains the value
     *               for the matrix entry in the i's row and j's column.
     */
    public Matrix(double[][] matrix) {
        // we do this to make sure that all rows have the same num of elements as it have to be in matrix.
        // otherwise, the exception will be thrown.
        this.matrix = new double[matrix.length][matrix[0].length];
        this.matrix = matrix.clone();
    }

    /**
     * Creates Zero matrix.
     * @param rows num of rows.
     * @param columns num of columns.
     * @return zero matrix.
     */
    public static Matrix createZeroMatrix(int rows, int columns) {
        return new Matrix(new double[rows][columns]);
    }

    /**
     * This function creates Identity Matrix
     * @param size - number of rows and number of columns in the matrix(num of rows == num of columns since
     *             it's identity matrix).
     * @return identity matrix.
     */
    public static Matrix createIdentityMatrix(int size) {
        double[][] arr = new double[size][size];
        for (int i = 0; i < size; i++) {
            arr[i][i] = 1;
        }
        return  new Matrix(arr);
    }
    public Matrix deleteRow(int row) {
        if (row >= this.matrix.length) {
            throw new IllegalArgumentException();
        }
        double[][] newArr = new double[this.matrix.length - 1][this.matrix[0].length];
        int k = 0;
        for (int i = 0; i < this.matrix.length; i++) {
            if (row != i) {
                newArr[k] = this.matrix[i].clone();
                k++;
            }
        }
        return new Matrix(newArr);
    }
    public Matrix deleteColumn(int col) {
        if (col >= this.matrix[0].length) {
            throw new IllegalArgumentException();
        }
        double[][] newArr = new double[this.matrix.length][this.matrix[0].length - 1];
        int k;
        int j;
        for (int i = 0; i < this.matrix.length; i++) {
            for (j = 0, k = 0; j < this.matrix[0].length; j++) {
                if (j != col ) {
                    newArr[i][k] = this.matrix[i][j];
                    k++;
                }
            }
        }
        return new Matrix(newArr);
    }

    /**
     * This function sets entry of matrix with value. That is it does: Matrix[row][column] <- value.
     * @param value
     * @param row
     * @param column
     */
    public void setEntry(double value, int row, int column) {
        this.matrix[row][column] = value;
    }

    /**
     * Returns entry's value of the matrix at row 'row' and column 'column'
     * @param row
     * @param column
     * @return
     */
    public double getEntry(int row, int column) {
        return this.matrix[row][column];
    }

    /**
     * multiplies this by other. the size of result matrix is (this.numRows, other.numCols).
     * @param other
     * @return
     */
    public Matrix multiply(Matrix other) {
        //Pay attention, in java default initialization of elements is with 0.0 in array of type double.
        double[][] newMatrix = new double[matrix.length][other.matrix[0].length];

        // i - iterating over each row in first matrix.
        for (int i = 0; i < matrix.length; i++) {
            // j - iterating over each column in second matrix.
            for (int j = 0; j < other.matrix[0].length; j++) {
                // c1- sub iterating in current row in first matrix over all elements in the row(all columns in this row.)
                int c1 = 0;
                int r2 = 0;
                for (; c1 < matrix[0].length; c1++, r2++) {
                    // r2 - sub iterating in current column in second matrix over all elements in the column
                    // (all rows in this column).
                    newMatrix[i][j] += this.matrix[i][c1] * other.matrix[r2][j];
                }
            }
        }
        return new Matrix(newMatrix);
    }
    public Vector multiply(Vector vec){
        double[] newV = new double[this.matrix.length];
        double[] v = vec.getVectorAsArray();
        for (int i = 0; i < matrix.length; i++) {
            // j - iterating over each column in second matrix.
            for (int j = 0; j < v.length; j++) {
                newV[i] += matrix[i][j] * v[j];
            }
        }
        return new Vector(newV);
    }

    @Override
    public String toString() {
        return Arrays.deepToString(this.matrix);
    }




}
