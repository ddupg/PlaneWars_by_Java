package net.hardbird.ui;

import java.awt.Dimension;

import javax.swing.JPanel;

import net.hardbird.config.Config;

public class HelpPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HelpPanel() {
		this.initComponents();
	}

	private void initComponents() {
		this.setSize(Config.GAME_WINDOW_WIDTH, Config.GAME_WINDOW_WIDTH);
		this.setPreferredSize(new Dimension(Config.GAME_WINDOW_WIDTH, Config.GAME_WINDOW_HEIGHT));

		this.setOpaque(false);
	}

}
