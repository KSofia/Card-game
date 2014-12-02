package cardgame;

import java.applet.Applet;
import java.awt.Image;

public class Card {
	private Image image;
	private String number;
	private String color;
	private String shape;
	private String fill;
	private boolean inPlay;
	private boolean isSelected;
	
	public boolean isInPlay() {
		return inPlay;
	}

	public void setInPlay(boolean inPlay) {
		this.inPlay = inPlay;
	}

	public Image getImage() {
		return image;
	}
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	public String getFill() {
		return fill;
	}

	public void setFill(String fill) {
		this.fill = fill;
	}

	public Card(Image image, String number, String color, String shape, String fill) {
		super();
		this.image = image;
		this.number = number;
		this.color = color;
		this.shape = shape;
		this.fill = fill;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
