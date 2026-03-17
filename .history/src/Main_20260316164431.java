import java.io.File;

import javax.imageio.ImageIO;

import javax.swing.*;

public class Main extends JFrame
{
	    public static void main(String[] args) {
        JFrame frame = new JFrame("Peg Solitaire");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 480);
        
       // GUI gameBoard = new GUI();
        Checkers paint = new Checkers();
        frame.add(paint);
        
        frame.setLocationRelativeTo(null); // Centers the window on screen
        frame.setVisible(true);
    }
}