package net.hardbird.entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;

import net.hardbird.config.BulletType;
import net.hardbird.config.Config;
import net.hardbird.config.Images;
import net.hardbird.ui.GamePlayingPanel;
import net.hardbird.util.Randomer;

public class Boss extends Plane {

	private GamePlayingPanel playingPanel;
	private Thread runThread;
	private int fireTimeRemainder = 0;

	public Boss(GamePlayingPanel panel) {
		this.playingPanel = panel;
		width = Config.BOSS_IMAGE_WIDTH;
		height = Config.BOSS_IMAGE_HEIGHT;
		image = Images.BOSS_IMG;
		alive = false;
		blood = Config.BOSS_PLANE_BLOOD;
	}

	class RunThread implements Runnable {

		@Override
		public void run() {
			while (isAlive()) {
				// System.err.println("boss plane thread");
				Boss.this.move();
				if (fireTimeRemainder == 0) {
					Fire();
				}
				fireTimeRemainder = (fireTimeRemainder - Config.GAME_PANEL_REPAINT_INTERVAL + Config.BOSS_FIRE_INTERVAL)
						% Config.BOSS_FIRE_INTERVAL;
				try {
					Thread.sleep(Config.GAME_PANEL_REPAINT_INTERVAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void move() {
		synchronized (this) {
			if (Math.random() < 0.1) {
				speedX = Randomer.getRandom(-Config.BOSS_ENEMY_PLANE_SPEED, Config.BOSS_ENEMY_PLANE_SPEED);
				speedY = Randomer.getRandom(-Config.BOSS_ENEMY_PLANE_SPEED, Config.BOSS_ENEMY_PLANE_SPEED);
			}
			posX += speedX;
			posY += speedY;
			Rectangle rect = playingPanel.getRectangle();
			posX = Math.max(posX, 0);
			posX = Math.min(posX, (int) (rect.getWidth()));
			posY = Math.max(posY, 0);
			posY = Math.min(posY, (int) (rect.getHeight()));
		}
	}

	public void Fire() {
		if (Math.random() < 0.5)
			return;

		LinkedList<Bullet> bullets = playingPanel.getEnemyBullets();
		int nums = 10;
		synchronized (this) {
			synchronized (bullets) {
				int speed = Config.BULLET_SPEED;
				double degree = 2.0 / nums * Math.PI;
				for (int i = 0; i < nums; i++) {
					// [x*cosA-y*sinA x*sinA+y*cosA]
					int x = (int) (speed * Math.cos(degree * i) - speed * Math.sin(degree * i));
					int y = (int) (speed * Math.sin(degree * i) + speed * Math.cos(degree * i));
					Bullet bullet = new EnemyBullet(playingPanel, BulletType.BossBulletType);
					bullet.setImage(Images.BOSS_BULLET_IMG);
					bullet.setPosX(this.posX);
					bullet.setPosY(this.posY);
					bullet.setSpeedX(x);
					bullet.setSpeedY(y);
					bullet.startFly();
					bullets.add(bullet);
				}
			}
		}
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(image, posX - width / 2, posY - height / 2, width, height, playingPanel);
	}

	public void startFly() {
		alive = true;
		runThread = new Thread(new RunThread());
		runThread.start();
	}

	@Override
	public Rectangle getRectangle() {
		int fixw = width / 3, fixh = height / 3;
		return new Rectangle(posX - fixw, posY - fixh, fixw * 2, fixh * 2);
	}
}
