package net.hardbird.entity;

public class Plane extends Flyer {

	protected volatile boolean alive;
	protected int blood, attackPower;

	public void getAttacked(int x) {
		synchronized (this) {
			blood -= x;
			if (blood <= 0) {
				blood = 0;
				alive = false;
			}
		}
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

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

}
