package org.raiderrobotix.vision;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

/**
 * A set of utilities to work with BufferedImages
 */
public abstract class BufferedImageUtilities {

	public static final void setContrast(BufferedImage img, float scale, int offset) {
		// Adjust contrast of a BufferedImage
		RescaleOp op = new RescaleOp(scale, offset, null);
		op.filter(img, img);
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
		BufferedImage ret = new BufferedImage((x2 - x1) + 1, img.getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < ret.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				ret.setRGB(x, y, img.getRGB(x + x1, y));
			}
		}
		return ret;
	}

	public static final BufferedImage yCut(BufferedImage img, int y1, int y2) {
		// Cut an image vertically.
		BufferedImage ret = new BufferedImage(img.getWidth(), (y2 - y1) + 1, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < ret.getHeight(); y++) {
				ret.setRGB(x, y, img.getRGB(x, y + y1));
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
	public static final int[] getCenter(boolean[][] table) {
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
		return new int[] { (int) retX, (int) retY };
	}

	public static void writeImage(BufferedImage img, String path) throws IOException {
		// Write the image without any compression
		ImageWriter writer = ImageIO.getImageWritersByFormatName(path.substring(path.lastIndexOf(".") + 1)).next();
		ImageWriteParam param = writer.getDefaultWriteParam();
		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(1.0F);
		writer.setOutput(new FileImageOutputStream(new File(path)));
		writer.write(img);
	}

}
