package net.hardbird.util;

import java.util.Random;

public class Randomer {

	public static Random random = new Random();

	public static int getRandom(int lim) {
		return random.nextInt(lim);
	}

	public static int getRandom(int L, int R) {
		int len = R - L + 1;
		return L + random.nextInt(len);
	}
}
