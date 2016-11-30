package tictactoe;

/*
  By Roman Andronov
 */

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

class CellViewMouseHandler
	extends MouseInputAdapter
{
	public void
	mousePressed( MouseEvent me )
	{
		( ( CellView )me.getComponent() ).press();
	}

	public void
	mouseReleased( MouseEvent me )
	{
		( ( CellView )me.getComponent() ).release( me );
	}

	public void
	mouseEntered( MouseEvent me )
	{
		CellView	cv = ( CellView )me.getComponent();
		cv.requestFocusInWindow();
		cv.select();
	}

	public void
	mouseExited( MouseEvent me )
	{
		( ( CellView )me.getComponent() ).deSelect();
	}
}
