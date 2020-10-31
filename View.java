import java.io.*;
import java.nio.file.FileSystemNotFoundException;
import java.util.Scanner;
/**
 * This class represents a parser of view files (files with .viw extension)
 */
public class View {
    private File viwFile;
    private double leftBound;
    private double topBound;
    private int widthScreen;
    private int heightScreen;
    private Vertex cameraPosition;
    private Vertex lookAt;
    private Vertex cameraAngleUp;
    private double bottomBound;
    private double rightBound;
    private double windowWidth;
    private double windowHeight;

    public View(String viwFileName) {
        this.viwFile = creatTheFile(viwFileName);
    }

    public File creatTheFile(String filename) {
        File myObj = null;
        try {
            myObj = new File(filename);
        } catch (FileSystemNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return myObj;
    }

    public void parseViwFile() {
        try {
            Scanner myReader = new Scanner(this.viwFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] arrOfStr = data.split(" ");
                switch (arrOfStr[0]) {
                    case "LookAt":
                        this.lookAt = new Vertex(Double.parseDouble(arrOfStr[1]), Double.parseDouble(arrOfStr[2]),
                                Double.parseDouble(arrOfStr[3]));
                        break;
                    case "Position":
                        this.cameraPosition = new Vertex(Double.parseDouble(arrOfStr[1]), Double.parseDouble(arrOfStr[2]), Double.parseDouble(arrOfStr[3]));
                        break;
                    case "Window":
                        this.leftBound = Double.parseDouble(arrOfStr[1]);
                        this.rightBound = Double.parseDouble(arrOfStr[2]);
                        this.bottomBound = Double.parseDouble(arrOfStr[3]);
                        this.topBound = Double.parseDouble(arrOfStr[4]);
                        this.windowHeight = this.topBound - this.bottomBound;
                        this.windowWidth = this.rightBound - this.leftBound;
                        break;
                    case "Up":
                        this.cameraAngleUp = new Vertex(Double.parseDouble(arrOfStr[1]), Double.parseDouble(arrOfStr[2]),
                                Double.parseDouble(arrOfStr[3]));
                        break;
                    case "Viewport":
                        this.widthScreen = Integer.parseInt(arrOfStr[1]);
                        this.heightScreen = Integer.parseInt(arrOfStr[2]);
                        break;
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }



    public double getRightBound() {
        return rightBound;
    }
    public double getLeftBound() {
        return leftBound;
    }
    public double getBottomBound() {
        return bottomBound;
    }
    public double getWindowWidth(){return (this.rightBound - this.leftBound);}
    public double getWindowHeight(){
        return (this.topBound - this.bottomBound);
    }

    public double getTopBound() {
        return topBound;
    }

    public int getWidthScreen() {
        return this.widthScreen;
    }
    public void setWidthScreen(int width) {
        this.widthScreen = width;
    }


    public int getHeightScreen() {
        return this.heightScreen;
    }
    public void setHeightScreen(int height) {
        this.heightScreen = height;
    }

    public Vertex getCameraPosition() {
        return this.cameraPosition;
    }

    public Vertex getLookAt() {
        return lookAt;
    }


    public Vertex getCameraAngleUp() {
        return this.cameraAngleUp;
    }

    public Matrix getViewPortMatrix() {
        double cx = this.getLeftBound() + (this.getRightBound() - this.getLeftBound()) / 2;
        double cy = this.getBottomBound() + (this.getTopBound() - this.getBottomBound()) / 2;
        Matrix tr1 = Transformations3d.translate(-cx, -cy,0);
        Matrix s = Transformations3d.scale(this.widthScreen / this.windowWidth,
                - this.heightScreen / this.windowHeight,1);
        Matrix tr2 = Transformations3d.translate((this.widthScreen /2) + 20,
                (this.heightScreen / 2) + 20, 0);
        return tr2.multiply(s.multiply(tr1));
    }




}





