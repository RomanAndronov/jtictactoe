package tictactoe;

/*
  By Roman Andronov
 */

import java.awt.Insets;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.RootPaneContainer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;

class TttGui
{
	TttGui( TttPanel pnlttt )
	{
		pnlTtt = pnlttt;
	}

	void
	init( RootPaneContainer rpc )
	{
		GridBagConstraints	gbc = new GridBagConstraints();


		gbc.gridx = gbc.gridy = 0;
		gbc.weightx = gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = INSETS;

		rpc.getContentPane().setLayout( new GridBagLayout() );

		pnlTtt.setLayout( new GridBagLayout() );
		pnlTtt.setBorder( BorderFactory.createLineBorder( CLRGRAY ) );
		rpc.getContentPane().add( pnlTtt, gbc );

		pnlTtt.pnlBoard = new JPanel();
		pnlTtt.pnlBoard.setBorder( BorderFactory.createLineBorder( CLRGRAY ) );
		pnlTtt.pnlBoard.setLayout( new GridBagLayout() );
		pnlTtt.add( pnlTtt.pnlBoard, gbc );
		mkBoardPnl();

		gbc.gridy = 1;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		pnlTtt.pnlCtrls = new JPanel();
		pnlTtt.pnlCtrls.setBorder( BorderFactory.createLineBorder( CLRGRAY ) );
		pnlTtt.pnlCtrls.setLayout( new GridBagLayout() );
		pnlTtt.add( pnlTtt.pnlCtrls, gbc );
		mkCtrlsPnl();
	}

	private void
	mkBoardPnl()
	{
		CellView		cv = null;
		GridBagConstraints	gbc = new GridBagConstraints();

		pnlTtt.pnlBoard.setBackground( BOARD_CLR );
		
		gbc.gridx = gbc.gridy = 0;
		gbc.weightx = gbc.weighty = 1.0;
		gbc.insets = INSETS;
		gbc.fill = GridBagConstraints.BOTH;

		pnlTtt.cells = new Cell[ TttPanel.CELL_COUNT ];

		for ( int i = 0; i < pnlTtt.cells.length; i++ )
		{
			pnlTtt.cells[ i ] = new Cell( i );

			// Bind each logical cell to its view
			gbc.gridx = i % TttPanel.COLUMN_COUNT;
			gbc.gridy = i / TttPanel.COLUMN_COUNT;
			cv = new CellView( pnlTtt, i );
			pnlTtt.cells[ i ].setView( cv );
			pnlTtt.pnlBoard.add( cv, gbc );
		}
	}

