package tictactoe;

/*
  By Roman Andronov
 */

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class CellViewKeyHandler
	extends KeyAdapter
{
	public void
	keyPressed( KeyEvent ke )
	{
		int		kc = ke.getKeyCode();
		if ( kc != KeyEvent.VK_ENTER && kc != KeyEvent.VK_SPACE )
		{
			return;
		}
		( ( CellView )ke.getComponent() ).press();
	}

	public void
	keyReleased( KeyEvent ke )
	{
		int		kc = ke.getKeyCode();
		if ( kc != KeyEvent.VK_ENTER && kc != KeyEvent.VK_SPACE )
		{
			return;
		}
		( ( CellView )ke.getComponent() ).release( ke );
	}
}
