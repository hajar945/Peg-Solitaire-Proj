import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import javax.swing.*;

public class GUI extends JPanel 
{
	private int x = 0, y = 15;
	private String [][] twoDCells = { 
									 {"Apples", "Oranges", "Bananas"},  
									 {"Cherries", "Plums", "Nectarines"},
									 {"Coconuts", "Mango", "Dragon Fruit" }, 
									 {"Blueberries", "Raspberries", "Poisonberries"} 
									}; 
            				 
	
	public GUI()
	{
		setBackground( Color.WHITE );
		setDoubleBuffered( true );
	}
public void paintComponent(Graphics g) {
         super.paintComponent( g );
         Graphics2D g2 = (Graphics2D) g;

         // Reset your starting y value here 
         y = 0;
         for (int i = 0; i < 4; i++) {
             for (int j = 0; j < 3; j++) {
                 g2.drawString(twoDCells[i][j], x, y);
                 y += 30;
             }

         }
     }
}