	private void
	mkCtrlsPnl()
	{
		GridBagConstraints	gbc = new GridBagConstraints();

		gbc.gridx = gbc.gridy = 0;
		gbc.insets = INSETS;
		gbc.anchor = GridBagConstraints.EAST;

		pnlTtt.pnlCtrls.setBackground( BOARD_CLR );
		
		pnlTtt.jbNewGame = new JButton( "New Game" );
		pnlTtt.jbNewGame.setMnemonic( KeyEvent.VK_N );
		pnlTtt.jbNewGame.addActionListener( pnlTtt );
		pnlTtt.pnlCtrls.add( pnlTtt.jbNewGame, gbc );

		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 1;
		pnlTtt.lblMyMark = new JLabel( "My Mark: " );
		pnlTtt.pnlCtrls.add( pnlTtt.lblMyMark, gbc );

		pnlTtt.bgrpMyMark = new ButtonGroup();
		pnlTtt.jrbX = new JRadioButton( "X" );
		pnlTtt.jrbX.setMnemonic( KeyEvent.VK_X );
		pnlTtt.jrbX.setSelected( true );
		pnlTtt.jrbX.setBackground( BOARD_CLR );
		pnlTtt.bgrpMyMark.add( pnlTtt.jrbX );
		gbc.gridx = 2;
		pnlTtt.pnlCtrls.add( pnlTtt.jrbX, gbc );

		gbc.gridx = 3;
		pnlTtt.jrbO = new JRadioButton( "O" );
		pnlTtt.jrbO.setMnemonic( KeyEvent.VK_O );
		pnlTtt.jrbO.setBackground( BOARD_CLR );
		pnlTtt.bgrpMyMark.add( pnlTtt.jrbO );
		pnlTtt.pnlCtrls.add( pnlTtt.jrbO, gbc );

		gbc.gridy = 1;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.EAST;
		pnlTtt.lblFirstMove = new JLabel( "First Move: " );
		pnlTtt.pnlCtrls.add( pnlTtt.lblFirstMove, gbc );

		gbc.anchor = GridBagConstraints.WEST;

		pnlTtt.bgrpFirstMove = new ButtonGroup();
		pnlTtt.jrbPlayer = new JRadioButton( "Player" );
		pnlTtt.jrbPlayer.setMnemonic( KeyEvent.VK_M );
		pnlTtt.jrbPlayer.setSelected( true );
		pnlTtt.jrbPlayer.setBackground( BOARD_CLR );
		pnlTtt.bgrpFirstMove.add( pnlTtt.jrbPlayer );
		gbc.gridx = 1;
		pnlTtt.pnlCtrls.add( pnlTtt.jrbPlayer, gbc );

		gbc.gridx = 2;
		pnlTtt.jrbRandom = new JRadioButton( "Random" );
		pnlTtt.jrbRandom.setMnemonic( KeyEvent.VK_R );
		pnlTtt.jrbRandom.setBackground( BOARD_CLR );
		pnlTtt.bgrpFirstMove.add( pnlTtt.jrbRandom );
		pnlTtt.pnlCtrls.add( pnlTtt.jrbRandom, gbc );

		gbc.gridx = 3;
		pnlTtt.jrbComputer = new JRadioButton( "Computer" );
		pnlTtt.jrbComputer.setMnemonic( KeyEvent.VK_C );
		pnlTtt.jrbComputer.setBackground( BOARD_CLR );
		pnlTtt.bgrpFirstMove.add( pnlTtt.jrbComputer );
		pnlTtt.pnlCtrls.add( pnlTtt.jrbComputer, gbc );

		gbc.gridy = 2;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.EAST;
		pnlTtt.lblGameLevel = new JLabel( "Game Level: " );
		pnlTtt.pnlCtrls.add( pnlTtt.lblGameLevel, gbc );

		gbc.anchor = GridBagConstraints.WEST;

		pnlTtt.bgrpGameLevel = new ButtonGroup();
		pnlTtt.jrbEasy = new JRadioButton( "Easy" );
		pnlTtt.jrbEasy.setMnemonic( KeyEvent.VK_E );
		pnlTtt.jrbEasy.setSelected( true );
		pnlTtt.jrbEasy.setBackground( BOARD_CLR );
		pnlTtt.bgrpGameLevel.add( pnlTtt.jrbEasy );
		gbc.gridx = 1;
		pnlTtt.pnlCtrls.add( pnlTtt.jrbEasy, gbc );

		gbc.gridx = 2;
		pnlTtt.jrbMedium = new JRadioButton( "Medium" );
		pnlTtt.jrbMedium.setMnemonic( KeyEvent.VK_D );
		pnlTtt.jrbMedium.setBackground( BOARD_CLR );
		pnlTtt.bgrpGameLevel.add( pnlTtt.jrbMedium );
		pnlTtt.pnlCtrls.add( pnlTtt.jrbMedium, gbc );

		gbc.gridx = 3;
		pnlTtt.jrbHard = new JRadioButton( "Hard" );
		pnlTtt.jrbHard.setMnemonic( KeyEvent.VK_H );
		pnlTtt.jrbHard.setBackground( BOARD_CLR );
		pnlTtt.bgrpGameLevel.add( pnlTtt.jrbHard );
		pnlTtt.pnlCtrls.add( pnlTtt.jrbHard, gbc );

		gbc.gridy = 3;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.EAST;
		pnlTtt.lblStats = new JLabel( "Stats: " );
		pnlTtt.pnlCtrls.add( pnlTtt.lblStats, gbc );

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		pnlTtt.lblWon = new JLabel( "Won: 0" );
		pnlTtt.pnlCtrls.add( pnlTtt.lblWon, gbc );

		gbc.gridx = 2;
		pnlTtt.lblLost = new JLabel( "Lost: 0" );
		pnlTtt.pnlCtrls.add( pnlTtt.lblLost, gbc );

		gbc.gridx = 3;
		pnlTtt.lblTied = new JLabel( "Tied: 0" );
		pnlTtt.pnlCtrls.add( pnlTtt.lblTied, gbc );
	}


	TttPanel				pnlTtt;

	static final Insets			INSETS = new Insets( 5, 5, 5, 5 );

	static final Color			CLRGRAY = Color.GRAY;
	static final Color			CLRLGRAY = Color.LIGHT_GRAY;
	private final Color			BOARD_CLR = new Color( 197, 213, 203 );
}
