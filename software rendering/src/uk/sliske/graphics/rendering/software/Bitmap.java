package uk.sliske.graphics.rendering.software;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class Bitmap {
	private final int		width;
	private final int		height;

	private final byte[]	components;

	public Bitmap(final int width, final int height) {
		this.width = width;
		this.height = height;
		components = new byte[width * height * 4];
	}

	public Bitmap(final String fileName) throws IOException {
		this(new File(fileName));
	}

	public Bitmap(final BufferedImage image) {
		if (image == null) {
			width = 0;
			height = 0;
			components = new byte[0];
			return;
		}

		int width = 0;
		int height = 0;
		byte[] components = null;
		width = image.getWidth();
		height = image.getHeight();

		final int pixels = width * height;
		components = new byte[pixels * 4];

		int[] imgPixels = new int[pixels];
		image.getRGB(0, 0, width, height, imgPixels, 0, width);

		for (int i = 0; i < pixels; i++) {
			int p = imgPixels[i];
			components[i * 4 + 0] = (byte) ((p >> 24) & 0xFF);
			components[i * 4 + 1] = (byte) ((p) & 0xFF);
			components[i * 4 + 2] = (byte) ((p >> 8) & 0xFF);
			components[i * 4 + 3] = (byte) ((p >> 16) & 0xFF);
		}

		this.width = width;
		this.height = height;
		this.components = components;
	}

	public Bitmap(final File file) throws IOException {
		this(ImageIO.read(file));
	}

	public void clear(final byte shade) {
		Arrays.fill(components, shade);
	}

	public void clear(final int shade) {
		clear((byte) shade);
	}

	public void copyPixel(int destX, int destY, int srcX, int srcY, Bitmap src) {
		int destIndex = (destX + destY * width) * 4;
		int srcIndex = (srcX + srcY * src.getWidth()) * 4;

		components[destIndex + 0] = src.getComponent(srcIndex + 0);
		components[destIndex + 1] = src.getComponent(srcIndex + 1);
		components[destIndex + 2] = src.getComponent(srcIndex + 2);
		components[destIndex + 3] = src.getComponent(srcIndex + 3);
	}

	public void drawPixel(final int x, final int y, final byte a, final byte b, final byte g, final byte r) {
		int index = (x + y * width) * 4;
		components[index] = a;
		components[index + 1] = b;
		components[index + 2] = g;
		components[index + 3] = r;
	}

	public void drawPixel(final int x, final int y, final int a, final int b, final int g, final int r) {
		drawPixel(x, y, (byte) a, (byte) b, (byte) g, (byte) r);
	}

	public void copyToByteArray(final byte[] dest) {
		for (int i = 0; i < width * height; i++) {
			dest[i * 3 + 0] = components[i * 4 + 1];
			dest[i * 3 + 1] = components[i * 4 + 2];
			dest[i * 3 + 2] = components[i * 4 + 3];
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public byte getComponent(int index) {
		if (index < 0 || index >= components.length) {
			return (byte) 0x00;
		}
		return components[index];
	}

	public void drawPixel(int x, int y, double a, double b, double g, double r) {
		drawPixel(x, y, (byte) a, (byte) b, (byte) g, (byte) r);
	}

	public void drawBitmap(int x, int y, final Bitmap bitmap) {
		for (int i = 0, w = bitmap.width; i < w; i++) {
			int nx = i + x;
			if (nx >= width || nx < 0) {
				continue;
			}
			for (int j = 0, h = bitmap.height; j < h; j++) {
				int ny = j + y;
				if (ny >= height || ny < 0) {
					continue;
				}
				
				copyPixel(nx, ny, i, j, bitmap);
			}
		}
	}
}
