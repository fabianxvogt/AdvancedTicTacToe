package MVC;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Class View
 * @author Fabian Vogt
 * describes the Tic Tac Toe UI
 */
@SuppressWarnings("serial")
public class View extends JFrame {
	private static final String TITLE = "Tic-Tac-Toe";
	
	private GridLayout baseLayout = new GridLayout(1,2,10,10);
	private JPanel basePanel = new JPanel(baseLayout);
	
	private JPanel menuPanel;
	
	private GridLayout settingsLayout = new GridLayout(1,2, 10, 10);
	private JPanel settingsPanel = new JPanel(settingsLayout);
	
	private FlowLayout buttonsLayout = new FlowLayout(FlowLayout.CENTER, 10, 10);
	private JPanel buttonsPanel = new JPanel(buttonsLayout);
	
	private GridLayout sliderLayout = new GridLayout(1,2);
	private JPanel sliderPanel = new JPanel(sliderLayout);
	
	private GridLayout fieldLayout;
	private JPanel fieldPanel;
	
	private GridLayout playerLayout = new GridLayout(9,1,0,3);
	private JPanel player1Panel = new JPanel(playerLayout);
	private JPanel player2Panel = new JPanel(playerLayout);
	
	private JCheckBox cbxComputer1 = new JCheckBox("Computer");
	private JCheckBox cbxComputer2 = new JCheckBox("Computer");
	
	private JButton btnNewGame = new JButton("New Game");
	private JButton btnQuit = new JButton("Quit");
	
	private JLabel lblLengthSlider = new JLabel("Board length: " + DEFAULT_LENGTH);
	private JSlider sldSideLength = new JSlider(2,10,DEFAULT_LENGTH);
	
	private JLabel lblDiffSlider1 = new JLabel("Difficulty: " + DEFAULT_DIFFICULTY);
	private JLabel lblDiffSlider2 = new JLabel("Difficulty: " + DEFAULT_DIFFICULTY);
	private JSlider sldDifficulty1 = new JSlider(1, 12, DEFAULT_DIFFICULTY);
	private JSlider sldDifficulty2 = new JSlider(1, 12, DEFAULT_DIFFICULTY);
	
	private JLabel lblPlayer1 = new JLabel("Player 1");
	private JLabel lblPlayer2 = new JLabel("Player 2");
	private JLabel lblWinsPlayer1 = new JLabel("Wins: 0");
	private JLabel lblLossesPlayer1 = new JLabel("Losses: 0");
	private JLabel lblWinsPlayer2 = new JLabel("Wins: 0");
	private JLabel lblLossesPlayer2 = new JLabel("Losses: 0");
	private JLabel lblTiesPlayer1 = new JLabel("Ties: 0");
	private JLabel lblTiesPlayer2 = new JLabel("Ties: 0");
	
	private JTextField txtPlayer1Symbol = new JTextField();
	private JTextField txtPlayer2Symbol = new JTextField();
	private JTextField txtPlayer1Name = new JTextField("Player 1");
	private JTextField txtPlayer2Name = new JTextField("Player 2");
	
	private JButton[][] fieldButtons;
	
	private static final Font PLAYER_LBL_FONT = new Font(null, Font.ITALIC, 20);
	private static final Font BTN_FONT = new Font(Font.MONOSPACED, Font.BOLD, 20);
	private static final int DEFAULT_LENGTH = 4; 
	private static final int DEFAULT_DIFFICULTY = 4;
	
	private Controller controller;
	
