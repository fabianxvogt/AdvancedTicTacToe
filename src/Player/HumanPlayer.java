package Player;

/**
 * Class Human Player
 * @author Fabian Vogt
 * Descibes a human Tic Tac Toe player
 */
public class HumanPlayer extends Player {
	/**
	 * create a new Human Player
	 * @param symbol Symbol of Player
	 * @param name Name of Player
	 * @param playerNo Player number (1 or 2)
	 */
	public HumanPlayer(char symbol, String name, int playerNo) {
		super(symbol, name, playerNo);
	}
	@Override
	public boolean isComputer() {
		return false;
	}
	@Override
	public boolean isHuman() {
		return true;
	}
}
