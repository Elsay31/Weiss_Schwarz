package Field;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import CardAssociation.Card;
import Game.Player;

public class Random_Zone extends FieldElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4719315349186983715L;

	private Card thisCard;

	public Random_Zone(String imageFileName, int xa, int ya, Player player) {
		super(imageFileName, xa, ya, "Resolution Area", player);
		thisCard = null;
	}

	@Override
	public boolean containCards() {
		return thisCard != null;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			Card select = selectCard(e);
			if (select != null) {
				removeCard();
				associatedPlayer.getHand().setCard(select);
			}
		}
	}

	@Override
	public void paint(Graphics g, Card c) {
		if (containCards()) {
			showCard().setDisplay(true, false);
			showCard().toCanvas().setLocation(x, y);
			showCard().toCanvas().paint(g);
			System.out.println("RESOLUTION: " + thisCard.getCardName());
		} else {
			g.drawImage(bi, x, y, null);
		}
		g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
		g.setColor(Color.BLUE);

		g.drawString(zoneName, x + 10, y + 20);
	}

	public Card removeCard() {
		Card tempCard = thisCard;
		thisCard = null;
		return tempCard;
	}

	@Override
	public Card selectCard(MouseEvent e) {
		if (containCards()) {
			if (thisCard.getCardBound().contains(e.getX(), e.getY())) {
				Card tempCard = thisCard;
				if (e.getButton() == MouseEvent.BUTTON1) {
					removeCard();
				}
				return tempCard;
			}
		}
		return null;
	}

	@Override
	public void setCard(Card selectedCard) {
		thisCard = selectedCard;
	}

	@Override
	public Card showCard() {
		return thisCard;
	}

}