	public static void main(String args[]) {
		View v = new View();
		v.init();
		v.display();
	}
	/**
	 * Create a new Tic Tac Toe view
	 */
	public View() {
	}
	/**
	 * initialize this view
	 */
	public void init() {
		this.setTitle(TITLE);
		this.setLocationRelativeTo(null);
		this.setSize(800, 420);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// initialize field & menu panel
		this.fieldLayout = new GridLayout(DEFAULT_LENGTH, DEFAULT_LENGTH, 5, 5);
		this.fieldPanel = new JPanel(fieldLayout);
		this.menuPanel = new JPanel();
		this.menuPanel.setLayout(new BoxLayout(this.menuPanel, BoxLayout.Y_AXIS));
		
		// Change appearance of UI elements
		this.modifyContent();
		
		// set controller
		this.controller = new Controller(this);
		// register Events
		this.registerEvents();
	
		// Build the UI
		this.build();
		
		// initialize new game & build game field
		this.initGame(DEFAULT_LENGTH);
		
		// add base panel
		this.add(this.basePanel);
		this.setContentPane(this.basePanel);

		// create borders
		this.createBorders();
	}
	/**
	 * makes the view visible
	 */
	public void display() {
		this.setVisible(true);
	}
	/**
	 * Registers all UI Events
	 */
	private void registerEvents() {
		// create change listener for slider changes
		ChangeListener cl = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				repaint();				
			}
		};
		// register events
		this.sldDifficulty1.addChangeListener(cl);
		this.sldDifficulty2.addChangeListener(cl);
		this.sldSideLength.addChangeListener(cl);
		this.btnNewGame.setActionCommand(Controller.CMD_NEW_GAME);
		this.btnNewGame.addActionListener(this.controller);
		this.btnQuit.setActionCommand(Controller.CMD_EXIT);
		this.btnQuit.addActionListener(this.controller);
		this.cbxComputer1.setActionCommand(Controller.CMD_COMPUTER1);
		this.cbxComputer1.addActionListener(this.controller);
		this.cbxComputer2.setActionCommand(Controller.CMD_COMPUTER2);
		this.cbxComputer2.addActionListener(this.controller);
	}
	/**
	 * changes the appearance of different UI elements
	 */
	private void modifyContent() {
		// Player 1 is Human Player by default, so turn off difficulty slider
		this.setDifficultyPlayer1Visible(false);
		// Player 2 is Computer by default, so set computer-checkbox = true
		this.cbxComputer2.setSelected(true);
		
		// Fonts & Colors
		this.lblPlayer1.setFont(PLAYER_LBL_FONT);
		this.lblPlayer2.setFont(PLAYER_LBL_FONT);
		this.btnNewGame.setFont(BTN_FONT);
		this.btnQuit.setFont(BTN_FONT);
		this.lblWinsPlayer1.setForeground(new Color(0, 100, 0));
		this.lblWinsPlayer2.setForeground(new Color(0, 100, 0));
		this.lblLossesPlayer1.setForeground(Color.red);
		this.lblLossesPlayer2.setForeground(Color.red);
		this.lblTiesPlayer1.setForeground(Color.blue);
		this.lblTiesPlayer2.setForeground(Color.blue);
		
		// Top menu button sizes
		this.btnNewGame.setPreferredSize(new Dimension(170,30));
		this.btnQuit.setPreferredSize(new Dimension(170,30));
		
		// symbol textboxes should not be longer than necessary;
		this.txtPlayer1Symbol.setDocument(new JTextFieldLimit(1));
		this.txtPlayer2Symbol.setDocument(new JTextFieldLimit(1));
		// initialize them with default symbols
		this.txtPlayer1Symbol.setText("X");
		this.txtPlayer2Symbol.setText("O");
	}
	/**
	 * Sets borders for different UI elements
	 */
	private void createBorders() {
		this.fieldPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		this.player1Panel.setBorder(BorderFactory.createLoweredBevelBorder());
		this.player2Panel.setBorder(BorderFactory.createLoweredBevelBorder());
		this.basePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}
	/**
	 * Builds the UI
	 */
	private void build() {
		// Build Player 1 menu
		this.player1Panel.add(this.lblPlayer1);
		this.player1Panel.add(this.lblWinsPlayer1);
		this.player1Panel.add(this.lblLossesPlayer1);
		this.player1Panel.add(this.lblTiesPlayer1);
		this.player1Panel.add(this.txtPlayer1Name);
		this.player1Panel.add(this.txtPlayer1Symbol);
		this.player1Panel.add(this.cbxComputer1);
		this.player1Panel.add(this.lblDiffSlider1);
		this.player1Panel.add(this.sldDifficulty1);		
		
		// Build Player 2 menu
		this.player2Panel.add(this.lblPlayer2);
		this.player2Panel.add(this.lblWinsPlayer2);
		this.player2Panel.add(this.lblLossesPlayer2);
		this.player2Panel.add(this.lblTiesPlayer2);
		this.player2Panel.add(this.txtPlayer2Name);
		this.player2Panel.add(this.txtPlayer2Symbol);
		this.player2Panel.add(this.cbxComputer2);
		this.player2Panel.add(this.lblDiffSlider2);
		this.player2Panel.add(this.sldDifficulty2);
		
		// Build top menu
		this.sliderPanel.add(this.lblLengthSlider);
		this.sliderPanel.add(this.sldSideLength);
		this.buttonsPanel.add(this.btnNewGame);
		this.buttonsPanel.add(this.btnQuit);
		this.buttonsPanel.add(this.lblLengthSlider);
		this.buttonsPanel.add(this.sldSideLength);
		this.buttonsPanel.add(this.sliderPanel);
		
		// Build player menu
		this.settingsPanel.add(this.player1Panel);
		this.settingsPanel.add(this.player2Panel);
		
		// Build menu
		this.menuPanel.add(this.buttonsPanel);
		this.menuPanel.add(this.settingsPanel);
		
		// Build base panel
		this.basePanel.add(this.menuPanel);
		this.basePanel.add(this.fieldPanel);
	}
	/**
	 * Initializes the game UI
	 */
	public void initGame(int sideLength) {
		this.fieldPanel.removeAll();
		this.fieldLayout.setRows(sideLength);
		this.fieldLayout.setColumns(sideLength);
		this.fieldButtons = new JButton[sideLength][sideLength];
		for(int i = 0; i < sideLength; i++) 
			for(int j = 0; j < sideLength; j++) {
				JButton b = new JButton();
				this.fieldPanel.add(b);
				b.setActionCommand("FIELD" + i + j);
				b.addActionListener(this.controller);
				b.setBorder(BorderFactory.createEtchedBorder());
				b.setFont(new Font(b.getFont().getName(), Font.BOLD, 120 - sideLength*10));
				this.fieldButtons[i][j] = b;
			}
		this.fieldPanel.revalidate();
		this.fieldPanel.repaint();
	}
	/**
	 * Sets a new symbol in the UI game field
	 */
	public void setFieldSymbol(int x, int y, char symbol) {
		this.fieldButtons[x][y].setText(String.valueOf(symbol));
		this.fieldPanel.revalidate();
		this.fieldPanel.repaint();
	}
	/**
	 * Repaints the view during runtime
	 * used to update slider labels on changes
	 */
	public void paint(Graphics g) {
		super.paint(g);
		int sideLength = this.getSideLength();
		// set maximum difficulty so that the game can still run without too much waiting
		int maxDifficulty = Math.min(15 - sideLength, sideLength*sideLength-sideLength);
		if (this.getDifficultyPlayer1() > maxDifficulty)
			this.sldDifficulty1.setValue(maxDifficulty);
		this.sldDifficulty1.setMaximum(maxDifficulty);
		if (this.getDifficultyPlayer2() > maxDifficulty)
			this.sldDifficulty2.setValue(maxDifficulty);
		this.sldDifficulty2.setMaximum(maxDifficulty);
		// modify slider labels
		this.lblDiffSlider1.setText("Difficulty: " + this.getDifficultyPlayer1());
		this.lblDiffSlider2.setText("Difficulty: " + this.getDifficultyPlayer2());
		this.lblLengthSlider.setText("Board length: " + this.getSideLength());
	}
	/**
	 * Updates the player statistics
	 * @param player1Wins No. of wins for player 1
	 * @param player2Wins No. of wins for player 2
	 * @param ties No. of ties
	 */
	public void updateStats(int player1Wins, int player2Wins, int ties) {
		this.lblTiesPlayer1.setText("Ties: " + ties);
		this.lblTiesPlayer2.setText("Ties: " + ties);
		this.lblWinsPlayer1.setText("Wins: " + player1Wins);
		this.lblLossesPlayer2.setText("Losses: " + player1Wins);
		this.lblWinsPlayer2.setText("Wins: " + player2Wins);
		this.lblLossesPlayer1.setText("Losses: " + player2Wins);
	}
	public char getPlayer1Symbol() {
		return this.txtPlayer1Symbol.getText().charAt(0);
	}
	public void setPlayer1Symbol(char symbol) {
		this.txtPlayer1Symbol.setText(String.valueOf(symbol));
	}
	public char getPlayer2Symbol() {
		return this.txtPlayer2Symbol.getText().charAt(0);
	}
	public String getPlayer1Name() {
		return this.txtPlayer1Name.getText();
	}
	public String getPlayer2Name() {
		return this.txtPlayer2Name.getText();
	}
	public boolean player1IsComputer() {
		return this.cbxComputer1.isSelected();
	}
	public boolean player2IsComputer() {
		return this.cbxComputer2.isSelected();
	}
	public int getSideLength() {
		return this.sldSideLength.getValue();
	}
	/**
	 * Sets the visibility of player 1 difficulty slider
	 */
	public void setDifficultyPlayer1Visible(boolean isVisible) {
		this.sldDifficulty1.setVisible(isVisible);
		this.lblDiffSlider1.setVisible(isVisible);
	}
	/**
	 * Sets the visibility of player 2 difficulty slider
	 */
	public void setDifficultyPlayer2Visible(boolean isVisible) {
		this.sldDifficulty2.setVisible(isVisible);
		this.lblDiffSlider2.setVisible(isVisible);
	}
	public int getDifficultyPlayer1() {
		return this.sldDifficulty1.getValue();
	}
	public int getDifficultyPlayer2() {
		return this.sldDifficulty2.getValue();
	}
	/**
	 * Class JTextFieldLimit
	 * @author Fabian Vogt
	 * used to limit the no. of characters in player symbol textfields to 1
	 */
	public class JTextFieldLimit extends PlainDocument {
		  private int limit;

		  JTextFieldLimit(int limit) {
		   super();
		   this.limit = limit;
		   }
		  /**
		   * Checks if the new String length does not exceed the limit 
		   */
		  public void insertString( int offset, String  str, AttributeSet attr ) throws BadLocationException {
		    if (str == null) return;
		    if ((getLength() + str.length()) <= limit) {
		      super.insertString(offset, str, attr);
		    }
		  }
		}
}
