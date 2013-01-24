package Field;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import CardAssociation.Card;
import Game.Player;

abstract class FieldElement extends Component implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8801519680358307435L;

	protected BufferedImage bi;
	protected Rectangle rect;
	protected int w, h, x, y; // x, y coordinates of the displayed image and height and width of the image
	public String zoneName;
	protected Player associatedPlayer;

	/**
	 * A basic FieldElement constructor
	 * 
	 * @param imageFileName
	 *            relative URL of the background image
	 * @param xa
	 *            x-axis coordinate on the field
	 * @param ya
	 *            y-axis coordinate on the field
	 */
	public FieldElement(String imageFileName, int xa, int ya, String zone,
			Player player) {
		URL imageSrc = null;
		addMouseListener(this);
		setAssociatedPlayer(player);
		zoneName = zone;

		try {
			imageSrc = ((new File(imageFileName)).toURI()).toURL();
		} catch (MalformedURLException e) {
		}
		x = (int) (xa * Game.Game.gameScale);
		y = (int) ((ya + Game.Game.translatedY) * Game.Game.gameScale);
		try {
			
			BufferedImage before = ImageIO.read(imageSrc);
			int wid = before.getWidth();
			int hit = before.getHeight();
			bi = new BufferedImage(wid, hit, BufferedImage.TYPE_INT_ARGB);
			
			AffineTransform at = new AffineTransform();
			at.scale(Game.Game.gameScale, Game.Game.gameScale);
			AffineTransformOp scaleOp = 
			   new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
			bi = scaleOp.filter(before, null);
			
			w = (int) (bi.getWidth(null));
			h = (int) (bi.getHeight(null));
			/*
			 * rect = new Rectangle((int) (x), (int) (y +
			 * Game.Game.translatedY), (int) (w * Game.Game.gameScale), (int) (h
			 * * Game.Game.gameScale));
			 */
			rect = new Rectangle((int) (x), (int) (y), (int) (w), (int) (h));
		} catch (IOException e) {
			System.out.println("Image could not be read");
			System.exit(1);
		}
	}

	/**
	 * @return the Dimension of the FieldElement
	 */
	public Dimension getPreferredSize() {
		return new Dimension(w, h);
	}

	/**
	 * * check if the zone contains cards
	 * 
	 * @return
	 */
	public abstract boolean containCards();
	
	
	public boolean contains(int x, int y) {
		return rect.contains(x, y);
	}

	/**
	 * @param g
	 *            draw the FieldElement
	 */
	/*
	 * public void paint(Graphics g, Card topCard) {
	 * 
	 * // System.out.println(zoneName + " contains card = " + containCards());
	 * 
	 * if (containCards()) { paint(g, topCard); } else { g.drawImage(bi, x, y,
	 * null); } g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
	 * g.setColor(Color.BLUE);
	 * 
	 * g.drawString(zoneName, x + 10, y + 20); }
	 */

	public void paintOption(Graphics g) {
		System.out.println("Climax drawing option");
		g.drawString("option 1", x, y - 10);
		g.setColor(Color.LIGHT_GRAY);
		// g.drawRect(this.x, y - 10, 150, 20);
		g.fillRect(x, y - 10, 150, 20);
		g.setColor(Color.BLUE);
	}

	/**
	 * draw the cards in the zone
	 * 
	 * @param g
	 * @param x
	 * @param y
	 */
	public abstract void paint(Graphics g, Card c);

	// public abstract void addCard(Card c);
	//
	// public abstract ArrayList<Card> showCards();
	//
	// public abstract Card removeCard(Card c);

	/*
	 * public boolean snapToZone(Rectangle r) {
	 * 
	 * if (r.intersects(rect)) { r.setLocation(x, y); return true; }
	 * 
	 * return false; }
	 */

	public boolean snapToZone(Card c) {

		if (c.getCardBound().intersects(rect)) {
			c.getCardBound().setLocation(x, y);
			System.out.println("Intersect " + zoneName);
			return true;
		}

		return false;
	}

	@Override
	public abstract void mouseClicked(MouseEvent e);

	@Override
	public void mouseEntered(MouseEvent e) {
		if (rect.contains(e.getX(), e.getY()))
			System.out.println("FieldElement mouseEntered " + zoneName);
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	public abstract Card selectCard(MouseEvent e);

	public abstract void setCard(Card selectedCard);

	public abstract Card showCard();

	public String toString() {
		if (rect != null)
			System.out.println("hitbox ("
					+ rect.x + " ~ " + rect.getWidth() + ", " + rect.y  + " ~ " + rect.getHeight() + ")");
		return zoneName;
	}

	public void setAssociatedPlayer(Player associatedPlayer) {
		this.associatedPlayer = associatedPlayer;
	}

	public Player getAssociatedPlayer() {
		return associatedPlayer;
	}

	public boolean isList() {
		return false;
	}
}
