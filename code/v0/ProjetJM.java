package projet;

import td03.Exo2;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ProjetJM {

	public static void imshow(BufferedImage image) throws IOException {
		// Instantiate JFrame
		JFrame frame = new JFrame();

		// Set Content to the JFrame
		frame.getContentPane().add(new JLabel(new ImageIcon(image)));
		frame.pack();
		frame.setVisible(true);
	}

	// Code pour lisser l'image

	public static BufferedImage filtremedian(BufferedImage img) {
		Color[] pixel = new Color[9];
		int[] R = new int[9];
		int[] B = new int[9];
		int[] G = new int[9];
		for (int i = 1; i < img.getWidth() - 1; i++) {
			for (int j = 1; j < img.getHeight() - 1; j++) {
				pixel[0] = new Color(img.getRGB(i - 1, j - 1));
				pixel[1] = new Color(img.getRGB(i - 1, j));
				pixel[2] = new Color(img.getRGB(i - 1, j + 1));
				pixel[3] = new Color(img.getRGB(i, j + 1));
				pixel[4] = new Color(img.getRGB(i + 1, j + 1));
				pixel[5] = new Color(img.getRGB(i + 1, j));
				pixel[6] = new Color(img.getRGB(i + 1, j - 1));
				pixel[7] = new Color(img.getRGB(i, j - 1));
				pixel[8] = new Color(img.getRGB(i, j));
				for (int k = 0; k < 9; k++) {
					R[k] = pixel[k].getRed();
					B[k] = pixel[k].getBlue();
					G[k] = pixel[k].getGreen();
				}
				Arrays.sort(R);
				Arrays.sort(G);
				Arrays.sort(B);
				img.setRGB(i, j, new Color(R[4], B[4], G[4]).getRGB());
			}
		}
		return img;
	}

	// code pour binariser l'image

	public static BufferedImage exo3bin(BufferedImage imgbin) throws IOException {

		int width = imgbin.getWidth();
		int height = imgbin.getHeight();

		int noir[] = { 0, 0, 0, 255 };
		int blanc[] = { 255, 255, 255, 255 };
		float teta = otsu1979(imgbin);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int p = imgbin.getRGB(j, i);
				int r = (p >> 16) & 0xff;
				if (r < teta) {
					imgbin.getRaster().setPixel(j, i, noir);
				} else {
					imgbin.getRaster().setPixel(j, i, blanc);
				}
			}
		}

		return imgbin;

	}

	// code pour mettre l'image en niveau de gris

	public static BufferedImage nvgris(BufferedImage imgris) throws IOException {

		int width = imgris.getWidth();
		int height = imgris.getHeight();

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int p = imgris.getRGB(j, i);
				int r = (p >> 16) & 0xff;
				int g = (p >> 8) & 0xff;
				int b = p & 0xff;
				int l = (r + g + b) / 3;
				int tab[] = { l, l, l };
				imgris.getRaster().setPixel(j, i, tab);

			}
		}
		//egalisation(imgris);
		return imgris;
	}

	public static float otsu1979(BufferedImage imgris) {
		int width = imgris.getWidth();
		int height = imgris.getHeight();

		int histData[] = new int[256];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int p = imgris.getRGB(i, j);
				int r = (p >> 16) & 0xff;
				histData[r]++;

			}
		}

		// Total number of pixels
		int total = width * height;

		float sum = 0;
		for (int t = 0; t < 256; t++)
			sum += t * histData[t];

		float sumB = 0;
		int wB = 0;
		int wF = 0;

		float varMax = 0;
		float threshold = 0;

		for (int t = 0; t < 256; t++) {
			wB += histData[t]; // Weight Background
			if (wB == 0)
				continue;

			wF = total - wB; // Weight Foreground
			if (wF == 0)
				break;

			sumB += (float) (t * histData[t]);

			float mB = sumB / wB; // Mean Background
			float mF = (sum - sumB) / wF; // Mean Foreground

			// Calculate Between Class Variance
			float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

			// Check if new maximum found
			if (varBetween > varMax) {
				varMax = varBetween;
				threshold = t;
			}
		}
		System.out.println(threshold);
		return threshold;
	}
	

	public static void egalisation(BufferedImage img) {

		int width = img.getWidth();
		int height = img.getHeight();
		int totalpixel = width * height;
		int[] histogram = new int[255];
		int[] iarray = new int[1];
		int i = 0;

		// read pixel intensities into histogram
		for (int x = 1; x < width; x++) {
			for (int y = 1; y < height; y++) {
				int valueBefore = img.getRaster().getPixel(x, y, iarray)[0];
				histogram[valueBefore]++;
			}
		}

		int sum = 0;
		// build a Lookup table LUT containing scale factor
		float[] lut = new float[256];
		for (i = 0; i < 255; ++i) {
			sum += histogram[i];
			lut[i] = sum * 255 / totalpixel;
		}

		// transform image using sum histogram as a Lookup table
		for (int x = 1; x < width; x++) {
			for (int y = 1; y < height; y++) {
				int valueBefore = img.getRaster().getPixel(x, y, iarray)[0];
				int valueAfter = (int) lut[valueBefore];
				iarray[0] = valueAfter;
				img.getRaster().setPixel(x, y, iarray);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		File path = new File("/Users/Jaym/Documents/Projet Image/escalier7.jpg"); //Chemin vers l'image

		BufferedImage img1 = null;
		BufferedImage img2 = null;
		try {
			img1 = ImageIO.read(path);
			img2 = ImageIO.read(path);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		imshow(img1);
		BufferedImage res;
		res = nvgris(img2);
		res = exo3bin(res);

		filtremedian(res);

		imshow(res);

	}

}
