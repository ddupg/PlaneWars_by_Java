package net.hardbird.entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;

import net.hardbird.config.Config;
import net.hardbird.config.Images;
import net.hardbird.config.SupplyType;
import net.hardbird.ui.GamePlayingPanel;

public class Supply extends Flyer {

	private GamePlayingPanel playingPanel;
	private SupplyType supplyType;
	private Thread runThread;

	public Supply(GamePlayingPanel playingPanel, SupplyType supplyType) {
		this.playingPanel = playingPanel;
		this.supplyType = supplyType;

		switch (supplyType) {
		case BloodSupplyType:
			image = Images.BLOOD_SUPPORT_IMG;
			this.width = Config.BLOOD_SUPPLY_WIDTH;
			this.height = Config.BLOOD_SUPPLY_HEIGHT;
			break;
		case DoubleBulletsSupplyType:
			image = Images.DOUBLE_BULLETS_SUPPORT_IMG;
			this.width = Config.DOUBLE_BULLETS_SUPPLY_WIDTH;
			this.height = Config.DOUBLE_BULLETS_SUPPLY_HEIGHT;
		default:
			break;
		}
	}

	public void startFly() {
		runThread = new Thread(new RunThread());
		runThread.start();
	}

	class RunThread implements Runnable {
		@Override
		public void run() {
			while (true) {
				move();
				if (!getRectangle().intersects(playingPanel.getRectangle())) {
					LinkedList<Supply> supplies = playingPanel.getSupplies();
					synchronized (supplies) {
						supplies.remove(Supply.this);
					}
					break;
				}
				if (getRectangle().intersects(playingPanel.getMyPlane().getRectangle())) {
					MyPlane myPlane = playingPanel.getMyPlane();
					synchronized (myPlane) {
						myPlane.getSupply(Supply.this);
					}
					LinkedList<Supply> supplies = playingPanel.getSupplies();
					synchronized (supplies) {
						supplies.remove(Supply.this);
					}
					break;
				}

				try {
					Thread.sleep(Config.GAME_PANEL_REPAINT_INTERVAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public SupplyType getSupplyType() {
		return supplyType;
	}

	public void setSupportType(SupplyType supplyType) {
		this.supplyType = supplyType;
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(image, posX - width / 2, posY - height / 2, width, height, playingPanel);
	}

}
