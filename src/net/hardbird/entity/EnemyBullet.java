package net.hardbird.entity;

import java.util.LinkedList;

import net.hardbird.config.BulletType;
import net.hardbird.config.Config;
import net.hardbird.ui.GamePlayingPanel;

public class EnemyBullet extends Bullet {

	public EnemyBullet(GamePlayingPanel playingPanel, BulletType bulletType) {
		super(playingPanel, bulletType);
	}

	class RunThread implements Runnable {

		@Override
		public void run() {
			while (isAlive()) {
				move();
				if (!playingPanel.getRectangle().intersects(getRectangle())) {
					LinkedList<Bullet> enemyBullets = playingPanel.getEnemyBullets();
					synchronized (enemyBullets) {
						EnemyBullet.this.setAlive(false);
						enemyBullets.remove(EnemyBullet.this);
						break;
					}
				}

				// if this is enemy's bullet
				MyPlane myPlane = playingPanel.getMyPlane();
				synchronized (myPlane) {
					if (myPlane != null && myPlane.isAlive()) {
						if (getRectangle().intersects(myPlane.getRectangle())) {
							myPlane.getAttacked(getAttack());

							LinkedList<Bullet> enemysBullets = playingPanel.getEnemyBullets();
							synchronized (enemysBullets) {
								EnemyBullet.this.setAlive(false);
								enemysBullets.remove(EnemyBullet.this);
							}
						}
					}
				}

				try {
					Thread.sleep(Config.GAME_PANEL_REPAINT_INTERVAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}

	@Override
	public void startFly() {
		this.alive = true;
		runThread = new Thread(new RunThread());
		runThread.start();
	}

}
