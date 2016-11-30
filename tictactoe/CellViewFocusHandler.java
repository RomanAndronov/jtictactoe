package tictactoe;

/*
  By Roman Andronov
 */

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

class CellViewFocusHandler
	implements FocusListener
{
	public void 
	focusGained( FocusEvent fe )
	{
		( ( CellView )fe.getComponent() ).select();
	}

	public void 
	focusLost( FocusEvent fe )
	{
		( ( CellView )fe.getComponent() ).deSelect();
	}
}
