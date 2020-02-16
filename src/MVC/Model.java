package MVC;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import Board.Board;
import Player.ComputerPlayer;
import Player.HumanPlayer;
import Player.Player;

/**
 * Class Model
 * @author Fabian Vogt
 * manages the game
 * controlled by Controller class
 */
public class Model {
	
	private Board board;
	private Player p1, p2, playerNextMove;
	
	private int[] nextMove = new int[2];
	private int moveCounter;
	
	/**
	 * Create a new game Mode
	 * @param player1Symbol Symbol of player 1
	 * @param player1Name Name of player 1
	 * @param player1isHuman Indicator Player 1 is a human
	 * @param player2Symbol Symbol of player 2
	 * @param player2Name Name of player 2
	 * @param player2isHuman Indicator Player 2 is a human
	 * @param sideLength Side length of game board
	 */
	public Model(char player1Symbol, String player1Name, boolean player1isHuman,
			     char player2Symbol, String player2Name, boolean player2isHuman, 
			     int sideLength ) {
		this.board = new Board(sideLength);
		this.p1 = Model.createPlayer(
						player1Symbol, player1Name, player1isHuman, 1);
		this.p2 = Model.createPlayer(
						player2Symbol, player2Name, player2isHuman, 2);
		this.playerNextMove = this.p1;		
		this.moveCounter = 0;
	}
	/**
	 * Create a new player object 
	 * @param symbol Symbol of player
	 * @param name Name of player
	 * @param isHuman Indicator Player is a human
	 * @param playerNo No of Player (1 or 2)
	 * @return new Player
	 */
	public static Player createPlayer(char symbol, String name, boolean isHuman, int playerNo) {
		if (isHuman == true)
			return new HumanPlayer(symbol,name, playerNo);
		else
			return new ComputerPlayer(symbol, name, playerNo);
	}
	/**
	 * Evaluate a winning symbol for a given player 
	 * @param player Player 
	 * @param symbol Winning symbol
	 * @return score
	 */
    private int evaluateSymbol(Player player, char symbol) {
    	if (player.getSymbol() == symbol)
    		return 1;
    	if (this.getOpponent(player).getSymbol() == symbol)
    		return -1;
    	return 0;
    }
    /**
     * @param x X coordinate of Move
     * @param y Y coordinate of Move
     * @return new symbol
     */
    public Character move(int x, int y) {
    	if (this.board.isOccupied(x, y))
    		return null;
    	char symbol = this.playerNextMove.getSymbol();
    	this.board.setSymbolAt(x, y, symbol);
    	this.playerNextMove = this.getOpponent(this.playerNextMove);
    	this.moveCounter++;
    	return symbol;
    }
	public Player nextMovePlayer() {
		return this.playerNextMove;
	}	
	public boolean player1isNext() {
		return this.playerNextMove == this.p1;
	}
	/**
	 * Determines the winner based on current board state 
	 * @return Player who has won
	 */
	public Player getWinner() {
		char symbol = this.board.getWinSymbol();
		if (symbol == this.p1.getSymbol())
			return p1;
		if (symbol == this.p2.getSymbol())
			return p2;
		return null;
	}
	/**
	 * If the board is full and there is no winner, its a tie
	 * @return Is tie
	 */
	public boolean isTie() {
		return this.board.isFull();
	}
	/**
	 * returns the opponent for a given Player 
	 * @param player Player
	 * @return Opponent
	 */
	public Player getOpponent(Player player) {
		if (player.getSymbol() == this.p1.getSymbol())
			return this.p2;
		if (player.getSymbol() == this.p2.getSymbol())
			return this.p1;
		return null;		
	}
	/**
	 * Minimax algorithm
	 * determines the next best move for the computer 
	 * @param board Current board state
	 * @param player Maximizing player
	 * @param depth Depth (difficulty)
	 * @param initDepth Initial depth 
	 * @param a Alpha pruning
	 * @param b Beta pruning
	 * @return Score of the best Move
	 */
	private int miniMax(Board board, List<int[]> moves, Player player, int depth, int initDepth, int a, int b) {
    	int maxScore, score;
	    char winSymbol = board.getWinSymbol();
	    if (winSymbol != Board.EMPTY || depth == 0 || board.isFull()) 
	    	return evaluateSymbol(player, winSymbol);
	    
	    maxScore = -99;
	    for (int i = 0; i < moves.size(); i++) {
	    	int x = moves.get(i)[0];
	    	int y = moves.get(i)[1];
	        board.setSymbolAt(x, y, player.getSymbol());
	        moves.remove(i);
	        score = (-1)* ((this.miniMax(
	        		board, moves, this.getOpponent(player), depth-1, initDepth, -b, -a)));
	        moves.add(i, new int[] {x, y});
	        board.removeSymbolAt(x, y);
	        if (score > maxScore) {
	        	maxScore = score;
	        	if (depth == initDepth) {
	        		this.nextMove[0] = x;
	        		this.nextMove[1] = y;
	        	}
	        }
	        a = Integer.max(a, score);
	        if (a >= b) 
	        	return a;
	    }
	    return maxScore;
	}
	/**
	 * returns the next best Move based on given difficulty
	 * @param difficulty Difficulty
	 * @return next best move for computer
	 */
	public int[] getNextMove(int difficulty) {
		// add a random move for more fun
		if (this.moveCounter < 2 && this.board.getSideLength() > 3)
			return this.randomMove();
		// get possible moves
		List<int[]> possibleMoves = this.board.getEmptyFields();
		// randomize them
		Collections.shuffle(possibleMoves);
		this.miniMax(
				this.board, possibleMoves, this.playerNextMove, difficulty, difficulty, -99, 99);
		return this.nextMove;
	}
	/**
	 * returns a random move
	 * @return random move
	 */
	private int[] randomMove() {
		int x = ThreadLocalRandom.current().nextInt(0, this.board.getSideLength());
		int y = ThreadLocalRandom.current().nextInt(0, this.board.getSideLength());
		if (this.board.isOccupied(x, y))
			return randomMove();
		return new int[] {x, y};
	}
}
