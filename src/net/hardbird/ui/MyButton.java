package net.hardbird.ui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;

import net.hardbird.config.Config;
import net.hardbird.config.Images;
import net.hardbird.config.Sounds;
import net.hardbird.util.SoundPlayer;

public class MyButton extends JButton implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String text;
	private String buttonStatus;
	private String buttonNormalStatus = "BUTTON_NORMAL", buttonHoverStatus = "BUTTON_HOVER";
	private SoundPlayer buttonSoundPlayer;

	public MyButton(String text) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		super();
		this.text = text;
		initButton();
	}

	public MyButton() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		super();
		initButton();
	}

	private void initButton() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		this.buttonStatus = buttonNormalStatus;
		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.setContentAreaFilled(false);
		this.addMouseListener(this);
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		buttonSoundPlayer = new SoundPlayer(Sounds.BUTTON_SOUND_PATH);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);

		Image buttonImage = null;
		if (buttonStatus == buttonNormalStatus) {
			buttonImage = Images.BUTTON_NORMAL;
		} else {
			buttonImage = Images.BUTTON_HOVER;
		}
		int width = Config.BUTTON_WIDTH, height = Config.BUTTON_HEIGHT;
		this.setSize(width, height);
		this.setPreferredSize(new Dimension(width, height));

		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(buttonImage, 0, 0, width, height, this);
		FontMetrics metric = g.getFontMetrics();
		Rectangle2D rect = metric.getStringBounds(text, g);
		g2d.drawString(text, (float) (width / 2 - rect.getWidth() / 2),
				(float) ((height / 2) + ((metric.getAscent() + metric.getDescent()) / 2 - metric.getDescent())));

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		buttonStatus = buttonHoverStatus;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		buttonStatus = buttonHoverStatus;
		buttonSoundPlayer.play();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		buttonStatus = buttonNormalStatus;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		buttonStatus = buttonHoverStatus;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		buttonStatus = buttonHoverStatus;
	}

}
