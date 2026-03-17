import java.io.File;

import javax.imageio.ImageIO;

import javax.swing.*;

public class Main extends JFrame
{
	    public static void main(String[] args) {
       // 1. Create the window
        JFrame window = new JFrame("Peg Solitaire");
        
        // 2. Create your game board and add it to the window
        Checkers gameBoard = new Checkers(); 
        window.setContentPane(gameBoard);
        
        // 3. Pack it tight so the window wraps around the board perfectly
        window.pack();
        
        // 4. Center the window on your screen
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation( (screensize.width - window.getWidth())/2,
                            (screensize.height - window.getHeight())/2 );
        
        // 5. Make sure the program actually stops running when you hit the red X
        window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        window.setResizable(false);  
        
        // 6. Make it visible!
        window.setVisible(true);
    }
}