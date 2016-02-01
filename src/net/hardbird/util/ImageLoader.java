package net.hardbird.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageLoader {

	BufferedImage resourceImg;

	public ImageLoader(String path) throws IOException {
		resourceImg = ImageIO.read(new File(path));
	}

	public Image getImage() {
		return getSubImage(0, 0, resourceImg.getWidth(), resourceImg.getHeight());
	}

	public Image getSubImage(int x, int y, int width, int height) {
		BufferedImage targetImg = resourceImg.getSubimage(x, y, width, height);
		return new ImageIcon(targetImg).getImage();
	}

	public Image getRorateImage(int degree) {
		return getRorateSubImage(0, 0, resourceImg.getWidth(), resourceImg.getHeight(), degree);
	}

	public Image getRorateSubImage(int x, int y, int width, int height, int degree) {
		BufferedImage targetImg = resourceImg.getSubimage(x, y, width, height);
		targetImg = rotateImage(targetImg, degree);
		return (new ImageIcon(targetImg)).getImage();
	}

	public static BufferedImage rotateImage(final BufferedImage bufferedimage, final int degree) {
		int w = bufferedimage.getWidth();
		int h = bufferedimage.getHeight();
		int type = bufferedimage.getColorModel().getTransparency();
		BufferedImage img;
		Graphics2D graphics2d;
		(graphics2d = (img = new BufferedImage(w, h, type)).createGraphics())
				.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);
		graphics2d.drawImage(bufferedimage, 0, 0, null);
		graphics2d.dispose();
		return img;
	}
}
