package net.hardbird.entity;

import java.awt.Image;
import java.awt.Rectangle;

public abstract class Flyer extends GameObject {

	protected int speedX, speedY;
	protected int width, height;
	protected Image image;

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Rectangle getRectangle(){
		return new Rectangle(posX - width / 2, posY - height / 2, width, height);
	}

	public int getSpeedX() {
		return speedX;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public int getSpeedY() {
		return speedY;
	}

	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}

	public void move() {
		posX += speedX;
		posY += speedY;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
