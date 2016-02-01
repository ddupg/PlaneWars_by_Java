package net.hardbird.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.hardbird.config.Config;
import net.hardbird.config.Images;
import net.hardbird.config.Sounds;
import net.hardbird.util.ImageLoader;
import net.hardbird.util.SoundPlayer;

public class MainFrame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MenuPanel menuPanel;
	private GamePlayingPanel gamePlayingPanel;
	private HelpPanel helpPanel;
	private ImageLoader imageLoader;

	public MainFrame() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		this.loadImage();
		this.loadSound();
		this.initComponents();
		this.setBackgroundImg();
	}

	private void initComponents() {
		this.setTitle("Air Wars");
		this.setSize(Config.GAME_WINDOW_WIDTH, Config.GAME_WINDOW_HEIGHT);
		Dimension screenSz = getToolkit().getScreenSize();
		this.setLocation((screenSz.width - this.getWidth()) / 2, (screenSz.height - this.getHeight()) / 2);
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void setBackgroundImg() {
		ImageIcon bgImgIcon = new ImageIcon(Images.BACKGROUND_IMG);
		JLabel bgLabel = new JLabel(bgImgIcon);
		this.getLayeredPane().add(bgLabel, new Integer(Integer.MIN_VALUE));
		bgLabel.setBounds(0, 0, bgImgIcon.getIconWidth(), bgImgIcon.getIconHeight());
		((JPanel) this.getContentPane()).setOpaque(false);
	}

	private void loadImage() throws IOException {
		Images.BACKGROUND_IMG = (new ImageLoader(Images.BACKGROUND_IMG_PATH)).getSubImage(0, 0,
				Config.GAME_WINDOW_WIDTH, Config.GAME_WINDOW_HEIGHT);
		Images.LOGO_IMG = (new ImageLoader(Images.LOGO_IMG_PATH)).getImage();
		Images.MYPLANE_IMG = (new ImageLoader(Images.MYPLANE_IMG_PATH)).getImage();

		imageLoader = new ImageLoader(Images.MY_BULLET_IMG_PATH);
		Images.MY_BULLET_IMG = imageLoader.getImage();

		imageLoader = new ImageLoader(Images.SMALL_PLANE_IMG_PATH);
		Images.SMALL_PLANE_IMG = imageLoader.getImage();
		Images.SMALL_PLANE_INVERTED_IMG = imageLoader.getRorateImage(180);

		Images.BIG_PLANE_IMG = new Image[Config.BIG_ENEMY_PLANE_TYPE_NUM];
		Images.BIG_PLANE_INVERTED_IMG = new Image[Config.BIG_ENEMY_PLANE_TYPE_NUM];
		for (int i = 0; i < 10; i++) {
			imageLoader = new ImageLoader("Resources/images/enemyplane" + (i + 1) + ".png");
			Images.BIG_PLANE_IMG[i] = imageLoader.getImage();
			Images.BIG_PLANE_INVERTED_IMG[i] = imageLoader.getRorateImage(180);
		}

		Images.BOSS_IMG = (new ImageLoader(Images.BOSS_IMG_PATH)).getImage();

		imageLoader = new ImageLoader(Images.SMALL_ENEMY_PLANE_BULLET_IMG_PATH);
		Images.SMALL_ENEMY_PLANE_BULLET_IMG = imageLoader.getImage();
		Images.SMALL_ENEMY_PLANE_BULLET_INVERTED_IMG = imageLoader.getRorateImage(180);

		imageLoader = new ImageLoader(Images.BIG_ENEMY_PLANE_BULLET_IMG_PATH);
		Images.BIG_ENEMY_PLANE_BULLET_IMG = imageLoader.getImage();
		Images.BIG_ENEMY_PLANE_BULLET_INVERTED_IMG = imageLoader.getRorateImage(180);

		imageLoader = new ImageLoader(Images.BOSS_BULLET_IMG_PATH);
		Images.BOSS_BULLET_IMG = imageLoader.getImage();
		Images.BOSS_BULLET_INVERTED_IMG = imageLoader.getRorateImage(180);

		Images.BOMB_IMG = new Image[Config.BOMB_IMG_NUM];
		for (int i = 0; i < Config.BOMB_IMG_NUM; i++) {
			Images.BOMB_IMG[i] = (new ImageLoader("Resources/images/bomb" + (i + 1) + ".png")).getImage();
		}

		Images.BLOOD_SUPPORT_IMG = new ImageLoader(Images.BLOOD_SUPPORT_IMG_PATH).getImage();
		Images.DOUBLE_BULLETS_SUPPORT_IMG = new ImageLoader(Images.DOUBLE_BULLETS_SUPPORT_IMG_PATH).getImage();

		Images.BUTTON_NORMAL = (new ImageLoader(Images.BUTTON_NORMAL_PATH)).getImage();
		Images.BUTTON_HOVER = (new ImageLoader(Images.BUTTON_HOVER_PATH)).getImage();
	}

	private void loadSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		Sounds.GAME_SOUND = new SoundPlayer(Sounds.GAME_SOUND_PATH);
		Sounds.GAME_OVER_SOUND = new SoundPlayer(Sounds.GAME_OVER_SOUND_PATH);
		Sounds.FIRE_SOUND = new SoundPlayer(Sounds.FIRE_SOUND_PATH);
		Sounds.BOMB_SOUND = new SoundPlayer(Sounds.BOMB_SOUND_PATH);

		Sounds.BUTTON_SOUND = new SoundPlayer(Sounds.BUTTON_SOUND_PATH);
	}

	public void loadMenuPanel() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		Container c = this.getContentPane();
		c.removeAll();
		this.repaint();
		if (this.menuPanel == null) {
			this.menuPanel = new MenuPanel(this);
		}
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		c.add(Box.createVerticalGlue());
		c.add(this.menuPanel);
		c.add(Box.createVerticalGlue());
		this.validate();
	}

	public void startGame()
			throws InterruptedException, UnsupportedAudioFileException, IOException, LineUnavailableException {
		Container c = this.getContentPane();
		c.removeAll();
		this.repaint();
		this.gamePlayingPanel = new GamePlayingPanel();
		c.setLayout(new BorderLayout());
		c.add(gamePlayingPanel, BorderLayout.CENTER);
		gamePlayingPanel.requestFocus();
		gamePlayingPanel.startGame();
		Sounds.GAME_SOUND.play();
		Sounds.GAME_SOUND.loop();
		while (gamePlayingPanel.isPlaying()) {
			Thread.sleep(Config.GAME_PANEL_REPAINT_INTERVAL);
		}
		stopSound();
		Sounds.GAME_OVER_SOUND.play();
		int option = JOptionPane.showConfirmDialog(this, "Game Over! Play Again?", "Game Over",
				JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			startGame();
		} else {
			loadMenuPanel();
		}
	}

	private void loadHelpPanel() {
		Container c = this.getContentPane();
		c.removeAll();
		this.repaint();
		if (this.helpPanel == null) {
			this.helpPanel = new HelpPanel();
		}
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		c.add(Box.createVerticalGlue());
		c.add(this.helpPanel);
		c.add(Box.createVerticalGlue());
		this.validate();
	}

	private void stopSound() {
		Sounds.GAME_SOUND.stop();
		Sounds.FIRE_SOUND.stop();
		Sounds.BOMB_SOUND.stop();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals(MenuPanel.newGameCommand)) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						MainFrame.this.startGame();
					} catch (InterruptedException | UnsupportedAudioFileException | IOException
							| LineUnavailableException e) {
						e.printStackTrace();
					}

				}
			}).start();
		}
		if (command.equals(MenuPanel.helpCommand)) {
			this.loadHelpPanel();
		}
	}
}
