/* Coach's Java Programming FINAL Project
 * (Katarina) Sofia Hamrin
 * 5/07/14
 * D5
 * Game to find as many "sets" as possible by clicking on three cards in assortment of cards dealt to the screen
 */

package cardgame;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Button;
import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;


@SuppressWarnings("serial")
public class Game extends Applet implements MouseListener, ActionListener {
	// declare buffering/rendering image offscreen
	protected BufferedImage backImg;
	protected Image image, s1, s2, s3;
	private ArrayList<Card> cards = new ArrayList<Card>();
	private ArrayList<Card> dealt = new ArrayList<Card>();
	// creates strings to hold the 4 different types of card variables
	private String[] numbers = new String[] { "one", "two", "three" };
	private String[] colors = new String[] { "Red", "Green", "Purple" };
	private String[] shapes = new String[] { "Squiggles", "Ovals", "Triangles" };
	private String[] fills = new String[] { "Fill", "Striped", "Clear" };
	private String imgPath = "Images/";
	private Image set[];
	private int totalImages = 10, currentImage = 0, sleepTime = 1200;
	MediaTracker imageTracker;
	Timer myTimer;
	GameFrame myFrame;
	boolean alreadySelected = false;

	// int variable to keep track of how many cards the user has selected
	int selectedPos = 0;
	int rows = 5;
	int cols = 3;
	Rectangle[][] recs = new Rectangle[cols][rows];

	// point variable to keep track of index in the 2D rectangles
	Point[] selected = new Point[3];

	private AudioClip song1, song2;

	boolean Set = false;
	private Button addCardsButton, resetButton, howToPlay;
	private boolean demo = false;
	 
	public void update(Graphics g) {
		paint(g);
	}
	
