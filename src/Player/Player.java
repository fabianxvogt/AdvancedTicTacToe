package Player;
/**
 * Class Player
 * @author Fabian Vogt
 * Describes a Tic Tac Toe player
 */
public abstract class Player {
	private char symbol;
	private String name;
	private int playerNo;
	public Player() {};
	/**
	 * creates a new Player
	 * @param symbol Symbol of Player
	 * @param name Name of Player
	 * @param playerNo Player Number (1 or 2)
	 */
	public Player(char symbol, String name, int playerNo) {
		this.setSymbol(symbol);
		this.setName(name);
		this.playerNo = playerNo;
	}
	public abstract boolean isComputer();
	public abstract boolean isHuman();
	public char getSymbol() {
		return symbol;
	}
	public void setSymbol(char symbol) {
		this.symbol = symbol;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPlayerNo() {
		return playerNo;
	}
	public String toString() {
		return this.name;
	}
}
