import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Main {
    public static void main(String[] args) {
        // making the window frame and titling it
        JFrame window = new JFrame("Peg Solitaire");
        
        // create the UI that builds the board and buttons
        UI gameUI = new UI();
        // put the UI inside the empty window frame
        window.setContentPane(gameUI);
        // everything fits perfercly in the window with no extra space
        window.pack();
        // calculates the monitor's screen so the came can open in the center
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation((screensize.width - window.getWidth())/2,
                           (screensize.height - window.getHeight())/2);
        
        // program will shut down completely when X is clicked
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // stops user from dragging the window to resize it. It would mess with the graphics
        window.setResizable(false);  
        // user can see the window
        window.setVisible(true);
    }
}