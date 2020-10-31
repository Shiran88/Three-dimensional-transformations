//Shiran Golbar, 313196974
//Lev Levin, 342480456
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
	// write your code here

            Frame frame = new Frame("EX1");
            CanvasOfAll canvas = new CanvasOfAll(frame,"ex1.scn", "ex1.viw");
            frame.add(canvas);

            WindowAdapter myWindowAdapter = new WindowAdapter(){
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            };

            frame.addWindowListener(myWindowAdapter);
            frame.pack();
            frame.setVisible(true);

    }
}
