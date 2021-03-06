package gameField;

// Stock_Zone field display information

import gamePlay.Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import deckComponents.*;


public class Stock_Zone extends Zone {

	/**
	 * 
	 */
	private static final long serialVersionUID = 984526118697487778L;

	private ArrayList<Card> stockZone;

	public Stock_Zone(String imageFileName, int xa, int ya, Player player, FieldZone zone) {
		super(imageFileName, xa, ya, zone, player);

		stockZone = new ArrayList<Card>();
	}

	// add cards to the end of the stock pile
	public void setCard(Card c) {
		c.setCurrentState(State.FD_REST);
		stockZone.add(c);
	}

	// remove a selected card in the stock pile
	public Card removeCard(Card c) {
		if (stockZone.remove(c))
			return c;
		return null;
	}

	// get the top card of the stock pile
	public Card showCard() {
		if (containCards())
			return stockZone.get(stockZone.size() - 1);
		return null;
	}

	@Override
	// check if the stock zone is empty or not
	public boolean containCards() {
		return stockZone.size() > 0;
	}

	@Override
	// draw all the visible elements of stock zone
	public void paint(Graphics g, Card c) {
		if (showCard() != c && showCard() != null) {
			// System.err.println("painting " + getLast().toString() + "....");
			// showCard().paint(g, this.x, this.y, false, true);
			showCard().setDisplay(false, true);
			showCard().toCanvas().setLocation(x, y);
			showCard().toCanvas().paint(g);
		} else {
			g.drawImage(bi, x, y, null);
		}
		g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
		g.setColor(Color.BLUE);

		// g.drawString(zoneName, x + 10, y + 20);
		g.drawString("Stock size: " + stockZone.size() + "", this.x, this.y - 10);
	}

	// @Override
	// select the top card in the stock zone
	// public Card selectCard(MouseEvent e) {
	// if (containCards()) {
	// if (showCard().getCardBound().contains(e.getX(), e.getY())) {
	// Card c = showCard();
	// stockZone.remove(stockZone.size() - 1);
	// return c;
	// }
	// }
	// return null;
	// }

	@Override
	// right click to pay stock for ability activation/cost to summon
	// left click to pay the bottom
	public void mouseReleased(MouseEvent e) {
		Card card = selectCard(e);
		if (containCards() == false || card == null)
			return;
		if (e.getButton() == MouseEvent.BUTTON3) {
			constructPopup(e);
		} else if (e.getButton() == MouseEvent.BUTTON1) {

		}
	}

	public boolean isList() {
		return true;
	}

	public int getCount() {
		return stockZone.size();
	}

	@Override
	protected void constructPopup(MouseEvent e) {
		final JPopupMenu popmenu = new JPopupMenu();

		JMenuItem stockTop = new JMenuItem("pay stock");
		stockTop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				associatedPlayer.getField().getWaitingRoom().setCard(stockZone.get(stockZone.size() - 1));
				stockZone.remove(stockZone.size() - 1);
				associatedPlayer.getField().repaintElements();
			}
		});
		popmenu.add(stockTop);

		JMenuItem stockBot = new JMenuItem("remove bottom");
		stockBot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				associatedPlayer.getField().getWaitingRoom().setCard(stockZone.get(0));
				stockZone.remove(0);
				associatedPlayer.getField().repaintElements();
			}
		});
		popmenu.add(stockBot); 

		popmenu.show(e.getComponent(), e.getX(), e.getY());
	}
}
