package tictactoe;

/*
  By Roman Andronov
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.RootPaneContainer;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

class TttPanel
	extends JPanel
	implements ActionListener
{
	TttPanel( RootPaneContainer rpc )
	{
		super();
		
		CV_MH = new CellViewMouseHandler();
		CV_FH = new CellViewFocusHandler();
		CV_KH = new CellViewKeyHandler();

		init( rpc );
	}

	public void
	actionPerformed( ActionEvent ae )
	{
		Object		o = ae.getSource();

		if ( o instanceof JButton )
		{
			JButton		jb = ( JButton )o;

			if ( jb == jbNewGame )
			{
				newGame();
			}
		}
	}

	private void
	init( RootPaneContainer rpc )
	{
		gui = new TttGui( this );

		gui.init( rpc );

		newGame();
	}

	private void
	newGame()
	{
		boolean			cgf = false;
		int			firstMove = -1;

		gameOver = false;
		gameIsOn = true;
	
		winThree = -1;
		bestCell = -1;
		moveNumber = 1;
	
		for ( int i = 0; i < cells.length; i++ )
		{
			cells[ i ].setState( Cell.STATE_FREE );
		}

		if ( jrbX.isSelected() )
		{
			playerMark = KeyEvent.VK_X;
			computerMark = KeyEvent.VK_O;
		}
		else
		{
			playerMark = KeyEvent.VK_O;
			computerMark = KeyEvent.VK_X;
		}

		if ( jrbRandom.isSelected() )
		{
			firstMove = FM_RANDOM;
		}
		else if ( jrbPlayer.isSelected() )
		{
			firstMove = FM_PLAYER;
		}
		else
		{
			cgf = true;
			firstMove = FM_COMPUTER;
		}

		if ( jrbEasy.isSelected() )
		{
			gameLevel = GL_EASY;
		}
		else if ( jrbMedium.isSelected() )
		{
			gameLevel = GL_MEDIUM;
		}
		else
		{
			gameLevel = GL_HARD;
		}

		if ( firstMove == FM_RANDOM )
		{
			int		ri = ( new Random() ).nextInt( 2 );

			if ( ri == 0 )
			{
				cgf = true;
			}
		}
	
		if ( cgf )
		{
			firstComputerMove();
		}
	}

	int
	firstComputerMove()
	{
		int		n = -1;

		gameIsOn = true;

		if ( gameLevel == GL_EASY )
		{
			n = easyMove(); // Pick cells at random
		}
		else if ( gameLevel == GL_MEDIUM )
		{
			// Play to block obvious wins
			// Start with the edges: cells 1, 3, 5, 7
			int		r = ( new Random() ).nextInt( 4 );

			n = 2 * r + 1;
			cells[ n ].setState( Cell.STATE_COMPUTER );
		}
		else
		{
			// Play to win
			n = hardMove();
		}
		moveNumber++;

		return n;
	}

	int
	nextComputerMove()
	{
		int		n = -1;

		if ( gameLevel == GL_EASY )
		{
		    n = easyMove();
		}
		else if ( gameLevel == GL_MEDIUM )
		{
		    n = mediumMove();
		}
		else
		{
		    n = hardMove();
		}
		moveNumber++;

		if ( checkEndOfGame() )
		{
		    endThisGame();
		}

		return n;
	}

	int
	easyMove()
	{
		int		rc = 0;
		int		nfc = 0;
		int		fci = -1;

		// Pick a random cell, if more then one free cell is left
		for ( int i = 0; i < cells.length; i++ )
		{
			if ( cells[ i ].getState() == Cell.STATE_FREE )
			{
				nfc++;
				fci = i;
			}
		}

		if ( nfc == 1 )
		{
			cells[ fci ].setState( Cell.STATE_COMPUTER );
			return fci;
		}

		while ( true )
		{
			rc = ( new Random() ).nextInt( cells.length );
			if ( cells[ rc ].getState() != Cell.STATE_FREE )
			{
				continue; // Pick another one.
			}
			break;
		}

		cells[ rc ].setState( Cell.STATE_COMPUTER );
		return rc;
	}

	int
	mediumMove()
	{
		int		b = -1;

		// See if we can block one-move win
		for ( int i = 0; i < WINS.length; i++ )
		{
			b = blockOneMoveWin( WINS[ i ][ 0 ], WINS[ i ][ 1 ], WINS[ i ][ 2 ] );
			if ( b >= 0 )
			{
				cells[ b ].setState( Cell.STATE_COMPUTER );
				return b;
			}
		}

		// No obvious blocks - pick a random cell
		b = easyMove();
		return b;
	}

	int
	blockOneMoveWin( int l, int m, int r )
	{
		int		b = -1;
		int		ls = cells[ l ].getState();
		int		ms = cells[ m ].getState();
		int		rs = cells[ r ].getState();

		if ( ls == Cell.STATE_HUMAN &&
			ms == Cell.STATE_HUMAN &&
			rs == Cell.STATE_FREE )
		{
			b = r;
		}
		else if ( ls == Cell.STATE_FREE &&
			ms == Cell.STATE_HUMAN &&
			rs == Cell.STATE_HUMAN )
		{
			b = l;
		}
		else if ( ls == Cell.STATE_HUMAN &&
			ms == Cell.STATE_FREE &&
			rs == Cell.STATE_HUMAN )
		{
			b = m;
		}

		return b;
	}

	int
	hardMove()
	{
		findBestCell( cells, Cell.STATE_COMPUTER, moveNumber, -10, 10 );
		cells[ bestCell ].setState( Cell.STATE_COMPUTER );
		return bestCell;
	}

	private int
	Opponent( int player )
	{
		return player ==
			Cell.STATE_HUMAN ?
				Cell.STATE_COMPUTER : Cell.STATE_HUMAN;
	}

	int
	getWeight( Cell[] vb, int player )
	{
		int		state = -1;
		int		plPos = 0;
		int		opPos = 0;
		int		weight = 0;
		int		opponent = Opponent( player );

		for ( int i = 0; i < WINS.length; i++ )
		{
			plPos = 0;
			opPos = 0;
			for ( int j = 0; j < COLUMN_COUNT; j++ )
			{
				state = vb[ WINS[ i ][ j ] ].getState();
				if ( state == player )
				{
					plPos++;
				}
				else if ( state == opponent )
				{
					opPos++;
				}
			}
			weight += WEIGHTS[ plPos ][ opPos ];
		}

		return weight;
	}

	int
	virtualWinner( Cell[] vb )
	{
		int		s1 = -1;
		int		s2 = -1;
		int		s3 = -1;

		// Any three consequent cells taken by one player?
		for ( int i = 0; i < WINS.length; i++ )
		{
			s1 = vb[ WINS[ i ][ 0 ] ].getState();
			if ( s1 == Cell.STATE_FREE )
			{
			    continue;
			}
			s2 = vb[ WINS[ i ][ 1 ] ].getState();
			s3 = vb[ WINS[ i ][ 2 ] ].getState();
			if ( s1 != s2 || s1 != s3 )
			{
				continue;
			}
			if ( s1 == Cell.STATE_HUMAN )
			{
				return RESULT_HUMAN_WON;
			}
			else
			{
				return RESULT_COMPUTER_WON;
			}
		}

		// Free cells left - game not over yet.
		for ( int i = 0; i < vb.length; i++ )
		{
			if ( vb[ i ].getState() == Cell.STATE_FREE )
			{
				return RESULT_NO_WINNER_YET;
			}
		}

		// No winners, no free cells - a tie
		return RESULT_TIE;
	}

	int
	findBestCell( Cell[] vb, int player, int mvnmbr, int alpha, int beta )
	{
		/*
		  Make a new Virtual Board - copy of 
		  what's passed in with no views attached
		 */
		Cell[]		newvb = new Cell[ CELL_COUNT ];
		for ( int i = 0; i < CELL_COUNT; i++ )
		{
			newvb[ i ] = new Cell( i );
			newvb[ i ].setState( vb[ i ].getState() );
		}

		// New weights table
		CellWeight[]	    cweights = new CellWeight[ CELL_COUNT ];
		for ( int i = 0; i < CELL_COUNT; i++ )
		{
			cweights[ i ] = new CellWeight();
		}

		/*
		  Get weights for all potential moves.
		  Sort them in descending order
		 */
		int		weight;
		int		movesLeft = 0;
		for ( int i = 0; i < CELL_COUNT; i++ )
		{
			if ( newvb[ i ].getState() != Cell.STATE_FREE )
			{
				continue;
			}

			// Make a move. Get weight. Restore
			newvb[ i ].setState( player );
			weight = getWeight( newvb, player );
			newvb[ i ].setState( Cell.STATE_FREE );

			// Keep the weights in sorted order
			int		j;
			for ( j = movesLeft - 1; j >= 0 && cweights[ j ].weight < weight; j-- )
			{
				cweights[ j + 1 ].cell = cweights[ j ].cell;
				cweights[ j + 1 ].weight = cweights[ j ].weight;
			}
			cweights[ j + 1 ].cell = i;
			cweights[ j + 1 ].weight = weight;
			movesLeft++;
		}

		// Alpha-beta prune the remaining moves
		int		cell;
		int		score;
		int		vWinner;
		int		bc = -1;
		for ( int i = 0; i < movesLeft; i++ )
		{
			// Make a move. Get it's score
			cell = cweights[ i ].cell;
			vb[ cell ].setState( player );
			vWinner = virtualWinner( vb );

			if ( vWinner == RESULT_COMPUTER_WON )
			{
				score = ( CELL_COUNT + 1 ) - mvnmbr;
			}
			else if ( vWinner == RESULT_HUMAN_WON )
			{
				score = mvnmbr - ( CELL_COUNT + 1 );
			}
			else if ( vWinner == RESULT_TIE )
			{
				score = 0;
			}
			else // no winner yet - keep going
			{
				score = findBestCell( vb, Opponent( player ), mvnmbr + 1, alpha, beta );
			}

			// Restore the game. Prune
			vb[ cell ].setState( Cell.STATE_FREE );

			if ( player == Cell.STATE_COMPUTER )
			{
				if ( score >= beta )
				{
					bestCell = cell;
					return score;
				}
				else if ( score > alpha )
				{
					alpha = score;
					bc = cell;
				}
			}
			else
			{
				if ( score <= alpha )
				{
					bestCell = cell;
					return score;
				}
				else if ( score < beta )
				{
					beta = score;
					bc = cell;
				}
			}
		}

		bestCell = bc;

		if ( player == Cell.STATE_COMPUTER )
		{
			return alpha;
		}

		return beta;
	}

	void
	endThisGame()
	{

	}

	boolean
	checkEndOfGame()
	{
		int		s1 = -1;
		int		s2 = -1;
		int		s3 = -1;
		boolean		gio = false;
		JLabel		lbl = null;
		String		txt = null;

		// Any three consequent cells taken by one player?
		for ( int i = 0; i < WINS.length; i++ )
		{
			s1 = cells[ WINS[ i ][ 0 ] ].getState();
			if ( s1 == Cell.STATE_FREE )
			{
				continue;
			}
			s2 = cells[ WINS[ i ][ 1 ] ].getState();
			s3 = cells[ WINS[ i ][ 2 ] ].getState();
			if ( s1 != s2 || s1 != s3 )
			{
				continue;
			}
			winThree = i;
			if ( s1 == Cell.STATE_HUMAN )
			{
				numWon++;
				lbl = lblWon;
				txt = "Won: " + numWon;
			}
			else
			{
				numLost++;
				lbl = lblLost;
				txt = "Lost: " + numLost;
			}
			gio = true;
			repaint();
			break;
		}

		if ( !gio )
		{
			for ( int i = 0; i < cells.length; i++ )
			{
				if ( cells[ i ].getState() == Cell.STATE_FREE )
				{
					return false;
				}
			}
			// No free cells - must be a tie.
			numTied++;
			lbl = lblTied;
			txt = "Tied: " + numTied;
		}

		// It's over
		gameIsOn = false;
		gameOver = true;
		lbl.setText( txt );
		return true;
	}

	static final int		CELL_COUNT = 9;
	static final int		COLUMN_COUNT = 3;

	static final int		GL_EASY = 0;
	static final int		GL_MEDIUM = 1;
	static final int		GL_HARD = 2;

	static final int		FM_RANDOM = 0;
	static final int		FM_PLAYER = 1;
	static final int		FM_COMPUTER = 2;

	static final int		RESULT_HUMAN_WON = 0;
	static final int		RESULT_COMPUTER_WON = 1;
	static final int		RESULT_TIE = 2;
	static final int		RESULT_NO_WINNER_YET = 3;

	// Holds the winning cell sequences
	static final int[][]		WINS = 
	{
		{ 0, 1, 2 },
		{ 3, 4, 5 },
		{ 6, 7, 8 },
		{ 0, 3, 6 },
		{ 1, 4, 7 },
		{ 2, 5, 8 },
		{ 0, 4, 8 },
		{ 2, 4, 6 }
	};

	static final int[][]		WEIGHTS = 
	{
		{ 0, -10, -100, -1000 },
		{ 10, 0, 0, 0 },
		{ 100, 0, 0, 0 },
		{ 1000, 0, 0, 0 }
	};

	final CellViewMouseHandler	CV_MH;
	final CellViewFocusHandler	CV_FH;
	final CellViewKeyHandler	CV_KH;

	TttGui				gui = null;

	JPanel				pnlBoard = null;

	JPanel				pnlCtrls = null;
	JButton				jbNewGame = null;

	JLabel				lblMyMark = null;
	ButtonGroup			bgrpMyMark = null;
	JRadioButton			jrbX = null;
	JRadioButton			jrbO = null;

	JLabel				lblFirstMove = null;
	ButtonGroup			bgrpFirstMove = null;
	JRadioButton			jrbPlayer = null;
	JRadioButton			jrbRandom = null;
	JRadioButton			jrbComputer = null;

	JLabel				lblGameLevel = null;
	ButtonGroup			bgrpGameLevel = null;
	JRadioButton			jrbEasy = null;
	JRadioButton			jrbMedium = null;
	JRadioButton			jrbHard = null;

	JLabel				lblStats = null;
	JLabel				lblWon = null;
	JLabel				lblLost = null;
	JLabel				lblTied = null;

	Cell[]				cells;
	boolean				gameIsOn = false;
	boolean				gameOver = true;
	int				pressedCellNumber = -1;
	int				winThree = -1; // The winning triplet
	int				moveNumber = 0;
	int				playerMark = KeyEvent.VK_X;
	int				computerMark = KeyEvent.VK_O;
	int				gameLevel = GL_EASY;
	int				bestCell;
	int				numWon = 0;
	int				numLost = 0;
	int				numTied = 0;
}
