package MVC;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import Player.Player;

/**
 * Class Controller
 * @author Fabian Vogt
 * UI Event controller for Tic Tac Toe game
 */
public class Controller implements ActionListener{
	
	private View view;
	private Model model;
	
	public static final String CMD_NEW_GAME = "NEW";
	public static final String CMD_EXIT = "EXIT";
	public static final String CMD_COMPUTER1 = "COMP1";
	public static final String CMD_COMPUTER2 = "COMP2";
	public static final String CMD_PLAYER1_SYMBOL = "SYMBOL1";
	public static final String CMD_PLAYER2_SYMBOL = "SYMBOL2";
	
	private int tieCounter;
	private int player1WinsCounter;
	private int player2WinsCounter;
	
	/**
	 * Create a new event controller for a given view
	 * @param view
	 */
	public Controller(View view) {
		this.view = view;
		this.model = this.createNewGameModel();
		this.tieCounter = 0;
		this.player1WinsCounter = 0;
		this.player2WinsCounter = 0;
	}
	/**
	 * Event handling
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.contains("FIELD")) {
			Player nextMove = this.model.nextMovePlayer();
			if(nextMove.isComputer())
				return;
			int x = Integer.parseInt(cmd.substring(5, 6));
			int y = Integer.parseInt(cmd.substring(6, 7));
			Character symbol = this.model.move(x, y);
			if (symbol == null)
				return;
			this.view.setFieldSymbol(x, y, symbol);
			this.checkWinner();
			tryComputerMove();
		}
		
		if (cmd == CMD_NEW_GAME) {
			try {
				this.model = createNewGameModel();
			} catch (IllegalStateException ex) {
				JOptionPane.showMessageDialog(this.view, ex.getMessage());
				return;
			}
			this.view.initGame(this.view.getSideLength());
			this.tryComputerMove();
		}
		if (cmd == CMD_EXIT)
			System.exit(0);
		
		if (cmd == CMD_COMPUTER1) {
			if (this.view.player1IsComputer())
				this.view.setDifficultyPlayer1Visible(true);
			else
				this.view.setDifficultyPlayer1Visible(false);
		}
		if (cmd == CMD_COMPUTER2) {
			if (this.view.player2IsComputer())
				this.view.setDifficultyPlayer2Visible(true);
			else
				this.view.setDifficultyPlayer2Visible(false);
		}	
	}
	/*
	 * Check if there is winner (or if its a tie)
	 * and give respond message
	 */
	public void checkWinner() {
		Player winner = this.model.getWinner();
		if(winner != null) {
			if(winner.getPlayerNo() == 1)
				this.player1WinsCounter++;
			if(winner.getPlayerNo() == 2)
				this.player2WinsCounter++;
			this.view.updateStats(
					this.player1WinsCounter, this.player2WinsCounter, this.tieCounter);
			JOptionPane.showMessageDialog(this.view, winner.toString() + " has won.");
			return;
		}
		if(this.model.isTie()) {
			this.tieCounter++;
			this.view.updateStats(
					this.player1WinsCounter, this.player2WinsCounter, this.tieCounter);
			JOptionPane.showMessageDialog(this.view, "Tie");		
		}
	}
	/**
	 * try to execute a move for the computer
	 */
	public void tryComputerMove() {
		while (this.model.nextMovePlayer().isComputer()) {
			int diff;
			if (this.model.player1isNext())
				diff = this.view.getDifficultyPlayer1();
			else 
				diff = this.view.getDifficultyPlayer2();
			int[] move = model.getNextMove(diff);
			Character symbol = this.model.move(move[0], move[1]);
			if (symbol == null)
				return;
			this.view.setFieldSymbol(move[0], move[1], symbol);
			this.checkWinner();
		}
	}
	/**
	 * returns a new Tic Tac Toe game model
	 */
	public Model createNewGameModel() throws IllegalStateException{
		char s1 = this.view.getPlayer1Symbol();
		char s2 = this.view.getPlayer2Symbol();
		if (s1 == s2)
			throw new IllegalStateException("Symbols must be unique!");
		return new Model(
				s1, 
				this.view.getPlayer1Name(), 
				!this.view.player1IsComputer(),
				s2,
				this.view.getPlayer2Name(),
				!this.view.player2IsComputer(),
				this.view.getSideLength());
	}
}
