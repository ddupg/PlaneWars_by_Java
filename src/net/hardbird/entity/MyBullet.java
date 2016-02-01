package net.hardbird.entity;

import java.util.LinkedList;

import net.hardbird.config.BulletType;
import net.hardbird.config.Config;
import net.hardbird.ui.GamePlayingPanel;

public class MyBullet extends Bullet {

	public MyBullet(GamePlayingPanel playingPanel, BulletType bulletType) {
		super(playingPanel, bulletType);
	}

	class RunThread implements Runnable {

		@Override
		public void run() {
			while (isAlive()) {
				move();
				if (!playingPanel.getRectangle().intersects(getRectangle())) {
					if (bulletType == BulletType.MyBulletType) {
						LinkedList<Bullet> myBullets = playingPanel.getMyBullets();
						synchronized (myBullets) {
							MyBullet.this.setAlive(false);
							myBullets.remove(MyBullet.this);
							break;
						}
					}
				}

				LinkedList<EnemyPlane> enemys = playingPanel.getEnemys();
				synchronized (enemys) {
					for (EnemyPlane enemy : enemys) {
						if (getRectangle().intersects(enemy.getRectangle())) {
							enemy.getAttacked(getAttack());

							LinkedList<Bullet> myBullets = playingPanel.getMyBullets();
							synchronized (myBullets) {
								setAlive(false);
								myBullets.remove(MyBullet.this);
							}
							break;
						}
					}
					Boss boss = playingPanel.getBoss();
					if(boss!=null && boss.isAlive()){
						synchronized (boss) {
							if (boss.getRectangle().intersects(getRectangle())) {
								boss.getAttacked(getAttack());

								LinkedList<Bullet> myBullets = playingPanel.getMyBullets();
								synchronized (myBullets) {
									setAlive(false);
									myBullets.remove(MyBullet.this);
								}
								break;
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
