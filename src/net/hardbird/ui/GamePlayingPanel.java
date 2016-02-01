package net.hardbird.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

import javax.swing.JPanel;

import net.hardbird.config.Config;
import net.hardbird.config.EnemyType;
import net.hardbird.config.Images;
import net.hardbird.entity.BigEnemyPlane;
import net.hardbird.entity.Bomb;
import net.hardbird.entity.Boss;
import net.hardbird.entity.Bullet;
import net.hardbird.entity.EnemyPlane;
import net.hardbird.entity.MyPlane;
import net.hardbird.entity.SmallEnemyPlane;
import net.hardbird.entity.Supply;
import net.hardbird.util.Randomer;

public class GamePlayingPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MyPlane myPlane;
	private LinkedList<EnemyPlane> enemys;
	private LinkedList<Bullet> myBullets, enemyBullets;
	private LinkedList<Bomb> bombs;
	private LinkedList<Supply> supplies;
	private Boss boss;

	private int enemyProductRemainder, bossTimeRemainder;

	private Thread paintThread;

	public GamePlayingPanel() {
		this.initComponents();
	}

	private void initComponents() {
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);

		this.setSize(Config.GAME_WINDOW_WIDTH, Config.GAME_WINDOW_HEIGHT);
		this.setPreferredSize(new Dimension(Config.GAME_WINDOW_WIDTH, Config.GAME_WINDOW_HEIGHT));

		this.setDoubleBuffered(true);
		this.setOpaque(false);

	}

	public void startGame() {
		myPlane = new MyPlane(this);
		myPlane.setPosX(this.getWidth() / 2);
		myPlane.setPosY(this.getHeight() * 3 / 4);
		myPlane.setSpeedX(0);
		myPlane.setSpeedY(0);

		enemys = new LinkedList<>();
		myBullets = new LinkedList<>();
		enemyBullets = new LinkedList<>();
		bombs = new LinkedList<>();
		supplies = new LinkedList<>();
		enemyProductRemainder = Config.ENEMY_PLANE_PRODUCT_INTERVAL;
		bossTimeRemainder = Config.BOSS_SHOW_TIME;

		paintThread = new Thread(new PaintThread());
		paintThread.start();
	}

	class PaintThread implements Runnable {

		@Override
		public void run() {
			while (isPlaying()) {
				enemyProductRemainder -= Config.GAME_PANEL_REPAINT_INTERVAL;
				if (enemyProductRemainder == 0) {
					GamePlayingPanel.this.ProductEnemyPlane();
					enemyProductRemainder = Config.ENEMY_PLANE_PRODUCT_INTERVAL;
				}

				if (boss == null) {
					bossTimeRemainder -= Config.GAME_PANEL_REPAINT_INTERVAL;
					if (bossTimeRemainder == 0) {
						GamePlayingPanel.this.ShowBoss();
					}
				}

				GamePlayingPanel.this.repaint();
				try {
					Thread.sleep(Config.GAME_PANEL_REPAINT_INTERVAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void ProductEnemyPlane() {
		EnemyType enemyType;
		if (Math.random() < 0.25) {
			enemyType = EnemyType.BigEnemyPlaneType;
		} else {
			enemyType = EnemyType.SmallEnemyPlaneType;
		}
		if (enemyType == EnemyType.SmallEnemyPlaneType) {
			SmallEnemyPlane enemy = new SmallEnemyPlane(this, enemyType);
			enemy.setImage(Images.SMALL_PLANE_INVERTED_IMG);
			enemy.setSpeedX(0);
			enemy.setSpeedY(Config.SMALL_ENEMY_PLANE_SPEED);
			enemy.setPosX(Randomer.getRandom(enemy.getWidth() / 2, Config.GAME_WINDOW_WIDTH - enemy.getWidth() / 2));
			enemy.setPosY(enemy.getHeight() / 2);
			synchronized (enemys) {
				enemys.add(enemy);
			}
			enemy.startFly();
		} else if (enemyType == EnemyType.BigEnemyPlaneType) {
			BigEnemyPlane enemy = new BigEnemyPlane(this, enemyType);
			enemy.setImage(Images.BIG_PLANE_INVERTED_IMG[Randomer.getRandom(Config.BIG_ENEMY_PLANE_TYPE_NUM)]);
			enemy.setSpeedX(0);
			enemy.setSpeedY(Config.SMALL_ENEMY_PLANE_SPEED);
			enemy.setPosX(Randomer.getRandom(enemy.getWidth() / 2, Config.GAME_WINDOW_WIDTH - enemy.getWidth() / 2));
			enemy.setPosY(enemy.getHeight() / 2);
			synchronized (enemys) {
				enemys.add(enemy);
			}
			enemy.startFly();
		}
	}

	private void ShowBoss() {
		boss = new Boss(this);
		boss.setPosX(this.getWidth() / 2);
		boss.setPosY(Config.BOSS_IMAGE_HEIGHT / 2);
		boss.startFly();
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		super.paintComponent(g);
		// if (false == isPlaying()) {
		// return;
		// }

		synchronized (supplies) {
			for(Supply supply : supplies){
				supply.paint(g);
			}
		}
		
		synchronized (enemyBullets) {
			for (Bullet bullet : enemyBullets) {
				bullet.paint(g);
			}
		}

		synchronized (enemys) {
			for (EnemyPlane enemy : enemys) {
				enemy.paint(g);
			}
		}

		synchronized (bombs) {
			for (Bomb bomb : bombs) {
				bomb.paint(g);
			}
		}

		synchronized (myBullets) {
			for (Bullet bullet : myBullets) {
				bullet.paint(g);
			}
		}

		if (boss != null && boss.isAlive()) {
			boss.paint(g);
		}

		if (myPlane != null && myPlane.isAlive()) {
			myPlane.paint(g);
		}

		g2d.setColor(Color.ORANGE);
		g2d.fillRect(10, 10, myPlane.getBlood(), 20);
		if (boss != null && boss.isAlive()) {
			g2d.setColor(Color.GRAY);
			g2d.fillRect(10, 40, boss.getBlood(), 20);
		}
		g2d.setColor(Color.BLACK);
		this.repaint();
	};

	public LinkedList<EnemyPlane> getEnemys() {
		return enemys;
	}

	public void setEnemys(LinkedList<EnemyPlane> enemys) {
		this.enemys = enemys;
	}

	public LinkedList<Bullet> getEnemyBullets() {
		return enemyBullets;
	}

	public void setEnemyBullets(LinkedList<Bullet> enemyBullets) {
		this.enemyBullets = enemyBullets;
	}

	public LinkedList<Supply> getSupplies() {
		return supplies;
	}

	public void setSupplies(LinkedList<Supply> supplies) {
		this.supplies = supplies;
	}

	public Boss getBoss() {
		return boss;
	}

	public void setBoss(Boss boss) {
		this.boss = boss;
	}

	public boolean isPlaying() {
		return myPlane != null && myPlane.isAlive() && (boss == null || boss.isAlive());
	}

	public Rectangle getRectangle() {
		return new Rectangle(0, 0, Config.GAME_WINDOW_WIDTH, Config.GAME_WINDOW_HEIGHT);
	}

	public MyPlane getMyPlane() {
		return myPlane;
	}

	public void setMyPlane(MyPlane myPlane) {
		this.myPlane = myPlane;
	}

	public LinkedList<Bullet> getMyBullets() {
		return myBullets;
	}

	public void setMyBullets(LinkedList<Bullet> myBullets) {
		this.myBullets = myBullets;
	}

	public LinkedList<Bomb> getBombs() {
		return bombs;
	}

	public void setBombs(LinkedList<Bomb> bombs) {
		this.bombs = bombs;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (myPlane != null) {
			myPlane.mouseMoved(e);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (myPlane != null && myPlane.isAlive()) {
			synchronized (myPlane) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					myPlane.setFire(true);
				}
				int x = myPlane.getSpeedX(), y = myPlane.getSpeedY();
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					y -= Config.MYPLANE_SPEED;
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					y += Config.MYPLANE_SPEED;
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					x -= Config.MYPLANE_SPEED;
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					x += Config.MYPLANE_SPEED;
				}
				x = Math.max(x, -Config.MYPLANE_SPEED);
				x = Math.min(x, Config.MYPLANE_SPEED);
				y = Math.max(y, -Config.MYPLANE_SPEED);
				y = Math.min(y, Config.MYPLANE_SPEED);
				myPlane.setSpeedX(x);
				myPlane.setSpeedY(y);
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (myPlane != null && myPlane.isAlive()) {
			synchronized (myPlane) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					myPlane.setFire(false);
				}
				int x = myPlane.getSpeedX(), y = myPlane.getSpeedY();
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					y += Config.MYPLANE_SPEED;
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					y -= Config.MYPLANE_SPEED;
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					x += Config.MYPLANE_SPEED;
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					x -= Config.MYPLANE_SPEED;
				}
				myPlane.setSpeedX(x);
				myPlane.setSpeedY(y);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
