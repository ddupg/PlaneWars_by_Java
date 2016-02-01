package net.hardbird.entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.LinkedList;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import net.hardbird.config.Config;
import net.hardbird.config.EnemyType;
import net.hardbird.config.SupplyType;
import net.hardbird.ui.GamePlayingPanel;

public abstract class EnemyPlane extends Plane {

	protected GamePlayingPanel playingPanel;

	protected int fireTimeRemainder;
	protected Thread runThread;
	protected EnemyType enemyType;

	public EnemyPlane() {
		// TODO Auto-generated constructor stub
	}

	protected abstract void Fire();

	public void paint(Graphics g) {
		if (isAlive()) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.drawImage(image, posX - width / 2, posY - height / 2, width, height, playingPanel);
		}
	}

	class RunThread implements Runnable {
		@Override
		public void run() {
			while (alive) {
				EnemyPlane.this.move();
				if (!playingPanel.getRectangle().intersects(getRectangle())) {
					alive = false;
					LinkedList<EnemyPlane> enemys = playingPanel.getEnemys();
					synchronized (enemys) {
						setAlive(false);
						enemys.remove(EnemyPlane.this);
					}
				}

				MyPlane myPlane = playingPanel.getMyPlane();
				synchronized (myPlane) {
					if (myPlane != null && myPlane.isAlive() && getRectangle().intersects(myPlane.getRectangle())) {
						myPlane.getAttacked(getAttackPower());
						try {
							productBomb();
						} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
							e.printStackTrace();
						}
						LinkedList<EnemyPlane> enemys = playingPanel.getEnemys();
						synchronized (enemys) {
							setAlive(false);
							enemys.remove(EnemyPlane.this);
						}
					}
				}

				if (fireTimeRemainder == 0) {
					Fire();
				}
				if (enemyType == EnemyType.SmallEnemyPlaneType) {
					fireTimeRemainder = (fireTimeRemainder - Config.GAME_PANEL_REPAINT_INTERVAL
							+ Config.SMALL_PLANE_ENEMY_FIRE_INTERVAL) % Config.SMALL_PLANE_ENEMY_FIRE_INTERVAL;
				} else if (enemyType == EnemyType.BigEnemyPlaneType) {
					fireTimeRemainder = (fireTimeRemainder - Config.GAME_PANEL_REPAINT_INTERVAL
							+ Config.BIG_PLANE_ENEMY_FIRE_INTERVAL) % Config.BIG_PLANE_ENEMY_FIRE_INTERVAL;
				}
				try {
					Thread.sleep(Config.GAME_PANEL_REPAINT_INTERVAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void productBomb() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		Bomb bomb = new Bomb(playingPanel);
		bomb.setPosX(this.posX);
		bomb.setPosY(this.posY);
		LinkedList<Bomb> bombs = playingPanel.getBombs();
		synchronized (bombs) {
			bombs.add(bomb);
		}
	}

	public void productSupply() {
		SupplyType supplyType;
		if (Math.random() < 0.5) {
			supplyType = SupplyType.BloodSupplyType;
		} else {
			supplyType = SupplyType.DoubleBulletsSupplyType;
		}
		Supply supply = new Supply(playingPanel, supplyType);
		supply.setPosX(this.posX);
		supply.setPosY(this.posY);
		supply.setSpeedX(0);
		supply.setSpeedY(Config.SUPPLY_SPEED);
		LinkedList<Supply> supplies = playingPanel.getSupplies();
		synchronized (supplies) {
			supplies.add(supply);
		}
		supply.startFly();
	}

	@Override
	public void getAttacked(int x) {
		super.getAttacked(x);
		if (blood <= 0) {
			setAlive(false);
			try {
				productBomb();
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				e.printStackTrace();
			}
			LinkedList<EnemyPlane> enemys = playingPanel.getEnemys();
			synchronized (enemys) {
				enemys.remove(this);
			}
			if (Math.random() < Config.SUPPLY_APPEAR_PROBABILITY) {
				productSupply();
			}
		}
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public int getBlood() {
		return blood;
	}

	public void setBlood(int blood) {
		this.blood = blood;
	}

	public int getAttackPower() {
		return attackPower;
	}

	public void setAttackPower(int attackPower) {
		this.attackPower = attackPower;
	}

}
