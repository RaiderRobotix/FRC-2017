package org.raiderrobotix.vision;

import java.awt.Color;
import java.awt.image.BufferedImage;

public abstract class ImageUtilities {

	public static final void setContrast(BufferedImage img, float amount) {
		// Adjust contrast of a BufferedImage
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				amount = (float) Math.pow((double) ((255.0f + amount) / 255.0f), 2.0);
				float value = (255.0f + amount) / 255.0f;
				value *= value;
				Color c = new Color(img.getRGB(x, y));
				float r = c.getRed();
				float g = c.getGreen();
				float b = c.getBlue();
				r /= 255.0f;
				g /= 255.0f;
				b /= 255.0f;
				r = ((((r - 0.5f) * value) + 0.5f) * 255.0f);
				g = ((((g - 0.5f) * value) + 0.5f) * 255.0f);
				b = ((((b - 0.5f) * value) + 0.5f) * 255.0f);
				c = new Color(fixNum(r), fixNum(g), fixNum(b));
				img.setRGB(x, y, c.getRGB());
			}
		}
	}

	private static int fixNum(float num) {
		// Fix integers not in RGB color range.
		int n = (int) num;
		n = n > 255 ? 255 : n;
		n = n < 0 ? 0 : n;
		return n;
	}

	/**
	 * Create a two-dimensional array of booleans that tell us where the tape
	 * is.
	 * 
	 * @param img
	 *            The BufferedImage you wish to search.
	 * @param threshold
	 *            The RGB threshold of the tape.
	 * @return The two-dimensional truth table of booleans.
	 */
	public static final boolean[][] getTruthTable(BufferedImage img, int threshold) {
		boolean[][] ret = new boolean[img.getWidth()][img.getHeight()];
		for (int x = 0; x < ret.length; x++) {
			for (int y = 0; y < ret[x].length; y++) {
				ret[x][y] = img.getRGB(x, y) >= threshold;
			}
		}
		return ret;
	}

	public static final BufferedImage xCut(BufferedImage img, int x1, int x2) {
		// Cut an image horizontally.
		BufferedImage ret = new BufferedImage((x2 - x1) + 1, img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for (int x = x1; x <= x2; x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				ret.setRGB(x1, y, img.getRGB(x1, y));
			}
		}
		return ret;
	}

	public static final BufferedImage yCut(BufferedImage img, int y1, int y2) {
		// Cut an image vertically.
		BufferedImage ret = new BufferedImage(img.getWidth(), (y2 - y1) + 1, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = y1; y <= y2; y++) {
				ret.setRGB(x, y1, img.getRGB(x, y1));
			}
		}
		return ret;
	}

	public static final float percentTrue(boolean[][] table) {
		float ret = 0.0f;
		for (boolean[] i : table) {
			for (boolean j : i) {
				if (j) {
					ret++;
				}
			}
		}
		return ret / ((float) table.length * table[0].length);
	}

	public static final float percentAboveThreshold(BufferedImage img, int threshold) {
		return percentTrue(getTruthTable(img, threshold));
	}

	/**
	 * Get center of a table of Trues.
	 * 
	 * @param table
	 *            A two-dimensional array of booleans of which you want to
	 *            search.
	 * @return new float[]{xCoord, yCoord}; (the center)
	 */
	public static final float[] getCenter(boolean[][] table) {
		float numTrue = percentTrue(table);
		numTrue *= (((float) table.length) * ((float) table[0].length));
		float retX = 0.0f;
		float retY = 0.0f;
		for (int x = 0; x < table.length; x++) {
			for (int y = 0; y < table[x].length; y++) {
				if (table[x][y]) {
					retX += x;
					retY += y;
				}
			}
		}
		retX /= numTrue;
		retY /= numTrue;
		return new float[] { retX, retY };
	}

}
