package net.hardbird.entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import net.hardbird.config.BulletType;
import net.hardbird.config.Config;
import net.hardbird.config.Images;
import net.hardbird.config.Sounds;
import net.hardbird.ui.GamePlayingPanel;

public class MyPlane extends Plane {

	GamePlayingPanel playingPanel;

	private boolean fire;
	private int fireTimeRemainder;

	private Thread runThread;

	private int doubleBulletsRemainder;

	public MyPlane(GamePlayingPanel gamePlayingPanel) {

		this.playingPanel = gamePlayingPanel;

		this.image = Images.MYPLANE_IMG;
		this.width = Config.MYPLANE_IMAGE_WIDTH;
		this.height = Config.MYPLANE_IMAGE_HEIGHT;
		this.blood = Config.MYPLANE_BLOOD;
		this.attackPower = Config.MYPLANE_ATTACK;
		this.alive = true;
		this.fire = false;
		this.fireTimeRemainder = Config.MYPLANE_FIRE_INTERVAL;

		doubleBulletsRemainder = 0;

		runThread = new Thread(new RunThread());
		runThread.start();
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(image, posX - width / 2, posY - height / 2, width, height, playingPanel);
	}

	class RunThread implements Runnable {

		@Override
		public void run() {
			while (alive) {

				MyPlane.this.move();
				fireTimeRemainder -= Config.GAME_PANEL_REPAINT_INTERVAL;
				if (fire && fireTimeRemainder == 0) {
					MyPlane.this.Fire();
				}
				if (fireTimeRemainder == 0) {
					fireTimeRemainder = Config.MYPLANE_FIRE_INTERVAL;
				}
				try {
					Thread.sleep(Config.GAME_PANEL_REPAINT_INTERVAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void Fire() {

		Sounds.FIRE_SOUND.play();
		LinkedList<Bullet> bullets = playingPanel.getMyBullets();
		synchronized (bullets) {

			if (doubleBulletsRemainder > 0) {
				synchronized (this) {
					--doubleBulletsRemainder;
					Bullet myBullet = new MyBullet(this.playingPanel, BulletType.MyBulletType);
					myBullet.setPosX(this.getPosX() - width / 4);
					myBullet.setPosY(this.getPosY());
					myBullet.setSpeedX(0);
					myBullet.setSpeedY(-Config.BULLET_SPEED);
					myBullet.setImage(Images.MY_BULLET_IMG);
					myBullet.startFly();
					bullets.add(myBullet);
					
					myBullet = new MyBullet(this.playingPanel, BulletType.MyBulletType);
					myBullet.setPosX(this.getPosX() + width / 4);
					myBullet.setPosY(this.getPosY());
					myBullet.setSpeedX(0);
					myBullet.setSpeedY(-Config.BULLET_SPEED);
					myBullet.setImage(Images.MY_BULLET_IMG);
					myBullet.startFly();
					bullets.add(myBullet);
				}
			} else {
				Bullet myBullet = new MyBullet(this.playingPanel, BulletType.MyBulletType);
				myBullet.setPosX(this.getPosX());
				myBullet.setPosY(this.getPosY());
				myBullet.setSpeedX(0);
				myBullet.setSpeedY(-Config.BULLET_SPEED);
				myBullet.setImage(Images.MY_BULLET_IMG);
				myBullet.startFly();
				bullets.add(myBullet);
			}
		}
	}

	public void getSupply(Supply supply) {
		switch (supply.getSupplyType()) {
		case BloodSupplyType:
			blood += Config.BLOOD_SUPPORT;
			blood = Math.min(blood, Config.MYPLANE_BLOOD);
			break;
		case DoubleBulletsSupplyType:
			doubleBulletsRemainder += Config.DOUBLE_BULLETS_SUPPORT_NUMS;
			break;
		default:
			break;
		}
	}

	@Override
	public void move() {
		super.move();
		posX = Math.max(posX, 0);
		posX = Math.min(posX, Config.GAME_WINDOW_WIDTH);
		posY = Math.max(posY, 0);
		posY = Math.min(posY, Config.GAME_WINDOW_HEIGHT);
	}

	public void setFire(boolean f) {
		fire = f;
	}

	public void mouseMoved(MouseEvent e) {
		// this.setPosX(e.getX());
		// this.setPosY(e.getY());
	}
}
