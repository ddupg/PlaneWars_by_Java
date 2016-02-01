package net.hardbird.config;

public class Config {

	// size
	public final static int GAME_WINDOW_WIDTH = 480;
	public final static int GAME_WINDOW_HEIGHT = 800;

	public final static int MENU_PANEL_WIDTH = 130;
	public final static int MENU_PANEL_HEIGHT = 248;

	public final static int MYPLANE_IMAGE_WIDTH = 64;
	public final static int MYPLANE_IMAGE_HEIGHT = 64;

	public final static int SMALL_ENEMY_IMAGE_WIDTH = 36;
	public final static int SMALL_ENEMY_IMAGE_HEIGHT = 46;

	public final static int BIG_ENEMY_IMAGE_WIDTH = 64;
	public final static int BIG_ENEMY_IMAGE_HEIGHT = 64;

	public final static int BOSS_IMAGE_WIDTH = 190;
	public final static int BOSS_IMAGE_HEIGHT = 119;

	public final static int MY_BULLET_WIDTH = 9;
	public final static int MY_BULLET_HEIGHT = 14;

	public final static int SMALL_ENEMY_BULLET_WIDTH = 5;
	public final static int SMALL_ENEMY_BULLET_HEIGHT = 10;
	public final static int BIG_ENEMY_BULLET_WIDTH = 5;
	public final static int BIG_ENEMY_BULLET_HEIGHT = 10;
	public final static int BOSS_BULLET_WIDTH = 20;
	public final static int BOSS_BULLET_HEIGHT = 20;

	public final static int BOMB_WIDTH = 64;
	public final static int BOMB_HEIGHT = 64;

	public final static int BLOOD_SUPPLY_WIDTH = 40;
	public final static int BLOOD_SUPPLY_HEIGHT = 25;
	public final static int DOUBLE_BULLETS_SUPPLY_WIDTH = 40;
	public final static int DOUBLE_BULLETS_SUPPLY_HEIGHT = 35;

	public final static int BUTTON_WIDTH = 130;
	public final static int BUTTON_HEIGHT = 45;

	// time interval
	public final static int GAME_PANEL_REPAINT_INTERVAL = 30;
	public final static int MYPLANE_FIRE_INTERVAL = 5 * GAME_PANEL_REPAINT_INTERVAL;
	public final static int ENEMY_PLANE_PRODUCT_INTERVAL = 25 * GAME_PANEL_REPAINT_INTERVAL;

	public final static int SMALL_PLANE_ENEMY_FIRE_INTERVAL = 20 * GAME_PANEL_REPAINT_INTERVAL;
	public final static int BIG_PLANE_ENEMY_FIRE_INTERVAL = 15 * GAME_PANEL_REPAINT_INTERVAL;
	public final static int BOSS_FIRE_INTERVAL = 20 * GAME_PANEL_REPAINT_INTERVAL;

	public final static int BOSS_SHOW_TIME = 2000 * GAME_PANEL_REPAINT_INTERVAL;

	// blood
	public final static int MYPLANE_BLOOD = 100;
	public final static int SMALL_ENEMY_PLANE_BLOOD = 2;
	public final static int BIG_ENEMY_PLANE_BLOOD = 10;
	public final static int BOSS_PLANE_BLOOD = 200;

	// attack power
	public final static int MYPLANE_ATTACK = 2;
	public final static int SMALL_ENEMY_PLANE_ATTACK = 1;
	public final static int BIG_ENEMY_PLANE_ATTACK = 2;
	public final static int BOSS_PLANE_ATTACK = 3;

	// speed
	public final static int MYPLANE_SPEED = 8;
	public final static int SMALL_ENEMY_PLANE_SPEED = 5;
	public final static int BIG_ENEMY_PLANE_SPEED = 6;
	public final static int BOSS_ENEMY_PLANE_SPEED = 7;
	public final static int SUPPLY_SPEED = 5;

	public final static int BULLET_SPEED = 10;

	// nums
	public final static int BIG_ENEMY_PLANE_TYPE_NUM = 10;
	public final static int BOMB_IMG_NUM = 10;

	// support
	public final static double SUPPLY_APPEAR_PROBABILITY = 0.1;
	public final static int BLOOD_SUPPORT = 10;
	public final static int DOUBLE_BULLETS_SUPPORT_NUMS = 20;
}