	public void init() {
		
		myFrame = new GameFrame(this);
		myFrame.setSize(500, 100);
		myFrame.setVisible(true);
		
		imageTracker = new MediaTracker(this);
		set = new Image[totalImages];
		myTimer = new Timer(true);
		myTimer.schedule(new TimerTask() {
			public void run() {
				repaint();
			}
		}, 0, sleepTime);
		for (int i = 0; i < set.length; i++) {
			set[i] = getImage(getCodeBase(),"Animation/Set" + (i + 1) + ".JPG");
			imageTracker.addImage(set[i], i);
		}
		
		try {
			imageTracker.waitForID(0);
		} catch (InterruptedException e) {
		}
		
		setup_backgroundLayout();
		generateRects();
		addMouseListener(this);
		setupCards();
		
		backImg = new BufferedImage(getWidth(), getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		
		repaint();
	}

	private void setup_backgroundLayout() {
		this.setSize(500, 850);
		this.setLayout(null);
		setBackground(Color.black);
		
		song1 = getAudioClip(getDocumentBase(), "Sound/06 When It Falls.au");
		song2 = getAudioClip(getDocumentBase(), "Sound/04 Give It Away.au");

		this.addCardsButton = new Button("Add more cards");
		addCardsButton.addActionListener(this);
		add(addCardsButton);
		this.addCardsButton.setSize(110, 40);
		this.addCardsButton.setLocation(85, 600);

		this.resetButton = new Button("Reset");
		add(resetButton);
		resetButton.addActionListener(this);
		this.resetButton.setLocation(205, 600);
		this.resetButton.setSize(60, 40);

		this.howToPlay = new Button("Example Sets");
		howToPlay.addActionListener(this);
		add(howToPlay);
		this.howToPlay.setSize(110,40);
		this.howToPlay.setLocation(275, 600);
	}

	// generates rectangles to draw a border around cards
	private void generateRects() {
		int width = 105;
		int height = 72;
		for (int x = 0; x < cols; x++)
			for (int y = 0; y < rows; y++)
				recs[x][y] = new Rectangle(40 + x * (width + 40), 60 + y
						* (height + 40), width, height);
	}
	
	private void setupCards() {
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					for (int m = 0; m < 3; m++) {
						
						// creates a filename for each image
						String filename = numbers[i] + colors[j] + shapes[k]
								+ fills[m] + ".GIF";
						
						// gets image using filename and filepath
						image = getImage(getCodeBase(), imgPath + filename);
						
						// adds the image to cards in the arraylist
						cards.add(new Card(image, numbers[i], colors[j],
								shapes[k], fills[m]));
					}
				}
			}
		}
		
		// moving 12 cards from cards arraylist randomly and adding to dealt
		for (int i = 0; i < rows * cols; i++) {
			
			int index = (int) (Math.random() * cards.size());
			
			// gets index of these cards out of cards
			Card c = cards.get(index);
			
			// adds these cards to dealt arraylist
			dealt.add(c);
			c.setSelected(false);
			if (i > 11) {
				c.setInPlay(false);
			} else {
				c.setInPlay(true);
			}
			
			// removes index of cards in dealt from cards so same card
			// won't be drawn twice
			cards.remove(index);
		}
	}
	
	public void paint(Graphics g) {
		// saving reference of g to gBackup
		// changing the g reference to the offscreen image
		Graphics backImageGraphics = backImg.getGraphics();
		backImageGraphics.clearRect(0, 0, getWidth(), getHeight());

		backImageGraphics.setColor(Color.RED);
		Font FontTimesNewRoman22 = new Font("TimesNewRoman", Font.BOLD, 22);
		backImageGraphics.setFont(FontTimesNewRoman22);
		backImageGraphics.drawString("SET Game", 170, 30);
		backImageGraphics.setColor(Color.white);

		// draws rectangles in applet and gives them a 2D index (i.e. [0 2])
		for (int x = 0; x < cols; x++) {
			for (int y = 0; y < rows; y++) {
				int xLoc = recs[x][y].x;
				int yLoc = recs[x][y].y;
				int width = recs[x][y].width;
				int height = recs[x][y].height;

				// assigns the single index to new variable name
				int cardNum = getCardNumber(x, y);

				// gets index of card from dealt (to match the variables on face
				// of card)
				Card c = dealt.get(cardNum);

				if (c.isInPlay()) {
					if (c.isSelected())
						backImageGraphics.setColor(Color.yellow);
					else
						backImageGraphics.setColor(Color.black);
					// fills the rectangle in yellow if card is selected and
					// redraws in black if not
					backImageGraphics.fillRect(xLoc - 4, yLoc - 4, width - 1, height - 1);

				
					backImageGraphics.setColor(Color.white);
					backImageGraphics.drawRect(xLoc - 5, yLoc - 5, width, height);

					// draws images of cards in dealt
					backImageGraphics.drawImage(c.getImage(), xLoc, yLoc, this);
					
					// draws matched sets to area below
					if(Set==true){
						backImageGraphics.drawImage(s1, 80, 675, this);
						backImageGraphics.drawImage(s2, 185, 675, this);
						backImageGraphics.drawImage(s3, 290, 675, this);
					}
					else{
						backImageGraphics.setColor(Color.black);
					}
				}
			}
		}
		
		backImageGraphics.setColor(Color.yellow);
		
		if(demo) {			
			if (imageTracker.checkID(currentImage, true)) {
				backImageGraphics.drawImage(set[currentImage], 70, 750, this);	
				if (currentImage == 0){
					set[totalImages - 1].flush(); 
				}
				else{
					set[currentImage - 1].flush();
				}
				currentImage = ++currentImage % totalImages;
			} else
				postEvent(new Event(this, Event.MOUSE_ENTER, ""));
		}

		// draw the buffered image to original graphics object
		g.drawImage(backImg, 0, 0, null);
		// required disposal of graphics
		backImageGraphics.dispose();
	}
	
	public void start(Graphics g) {
		g.drawImage(set[0], 0, 0, this);
		currentImage = 1;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (addCardsButton.contains(e.getPoint())) {
		}

		for (int x = 0; x < cols; x++)
			for (int y = 0; y < rows; y++)

				// gets rectangle location from mouseclick and gets dealt index
				if (recs[x][y].contains(e.getPoint())
						&& dealt.get(getCardNumber(x, y)).isInPlay()) {

					// adds rectangle clicked to point variable
					Point cardPos = new Point(x, y);
					
					for (int i = 0; i < selectedPos; i++)
						if (selected[i].equals(cardPos)) {							
							alreadySelected = true;
							return;
						}

					if (selectedPos == 0)
						for (Card c : dealt)
							c.setSelected(false);
					dealt.get(getCardNumber(x, y)).setSelected(true);
					if(selectedPos<3){
						selected[selectedPos] = cardPos;
						selectedPos++;
						if (selectedPos == 3) {
							compareSelectedCards();
							selectedPos = 0;							
						}
					}
					else
						selectedPos = 0;
					repaint();
					return;
				}
	}

	// returns a single index to cards drawn in 2D array (going from 1,2 to 5)
	private int getCardNumber(int row, int col) {
		return row + col * cols;
	}
	private void compareSelectedCards() {
		// gets single card number and finds it's integer value in dealt
		int cardNum = getCardNumber(selected[0].x, selected[0].y);
		Card c1 = dealt.get(cardNum);

		cardNum = getCardNumber(selected[1].x, selected[1].y);
		Card c2 = dealt.get(cardNum);

		cardNum = getCardNumber(selected[2].x, selected[2].y);
		Card c3 = dealt.get(cardNum);

		int allTheSame = 0, allDifferent = 0;

		// compares three cards to see if they all have the same shape or all
		// have the different shape
		if (!(c1.getShape().equals(c2.getShape()))
				&& !(c1.getShape().equals(c3.getShape()))
				&& !(c2.getShape().equals(c3.getShape())))
			allDifferent++;
		else if ((c1.getShape().equals(c2.getShape()))
				&& (c1.getShape().equals(c3.getShape())))
			allTheSame++;

		// compares three cards to see if they all have the same fill or all
		// have the different fills...etc
		if (!(c1.getFill().equals(c2.getFill()))
				&& !(c1.getFill().equals(c3.getFill()))
				&& !(c2.getFill().equals(c3.getFill())))
			allDifferent++;
		else if ((c1.getFill().equals(c2.getFill()))
				&& (c1.getFill().equals(c3.getFill())))
			allTheSame++;

		if (!(c1.getNumber().equals(c2.getNumber()))
				&& !(c1.getNumber().equals(c3.getNumber()))
				&& !(c2.getNumber().equals(c3.getNumber())))
			allDifferent++;
		else if ((c1.getNumber().equals(c2.getNumber()))
				&& (c1.getNumber().equals(c3.getNumber())))
			allTheSame++;

		if (!(c1.getColor().equals(c2.getColor()))
				&& !(c1.getColor().equals(c3.getColor()))
				&& !(c2.getColor().equals(c3.getColor())))
			allDifferent++;
		else if ((c1.getColor().equals(c2.getColor()))
				&& (c1.getColor().equals(c3.getColor())))
			allTheSame++;

		// if all four attributes are all different
		// allDifferent = 4
		// if all four attributes are all the same
		// allTheSame = 4
		// if three attributes are all the same
		// allTheSame = 3

		if (allTheSame + allDifferent == 4) {
			Set = true;
			s1 = c1.getImage();
			s2 = c2.getImage();
			s3 = c3.getImage();

			// removes the 3 selected cards
			ArrayList<Card> toBeRemoved = new ArrayList<>();
			for (int i = 0; i < selected.length; i++) {
				Point p = selected[i];
				int index = getCardNumber(p.x, p.y);
				Card c = dealt.get(index);
				toBeRemoved.add(c);
			}
			dealt.removeAll(toBeRemoved);

			// add 3 new cards
			
			for (int i = 0; i < selected.length; i++) {
				int index = (int) (Math.random() * cards.size());
				Card c = cards.get(index);
					dealt.add(c);
					cards.remove(index);					
			}
			
			if(cards.size() < 3) {
				cards.clear();
				dealt.clear();
				setupCards();
			}
			// check to see if at the end of the deck
			// add the 3 statements underneath reset button


			// update the display to show the modified dealt cards
			repaint();

		} else {
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	public void playSound() {
		song1.play();
	}
	
	public void loopSound(){
		song2.stop();
		song1.loop();
	}
	
	public void stopSound(){
		song1.stop();
		song2.stop();
	}
	
	public void playSound2() {
		song2.play();
	}
	
	public void loopSound2() {
		song1.stop();
		song2.loop();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source == addCardsButton) {
			for (int i = 0; i < rows * cols; i++) {
				dealt.get(i).setInPlay(true);
			}
			
		} else if (source == resetButton) {
			cards.clear();
			dealt.clear();
			setupCards();
		}
		
		if(source == howToPlay){
			demo = !demo;
			repaint();
		}
	}
}
