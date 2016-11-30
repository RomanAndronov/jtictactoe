package tictactoe;

/*
  By Roman Andronov
 */

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;

/*
   This will execute tictactoe as a stand-alone
   Java program.

   To execute it as a Java applet consult the
   TttApplet class in this package
*/

public
class TttFrame
	extends JFrame
{
	public
	TttFrame()
	{
		super();
		setTitle( "Tic Tac Toe" );
		setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
	}

	public static void
	main( String[] args )
	{
		SwingUtilities.invokeLater( new Runnable()
		{
			public void
			run()
			{
				TttFrame		tttfrm = new TttFrame();

				tttfrm.tttpnl = new TttPanel( tttfrm );
				tttfrm.pack();
				tttfrm.setLocationRelativeTo( null );
				tttfrm.setVisible( true );
			}
		});

	}

	private TttPanel		tttpnl = null;
}
