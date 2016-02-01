package net.hardbird.entity;

import java.awt.Graphics;
import java.awt.Graphics2D;

import net.hardbird.config.BulletType;
import net.hardbird.config.Config;
import net.hardbird.ui.GamePlayingPanel;

public abstract class Bullet extends Flyer {

	protected BulletType bulletType;
	protected GamePlayingPanel playingPanel;

	protected Thread runThread;
	protected boolean alive;
	protected int attack;

	public Bullet(GamePlayingPanel playingPanel, BulletType bulletType) {
		this.playingPanel = playingPanel;
		this.bulletType = bulletType;
		this.alive = false;

		switch (bulletType) {
		case MyBulletType:
			this.attack = Config.MYPLANE_ATTACK;
			this.width = Config.MY_BULLET_WIDTH;
			this.height = Config.MY_BULLET_HEIGHT;
			break;
		case SmallEnemyBulletTpye:
			this.attack = Config.SMALL_ENEMY_PLANE_ATTACK;
			this.width = Config.SMALL_ENEMY_BULLET_WIDTH;
			this.height = Config.SMALL_ENEMY_BULLET_HEIGHT;
			break;
		case BigEnemyBulletType:
			this.attack = Config.BIG_ENEMY_PLANE_ATTACK;
			this.width = Config.BIG_ENEMY_BULLET_WIDTH;
			this.height = Config.BIG_ENEMY_BULLET_HEIGHT;
			break;
		case BossBulletType:
			this.attack = Config.BOSS_PLANE_ATTACK;
			this.width = Config.BOSS_BULLET_WIDTH;
			this.height = Config.BOSS_BULLET_HEIGHT;
			break;
		}
	}

	public abstract void startFly(); 

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(image, posX - width / 2, posY - height / 2, width, height, playingPanel);
	}

	public BulletType getBulletType() {
		return bulletType;
	}

	public void setBulletType(BulletType bulletType) {
		this.bulletType = bulletType;
	}

	public GamePlayingPanel getPlayingPanel() {
		return playingPanel;
	}

	public void setPlayingPanel(GamePlayingPanel playingPanel) {
		this.playingPanel = playingPanel;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

}
