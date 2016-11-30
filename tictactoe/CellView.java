package tictactoe;

/*
  By Roman Andronov
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

class CellView
	extends JLabel
{
	CellView( TttPanel tttpnl, int cn )
	{
		pnlTtt = tttpnl;
		cellNumber = cn;

		addMouseListener( pnlTtt.CV_MH );
		addMouseMotionListener( pnlTtt.CV_MH );
		addFocusListener( pnlTtt.CV_FH );
		addKeyListener( pnlTtt.CV_KH );

		setMaximumSize( CELL_VIEW_DIM );
		setMinimumSize( CELL_VIEW_DIM );
		setPreferredSize( CELL_VIEW_DIM );

		setBorder( RAISED_BRDR );
		setOpaque( true );
		setFocusable( true );
	}

	int
	getCellNumber()
	{
		return cellNumber;
	}

	int
	getMark()
	{
		return mark;
	}

	void
	setCellColor( Color cc )
	{
		setForeground( cc );
	}

	/*
	  Visual clues for free cells
	 */
	void
	select()
	{
		if ( pnlTtt.gameOver )
		{
			return;
		}

		if ( pnlTtt.cells[ cellNumber ].getState() != Cell.STATE_FREE )
		{
			return;
		}

		selected = true;
		repaint();
	}

	void
	deSelect()
	{
		if ( pnlTtt.gameOver )
		{
			return;
		}

		if ( pnlTtt.cells[ cellNumber ].getState() != Cell.STATE_FREE )
		{
			return;
		}

		selected = false;
		repaint();
	}

	void
	press()
	{
		if ( pnlTtt.gameOver )
		{
			return;
		}

		if ( pnlTtt.cells[ cellNumber ].getState() != Cell.STATE_FREE )
		{
			return;
		}

		setBorder( LOWERED_BRDR );
		deSelect();
		pnlTtt.pressedCellNumber = cellNumber;
	}

	void
	release( EventObject event )
	{
		int		prsdcn = pnlTtt.pressedCellNumber;
	
		pnlTtt.pressedCellNumber = -1; // Release the press event

		if ( pnlTtt.gameOver )
		{
			return;
		}

		if ( pnlTtt.cells[ cellNumber ].getState() != Cell.STATE_FREE )
		{
			return;
		}

		setBorder( RAISED_BRDR );
		if ( pnlTtt.gameIsOn == false )
		{
			pnlTtt.gameIsOn = true;
		}

		/*
		  Make sure that the mouse event occurred
		  over the originating cell
		 */
		if ( event instanceof MouseEvent )
		{
			MouseEvent	me = ( MouseEvent )event;

			/*
			  Point in the originating cell's coordinates, the
			  one that originated the mouse press event
			 */
			Point	    ocvp = me.getPoint();

			/*
			  Point in the Board's coordinates
			 */
			Point	    brdp = SwingUtilities.convertPoint( this, ocvp, pnlTtt.pnlBoard );

			/*
			  Over what component was the mouse released?
			 */
			Component   comp = SwingUtilities.getDeepestComponentAt( pnlTtt.pnlBoard, brdp.x, brdp.y );
			if ( comp == null )
			{
				return;
			}

			if ( !( comp instanceof CellView ) )
			{
				return;
			}

			// Destination cell
			CellView    destcv = ( CellView )comp;

			/*
			  No op if releasing the mouse over the cell view that
			  did not originate the mouse press event
			 */
			int	    destcn = destcv.getCellNumber();
			if ( destcn != prsdcn )
			{
				return;
			}
		}

		// Mark this cell as taken/occupied
		pnlTtt.cells[ prsdcn ].setState( Cell.STATE_HUMAN );
		pnlTtt.moveNumber++;
		deSelect();

		if ( pnlTtt.checkEndOfGame() )
		{
			pnlTtt.endThisGame();
			return;
		}

		// Computer makes its move
		pnlTtt.nextComputerMove();
	}

	Color
	getBgColor()
	{
		return getColor( BG_COLOR );
	}

	Color
	getXColor()
	{
		return getColor( X_COLOR );
	}

	Color
	getOColor()
	{
		return getColor( O_COLOR );
	}

	private Color
	getColor( int tp )
	{
		boolean	reverseVideo = pnlTtt.winThree >= 0 &&
			( cellNumber == TttPanel.WINS[ pnlTtt.winThree ][ 0 ] ||
			cellNumber == TttPanel.WINS[ pnlTtt.winThree ][ 1 ] || 
			cellNumber == TttPanel.WINS[ pnlTtt.winThree ][ 2 ] );
	
		if ( !reverseVideo )
		{
			if ( tp == BG_COLOR )
			{
				return CELL_CLR;
			}
			else if ( tp == X_COLOR )
			{
				return X_CLR;
			}
			// Must be O
			return O_CLR;
		}

		// Do reverse video for the winning three cells
		if ( tp == BG_COLOR )
		{
			if ( mark == KeyEvent.VK_X )
			{
				return X_CLR;
			}
			return O_CLR;
		}

		return CELL_CLR;
	}

	public void
	paintComponent( Graphics g )
	{
		setBackground( getBgColor() );
		super.paintComponent( g );

		if ( selected )
		{
			drawHighlight( g );
			return;
		}

		int		state = pnlTtt.cells[ cellNumber ].getState();
		if ( state == Cell.STATE_FREE )
		{
			return;
		}

		if ( state == Cell.STATE_HUMAN )
		{
			if ( pnlTtt.playerMark == KeyEvent.VK_X )
			{
				drawX( g );
			}
			else
			{
				drawO( g );
			}
			return;
		}

		// This cell is taken by the computer
		if ( pnlTtt.computerMark == KeyEvent.VK_X )
		{
			drawX( g );
		}
		else
		{
			drawO( g );
		}
	}

	void
	drawHighlight( Graphics g )
	{
		int		cvw = getWidth();
		int		cvh = getHeight();
		Insets		cvi = getInsets();

		int		hlx = cvi.left + HL_OFFSET;
		int		hly = cvi.top + HL_OFFSET;
		int		hlw = cvw - cvi.left - cvi.right - 2 * HL_OFFSET - 1;
		int		hlh = cvh - cvi.top - cvi.bottom - 2 * HL_OFFSET - 1;
	
		g.setColor( X_CLR );
		g.drawRect( hlx, hly, hlw, hlh );
	}

	void
	drawX( Graphics g )
	{
		int		cvw = getWidth();
		int		cvh = getHeight();
		Insets		cvi = getInsets();

		int		x1 = cvi.left + 3 * HL_OFFSET;
		int		y1 = cvi.top + 3 * HL_OFFSET;
		int		x2 = x1 + cvw - cvi.left - cvi.right - 6 * HL_OFFSET - 1;
		int		y2 = y1 + cvh - cvi.top - cvi.bottom - 6 * HL_OFFSET - 1;
		int		t = y1;

		Graphics2D  g2d = ( Graphics2D )g.create();
		g2d.setColor( getColor( X_COLOR ) );
		g2d.setStroke( new BasicStroke( XLINE_WIDTH ) );
		g2d.drawLine( x1, y1, x2, y2 );

		y1 = y2;
		y2 = t;
		g2d.drawLine( x1, y1, x2, y2 );
		g2d.dispose();
	}

	public void
	drawO( Graphics g )
	{
		int		cvw = getWidth();
		int		cvh = getHeight();
		Insets		cvi = getInsets();

		int		x = cvi.left + 3 * HL_OFFSET;
		int		y = cvi.top + 3 * HL_OFFSET;
		int		w = cvw - cvi.left - cvi.right - 6 * HL_OFFSET - 1;
		int		h = cvh - cvi.top - cvi.bottom - 6 * HL_OFFSET - 1;

		Graphics2D  g2d = ( Graphics2D )g.create();
		g2d.setColor( getColor( O_COLOR ) );
		g2d.fillOval( x, y, w, h );

		g2d.setColor( CELL_CLR );
		x += OLINE_WIDTH;
		y += OLINE_WIDTH;
		w -= 2 * OLINE_WIDTH;
		h -= 2 * OLINE_WIDTH;
		g2d.fillOval( x, y, w, h );

		g2d.dispose();
	}

	static final int			HL_OFFSET = 5;
	static final float			XLINE_WIDTH = 7.0f;
	static final int			OLINE_WIDTH = 7;
	static final int			CELL_VIEW_SIZE = 150;
	static final Dimension			CELL_VIEW_DIM =
		new Dimension( CELL_VIEW_SIZE, CELL_VIEW_SIZE );
	static final BevelBorder		RAISED_BRDR = new BevelBorder( BevelBorder.RAISED );
	static final BevelBorder		LOWERED_BRDR = new BevelBorder( BevelBorder.LOWERED );
	static final Color			CELL_CLR = new Color( 227, 224, 207 );
	static final Color			X_CLR = new Color( 114, 120, 116 );
	static final Color			O_CLR = new Color( 114, 120, 116 );
	
	private static final int		BG_COLOR = 0;
	private static final int		X_COLOR = 1;
	private static final int		O_COLOR = 2;
	private final int			cellNumber;
	private final TttPanel			pnlTtt;
	private int				mark = -1;
	private boolean				selected = false;
}
