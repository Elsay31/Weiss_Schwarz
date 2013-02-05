/**
 * @file Player.java
 * @author Jia Chen
 * @date Sept 06, 2011
 * @description 
 * 		Player.java is used to keep track of player information in the
 * 		game. It also keeps track of the player records.
 */

package Game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import CardAssociation.Card;
import CardAssociation.Deck;
import DeckBuilder.BuilderGUI;
import Field.Hand;
import Field.NewMainField;

public class Player implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8938420254001640004L;
	public Phase currentPhase;
	private Hand hand;
	public NewMainField field;
	private int playerID;

	private ArrayList<String> playerDecks;
	public String selectedDeck;
	public Deck currentDeck;
	public HashMap<String, Card> dictionary;

	public JFrame userFrame;
	public JTabbedPane tabbedPane;
	public JPanel deckPane;
	public JButton playGame;

	private BuilderGUI builderGui = null;
	private int sessionID;
	private boolean inGame;

	public JFrame f;
	private boolean loggedOut;
	private Game currentGame = null;

	// private Connector connector;

	// Player Constructor initializes all UI fields
	public Player() {
		playerID = (int) (Math.random() * 100);
		userFrame = new JFrame("Joe" + playerID);
		tabbedPane = new JTabbedPane();
		deckPane = new JPanel();
		loggedOut = true;

		selectedDeck = "";

		playGame = new JButton("Find Game");
		playGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!selectedDeck.isEmpty()) {
					findGame();
					initField();
					//setReady();
				}
			}

		});

		userFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sessionID = -1;

		playerDecks = new ArrayList<String>();
		// connector = new Connector("Cyber", 5000);
		deserializer();
	}

	// for Game.java to specify what game phase the player is at
	public void setCurrentPhase(Phase nextPhase) {
		currentPhase = nextPhase;
	}

	// to retrieve the current phase information of the player
	public Phase getCurrentPhase() {
		return currentPhase;
	}

	// set the player's hand
	public void setHand(Hand hand) {
		this.hand = hand;
	}

	// get the player's hand information
	public Hand getHand() {
		return hand;
	}

	// display and choose all the valid decks that the player has build
	public void displayDecks() {
		// display all decks in the "Deck" directory
		File file = new File("Deck");
		if (file.isDirectory()) {
			String titles[] = file.list();
			for (int i = 0; i < titles.length; i++) {
				playerDecks.add(titles[i]);
			}
		} else {
			System.out.println("Not A Directory");
		}

		// selecting a deck
		for (String deckTitle : playerDecks) {
			final JButton deckSelect = new JButton(deckTitle);
			deckSelect.addActionListener(new ActionListener() {

				@SuppressWarnings("deprecation")
				@Override
				public void actionPerformed(ActionEvent e) {
					selectedDeck = deckSelect.getLabel();
					System.out.println("Deck Selected : " + selectedDeck);
					// setReady();
					// startDeckEdit();
				}

			});
			deckPane.add(deckSelect);
			System.out.println(playerID + " : " + deckTitle);
		}

		final JButton newDeck = new JButton("New_Deck");
		newDeck.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				startDeckEdit();
			}

		});
		deckPane.add(newDeck);
	}

	private void findGame() {
		// TODO Auto-generated method stub

	}

	public void initField() {
		// displayDecks();

		System.err.println("loading....");

		// loading the deck
		currentDeck = new Deck();
		currentDeck.loadRaw(new File("Deck/" + selectedDeck), dictionary);
		System.out.println(selectedDeck + " has " + currentDeck.getPlayingDeck().size() + " cards");

		// Create Field Start
		URL imageSrc = null;
		try {
			imageSrc = ((new File("FieldImages/Background.png")).toURI())
					.toURL();
		} catch (MalformedURLException e) {
		}

		field = new NewMainField(imageSrc, this);

		field.prepare(currentDeck);
		// Create Field End

		// Create Hand given Field information
		
		hand = new Hand("FieldImages/Vertical.png", 50, 850, this);
		userFrame.add(hand);
		// System.out.println("should say match game " + sessionID);
		//
		// Runner.connector.messenger("match game " + sessionID, this);

		// Allow field to see hand

		// Create Game given player information
		setReady();
		System.out.println(playerID + " is ready");
	}

	private boolean ready = false;

	public boolean isReady() {
		return ready;
	}

	private void setReady() {
		ready = true;
	}

	public void buildGame() {
		System.out.println("Building game....");
		f = new JFrame("Weiss Schwarz " + playerID);
		if (currentGame == null) {
			currentGame = new Game(this);
			currentGame.testGame();
		} else {
			System.out.println(currentGame.getPlayersID());
			currentGame.playGame();
		}

		f.add(currentGame, BorderLayout.NORTH);
		f.pack();
		f.setVisible(true);
	}

	public NewMainField getField() {
		return field;
	}

	public String getSelectedDeck() {
		return selectedDeck;
	}

	@SuppressWarnings("unchecked")
	private void deserializer() {

		FileInputStream fileInput;
		ObjectInputStream objectInput;

		try {
			fileInput = new FileInputStream("CardDatav2");
			objectInput = new ObjectInputStream(fileInput);

			objectInput.readObject();
			dictionary = (HashMap<String, Card>) objectInput.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void buildAndDisplay() {
		deckPane.setSize(new Dimension(500, 500));

		deckPane.setLayout(new GridLayout(3, 5));
		displayDecks();

		tabbedPane.addTab("Game", playGame);
		tabbedPane.addTab("Deck", deckPane);

		Box box = Box.createVerticalBox();
		box.setAlignmentX(Box.CENTER_ALIGNMENT);
		box.add(tabbedPane);

		userFrame.setContentPane(box);

		userFrame.pack();
		userFrame.setVisible(true);
	}

	private void startDeckEdit() {
		if (builderGui == null) {
			builderGui = new BuilderGUI();
			// builderGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			builderGui.init();
			// builderGui.pack();
			builderGui.setVisible(true);
		}
		// builderGui.loadDefaultDeck(selectedDeck);
	}

	public void setSessionID(int playerCount) {
		sessionID = playerCount;
		System.out.println("my session id is " + sessionID);
	}

	// get the player's session id
	public int getSessionID() {
		return sessionID;
	}

	// check to see if the player is logged in
	public boolean getLogedOut() {
		return loggedOut;
	}

	public void setLogedOut() {
		loggedOut = !loggedOut;
	}

	// check to see if player is already playing a game
	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean b) {
		inGame = b;
	}

	public Player clone() {
		return this;
	}

	public void setGame(Game game) {
		currentGame = game;
		// currentGame.setFirst(this);
		System.out.println("set game into game");
		refresh();
	}

	private void refresh() {
		if (currentGame != null) {
			playerDecks.clear();
			deckPane.removeAll();
			deckPane.validate();
			tabbedPane.removeAll();
			tabbedPane.validate();

			displayDecks();

			playGame = new JButton("Play Game");
			playGame.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (!selectedDeck.isEmpty()) {
						initField();
					}
				}

			});

			tabbedPane.addTab("Game", playGame);
			tabbedPane.addTab("Deck", deckPane);
		}
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	public int getPlayerID() {
		return playerID;
	}

}