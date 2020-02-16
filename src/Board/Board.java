package Board;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Board
 * @author Fabian Vogt
 * Describes a Tic Tac Toe board with variable size
 */
public class Board {
	private char[][] data;
	private int sideLength;
	public static final char EMPTY = '-';
	public Board() {};
	/**
	 * Creates a new game board
	 * @param sideLength Side length of board
	 * @throws IllegalArgumentException If side length is invalid
	 */
	public Board(int sideLength) throws IllegalArgumentException {
		try {
			this.init(sideLength);
		} catch (IllegalArgumentException e) {
			throw e;
		}
		this.sideLength = sideLength;
	}
	/**
	 * Initialize the board
	 * @param sideLength Side length of board
	 * @throws IllegalArgumentException If side length is invalid
	 */
	public void init(int sideLength) throws IllegalArgumentException {
		if (sideLength < 2) 
			throw new IllegalArgumentException("Invalid length! Must be >1");
		this.data = new char[sideLength][sideLength];
		for(int i = 0; i < sideLength; i++)
			for(int j = 0; j < sideLength; j++)
				this.data[i][j] = Board.EMPTY;
	}
	/**
	 * Check if a gives field of the board is occupied
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return Indicator is occupied
	 */
	public boolean isOccupied(int x, int y) {
		if (this.data[x][y] != EMPTY)
			return true;
		return false;
	}
	/**
	 * Sets a new symbol at a given position
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param symbol Symbol to be set
	 * @return Was successfull
	 */
	public boolean setSymbolAt(int x, int y, char symbol) {
		if(x < 0 || x+1 > this.sideLength ||
		   y < 0 || y+1 > this.sideLength )
			return false;
		this.data[x][y] = symbol;
		return true;
	}
	public char getSymbolAt(int x, int y) {
		return this.data[x][y];
	}
	/**
	 * removes a symbol (used to simulate game states in miniMax)
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	public void removeSymbolAt(int x, int y) {
		this.data[x][y] = EMPTY;
	}
	/**
	 * Checks if the board is full
	 * @return is Full
	 */
	public boolean isFull() {
		for(int i = 0; i < this.sideLength; i++)
			for(int j = 0; j < this.sideLength; j++)
				if (this.data[i][j] == Board.EMPTY)
					return false;
		return true;
	}
	/**
	 * Returns winner symbol (if there is a winner)
	 * @return Symbol
	 */
	public char getWinSymbol() {
		// Check rows
		for(int i=0; i<this.getSideLength(); i++) { // check rows
			char symbol = this.getSymbolAt(i, 0);
			if(symbol == Board.EMPTY)
				continue;
			for(int j=1; j<this.getSideLength(); j++) {
				if(symbol != this.getSymbolAt(i, j))
					break;
				if(j == this.getSideLength()-1)
					// row win
					return symbol;
			}
		}
		// Check cols
		for(int i=0; i<this.getSideLength(); i++) { // check rows
			char symbol = this.getSymbolAt(0, i);
			if(symbol == Board.EMPTY)
				continue;
			for(int j=1; j<this.getSideLength(); j++) {
				if(symbol != this.getSymbolAt(j, i))
					break;
				if(j == this.getSideLength()-1)
					// row win
					return symbol;
			}
		}
		// check first diagonal
		char symbol = this.getSymbolAt(0, 0);
		for(int i=1; i<this.getSideLength(); i++) {
			if(symbol != this.getSymbolAt(i, i))
				break;
			if(i == this.getSideLength()-1)
				return symbol;
		}
		// check second diagonal
		symbol = this.getSymbolAt(0, this.getSideLength()-1);
		for(int i=1; i<this.getSideLength(); i++) {
			if(symbol != this.getSymbolAt(i, this.getSideLength()-1 - i))
				break;
			if(i == this.getSideLength()-1)
				return symbol;
		}	
		return EMPTY;
	}
	/**
	 * Returns all empty fields
	 * @return Empty fields
	 */
	public List<int[]> getEmptyFields() {
		List<int[]> empty = new ArrayList<int[]>();
		for(int i=0; i<this.getSideLength(); i++) {
			for(int j=0; j<this.getSideLength(); j++) {
				if(!this.isOccupied(i, j))
					empty.add(new int[] {i, j});
			}
		}
		return empty;
	}
	public int getSideLength() {
		return sideLength;
	}
}
