package net.hardbird.entity;

import java.util.LinkedList;

import net.hardbird.config.BulletType;
import net.hardbird.config.Config;
import net.hardbird.config.EnemyType;
import net.hardbird.config.Images;
import net.hardbird.ui.GamePlayingPanel;

public class BigEnemyPlane extends EnemyPlane {

	public BigEnemyPlane(GamePlayingPanel playingPanel, EnemyType enemyType) {
		this.playingPanel = playingPanel;
		this.enemyType = enemyType;
		this.fireTimeRemainder = 0;

		this.width = Config.BIG_ENEMY_IMAGE_WIDTH;
		this.height = Config.BIG_ENEMY_IMAGE_HEIGHT;

		this.blood = Config.BIG_ENEMY_PLANE_BLOOD;
		this.attackPower = Config.BIG_ENEMY_PLANE_ATTACK;
	}

	public void startFly() {
		alive = true;
		runThread = new Thread(new RunThread());
		runThread.start();
	}

	@Override
	protected void Fire() {
		Bullet bullet = new EnemyBullet(playingPanel, BulletType.BigEnemyBulletType);
		bullet.setPosX(this.posX);
		bullet.setPosY(this.posY);
		if (speedY > 0) {
			bullet.setImage(Images.BIG_ENEMY_PLANE_BULLET_INVERTED_IMG);
			bullet.setSpeedX(0);
			bullet.setSpeedY(Config.BULLET_SPEED);
		} else {
			bullet.setImage(Images.BIG_ENEMY_PLANE_BULLET_IMG);
			bullet.setSpeedX(0);
			bullet.setSpeedY(-Config.BULLET_SPEED);
		}
		LinkedList<Bullet> enemyBullets = playingPanel.getEnemyBullets();
		synchronized (enemyBullets) {
			enemyBullets.add(bullet);
		}
		bullet.startFly();
	}

}
