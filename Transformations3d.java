


enum RotationAxis {X,Y,Z};
/**
 * This class provides with tools and means that generate 3d transformation matrices such as translation matrix and
 * rotation matrix.
 */

public class Transformations3d {

    public static Matrix translate(double a, double b, double c) {
        return new Matrix(new  double[][]{
                {1,0,0,a},
                {0,1,0,b},
                {0,0,1,c},
                {0,0,0,1}
        });
    }
    public static Matrix scale(double a, double b, double c) {
        return new Matrix(new double[][]{
                {a,0,0,0},
                {0,b,0,0},
                {0,0,c,0},
                {0,0,0,1}
        });
    }

    /**
     * Pay attention, it's implemented as written in the presentation of the lecture.
     * @param angle - angle in radians.
     * @return rotation matrix.
     */
    public static Matrix rotate(double angle, RotationAxis axis) {
        switch (axis) {
            case X:
                return new Matrix(new double[][]{
                        {1, 0, 0, 0},
                        {0, Math.cos(angle),-Math.sin(angle), 0},
                        {0, Math.sin(angle), Math.cos(angle), 0},
                        {0, 0, 0, 1}
                });
            case Y:
                return new Matrix(new double[][]{
                        {Math.cos(angle),0 ,-Math.sin(angle), 0},
                        {0, 1, 0, 0},
                        {Math.sin(angle), 0, Math.cos(angle), 0},
                        {0, 0, 0, 1}
                });
            case Z:
                return new Matrix(new double[][]{
                        {Math.cos(angle),-Math.sin(angle),0, 0},
                        {Math.sin(angle),Math.cos(angle),0,0},
                        {0,0,1,0},
                        {0,0,0,1}
                });
        }
        //if something reaches here, it means that argument 'axis' is wrong
        throw new IllegalArgumentException();
    }
    public static Matrix orthographicProjection() {
        Matrix m = Matrix.createIdentityMatrix(4);
        m.setEntry(0, 2,2);
        return  m;
    }

    /**
     * Generates viewing matrix.
     * @param cameraPos
     * @param lookAt
     * @param viewUp
     * @return
     */
    public static Matrix Viewing(Vector cameraPos, Vector lookAt, Vector viewUp) {
        Vector zv = cameraPos.subtract(lookAt).normalize();
        Vector xv = viewUp.crossProduct3d(zv).normalize();
        Vector yv = zv.crossProduct3d(xv);

        Matrix r = Transformations3d.translate(
                -cameraPos.getElement(0), -cameraPos.getElement(1), -cameraPos.getElement(2));
        Matrix t = new Matrix(new double[][]{
                {xv.getElement(0), xv.getElement(1), xv.getElement(2),0},
                {yv.getElement(0), yv.getElement(1), yv.getElement(2),0},
                {zv.getElement(0), zv.getElement(1), zv.getElement(2),0},
                {0,0,0,1}
        });
        return r.multiply(t);
    }



}
