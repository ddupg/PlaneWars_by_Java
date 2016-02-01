package net.hardbird;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import net.hardbird.ui.MainFrame;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException, UnsupportedAudioFileException, LineUnavailableException {
		MainFrame game = new MainFrame();
		game.loadMenuPanel();
	}

}
