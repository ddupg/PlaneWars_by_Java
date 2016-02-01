package net.hardbird.entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.util.LinkedList;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import net.hardbird.config.Config;
import net.hardbird.config.Images;
import net.hardbird.config.Sounds;
import net.hardbird.ui.GamePlayingPanel;
import net.hardbird.util.SoundPlayer;

public class Bomb extends GameObject {

	private GamePlayingPanel playingPanel;
	private int index, width, height;
	private Image images[];
	private SoundPlayer bombSoundPlayer;

	public Bomb(GamePlayingPanel playingPanel) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		this.playingPanel = playingPanel;
		index = 0;
		images = Images.BOMB_IMG;
		width = Config.BOMB_WIDTH;
		height = Config.BOMB_HEIGHT;
		bombSoundPlayer = new SoundPlayer(Sounds.BOMB_SOUND_PATH);
		bombSoundPlayer.play();
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		if (index == images.length) {
			LinkedList<Bomb> bombs = playingPanel.getBombs();
			synchronized (bombs) {
				bombs.remove(this);
				return;
			}
		}
		g2d.drawImage(images[index++], posX - width / 2, posY - height / 2, width, height, playingPanel);
	}
}
