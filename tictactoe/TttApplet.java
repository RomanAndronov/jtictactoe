package tictactoe;

/*
  By Roman Andronov
*/

import javax.swing.JApplet;
import javax.swing.SwingUtilities;

public
class TttApplet
	extends JApplet
{
	public void
	init()
	{
		SwingUtilities.invokeLater( new Runnable()
		{
			public void
			run()
			{
				createAppletGui();
			}
		});
	}

	private void
	createAppletGui()
	{
		if ( tttpnl == null )
		{
			tttpnl = new TttPanel( this );
		}
	}

	private TttPanel		tttpnl = null;
}
