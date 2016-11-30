package tictactoe;

/*
  By Roman Andronov
 */

class Cell
{
	Cell( int cn )
	{
		number = cn;
	}

	int
	getNumber()
	{
		return number;
	}

	int
	getState()
	{
		return state;
	}

	CellView
	getView()
	{
		return cellView;
	}

	void
	setState( int s )
	{
		state = s;
		if ( cellView != null )
		{
			cellView.repaint();
		}
	}

	void
	setView( CellView cv )
	{
		cellView = cv;
	}

	static final int		STATE_FREE = 0;
	static final int		STATE_HUMAN = 1;
	static final int		STATE_COMPUTER = 2;

	private final int		number;
	private int			state; // Varies
	private CellView		cellView; // Can be null
}
