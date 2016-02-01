package net.hardbird.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.hardbird.config.Config;
import net.hardbird.config.Images;

public class MenuPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel logoLabel;
	private MyButton newGamePanelButton, helpPanelButton, exitButton;
	public final static String newGameCommand = "NEW_GAME_OF_MENU_PANEL";
	public final static String helpCommand = "HELP_OF_MENU_PANEL";

	public MenuPanel(MainFrame mainFrame) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		this.initComponents(mainFrame);
	}

	private void initComponents(MainFrame mainFrame) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

		logoLabel = new JLabel();
		ImageIcon logo = new ImageIcon(Images.LOGO_IMG);
		logoLabel.setSize(logo.getIconWidth(), logo.getIconHeight());
		logoLabel.setIcon(logo);
		logoLabel.setOpaque(false);

		newGamePanelButton = new MyButton("New Game");
		newGamePanelButton.addActionListener(mainFrame);
		newGamePanelButton.setActionCommand(newGameCommand);
		newGamePanelButton.setOpaque(false);

		helpPanelButton = new MyButton("Help");
		helpPanelButton.addActionListener(mainFrame);
		helpPanelButton.setActionCommand(helpCommand);
		helpPanelButton.setOpaque(false);

		exitButton = new MyButton("Exit Game");
		exitButton.addActionListener(mainFrame);
		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		exitButton.setOpaque(false);

		JPanel logoPanel = new JPanel();
		logoPanel.add(logoLabel);
		logoPanel.setOpaque(false);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		Dimension panelSize = new Dimension(Config.MENU_PANEL_WIDTH, Config.MENU_PANEL_HEIGHT);
		buttonPanel.setSize(panelSize);
		buttonPanel.setPreferredSize(panelSize);

		GridLayout gridLayout = new GridLayout(3, 1, 0, 10);
		buttonPanel.setLayout(gridLayout);
		buttonPanel.add(newGamePanelButton);
		buttonPanel.add(helpPanelButton);
		buttonPanel.add(exitButton);
		buttonPanel.setOpaque(false);

		BorderLayout mainLayout = new BorderLayout();
		mainLayout.setVgap(25);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(mainLayout);

		mainPanel.add(logoPanel, BorderLayout.NORTH);
		mainPanel.add(buttonPanel, BorderLayout.CENTER);
		mainPanel.setOpaque(false);
		this.setOpaque(false);

		this.add(mainPanel);
		this.setOpaque(false);
	}
}
