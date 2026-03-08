import javax.swing.*;

public class Main extends JFrame
{
	
	
	public Main()
	{
		
		add( new GUI() );
		
		setDefaultCloseOperation( EXIT_ON_CLOSE );
		setTitle( "Cells" );
		setSize( 450,150 );
		setVisible( true );
		setLocationRelativeTo( null );
		setResizable( false );
	}
	
	public static void main(String [] args)
	{
		new Main();
	}
}