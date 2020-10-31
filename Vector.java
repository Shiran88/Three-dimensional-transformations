//Shiran Golbar, 313196974
//Lev Levin, 342480456
import java.util.Arrays;

/**
 * This class represents a vector.
 */
public class Vector {
    protected double[] vector;


    public Vector(double[] vector) {
        this.vector = vector.clone();
    }

    public int getNumOfElements() {
        return vector.length;
    }

    /**
     * This function calculates the angle between two vectors.

     * @param vec1
     * @param vec2
     * @return
     */
    public static double calculateAngleBetweenVectors(Vector vec1, Vector vec2) {
        double dProduct = vec1.dotProduct(vec2);
        double magProduct = vec1.getMagnitude() * vec2.getMagnitude();
        double cosAngle = dProduct / magProduct;
        //calculate and return arccosine of cosine.
        return Math.acos(cosAngle);
    }
    public Vector normalize() {
        double magnitude = this.getMagnitude();
        double[] newArr = new double[this.vector.length];
        for (int i = 0; i < this.vector.length; i++) {
            newArr[i] = this.vector[i] / magnitude;
        }
        return  new Vector(newArr);
    }
    public double[] getVectorAsArray() {
        return vector.clone();
    }
    public void setElement(double value, int index) {
        this.vector[index] = value;
    }
    public double getElement(int index) {
        return this.vector[index];
    }
    public double getMagnitude() {
        double dotProd = this.dotProduct(this);
        return Math.sqrt(dotProd);
    }
    public double dotProduct(Vector other) {

        if (other.getVectorAsArray().length != this.getVectorAsArray().length) {
            throw new IllegalArgumentException("vectors must be the same length!");
        }
        double sum = 0;
        for (int i = 0; i < this.getVectorAsArray().length; ++i) {
            sum += this.getVectorAsArray()[i] * other.getVectorAsArray()[i];
        }
        return sum;
    }
    public Vector crossProduct3d(Vector other) {
        if (this.vector.length != 3 || other.vector.length !=3) {
            throw new IllegalArgumentException("both vectors must be of size 3!");
        }
        int x = 0;
        int y = 1;
        int z = 2;
        double[] arr = new double[3];
        arr[x] = this.vector[y] * other.vector[z] - this.vector[z] * other.vector[y];
        arr[y] = this.vector[z] * other.vector[x] - this.vector[x] * other.vector[z];
        arr[z] = this.vector[x] * other.vector[y] - this.vector[y] * other.vector[x];
        return new Vector(arr);
    }
    public Vector add(Vector other) {
        if (this.vector.length != other.vector.length){
            throw new IllegalArgumentException("vectors mush be the same length!");
        }
        double arr[] = new double[this.vector.length];
        for (int i = 0; i < this.vector.length; i++) {
            arr[i] = this.vector[i] + other.vector[i];
        }
        return new Vector(arr);
    }

    public Vector subtract(Vector other){
        if (this.vector.length != other.vector.length){
            throw new IllegalArgumentException("vectors mush be the same length!");
        }
        double arr[] = new double[this.vector.length];
        for (int i = 0; i < this.vector.length; i++) {
            arr[i] = this.vector[i] - other.vector[i];
        }
        return new Vector(arr);
    }

    @Override
    public String toString() {
        return Arrays.toString(this.getVectorAsArray());

    }













}
