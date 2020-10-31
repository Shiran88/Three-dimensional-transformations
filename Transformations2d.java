
public class Transformations2d {
    public static Matrix translate(double a, double b) {
        return new Matrix(new  double[][]{
                {1,0,a},
                {0,1,b},
                {0,0,1}});
    }
    public static Matrix scale(double a, double b) {
        return new Matrix(new double[][]{
                {a,0,0},
                {0,b,0},
                {0,0,1}});
    }

    /**
     * Pay attention, it's implemented as written in the presentation of the lecture.
     * @param angle - angle in radians.
     * @return
     */
    public static Matrix rotate(double angle) {
        return new Matrix(new double[][]{
                {Math.cos(angle),Math.sin(angle),0},
                {-Math.sin(angle),Math.cos(angle),0},
                {0,0,1}});
    }

}
