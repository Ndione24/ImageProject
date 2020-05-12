package TP3;
import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.image.BufferedImage;
import java.util.Scanner;
import java.lang.Math;

public class Sobel {

	public static void imshow(BufferedImage image) throws IOException {

		//Instantiate JFrame 
		JFrame frame = new JFrame(); 

		//Set Content to the JFrame 
		frame.getContentPane().add(new JLabel(new ImageIcon(image))); 
		frame.pack(); 
		frame.setVisible(true);

	}

	public static int getGrayScale(int rgb) {

		int p = rgb;
		int r = (p >> 16) & 0xff;
		int g = (p >> 8) & 0xff;
		int b = p & 0xff;
		int l = (r + g + b) / 3;


		return l;
	}
	public static BufferedImage projection(BufferedImage i){

		int longeur  = i.getWidth();
		int largeur = i.getHeight();
		int[] pHor = new int[largeur];
		int[] pVer = new int[longeur];

		for(int v=0 ; v<largeur ; v++) {

			for(int u=0; u<longeur ; u++) {

				int p = i.getRGB(u,v);
				int r = (p>>16)&0xff;

				if(r != 0) {
					pHor[v]++;
					pVer[u]++;
				}
			//	System.out.println(pHor[v]);

			}

		}


		BufferedImage img1 = new BufferedImage(i.getWidth(), i.getHeight(), BufferedImage.TYPE_INT_ARGB);

		for(int x = 0 ; x < i.getHeight() ; x++) {

			for(int y = 0 ; y < i.getWidth() ; y++) {

				img1.setRGB(y, x,Color.WHITE.getRGB());;				 
			}

		}

				for(int x=0 ; x<i.getHeight();x++) {
					
					for(int y = 0 ; y < pHor[x] ; y++) {
		
						img1.setRGB(y, x,Color.BLACK.getRGB());
						
					}
				}

				return img1;

	}
	public static void main(String args[]) throws IOException{

		//		System.out.println("Enter the file name :");
		//		Scanner ne1 =new Scanner(System.in);
		//		String filename=ne1.nextLine();


		File file= new File("C:\\Users\\boual\\OneDrive\\Bureau\\3.jpg");
		
		BufferedImage image = ImageIO.read(file);

		int x=image.getWidth();
		int y=image.getHeight();

		int maxGval = 0;
		int[][] edgeColors = new int[x][y];
		int maxGradient = -1;

		for(int i=1;i<x-1;i++){

			for(int j=1;j<y-1;j++){

				int val00 = getGrayScale(image.getRGB(i - 1, j - 1));
				int val01= getGrayScale(image.getRGB(i-1,j));
				int val02= getGrayScale(image.getRGB(i-1,j+1));

				int val10= getGrayScale(image.getRGB(i,j-1));
				int val11= getGrayScale(image.getRGB(i,j));
				int val12= getGrayScale(image.getRGB(i,j+1));

				int val20= getGrayScale(image.getRGB(i+1,j-1));
				int val21= getGrayScale(image.getRGB(i+1,j));
				int val22= getGrayScale(image.getRGB(i+1,j+1));

				int gx=(((-1*val00)+(0*val01)+(1*val02))+((-2*val10)+(0*val11)+(2*val12))+((-1*val20)+(0*val21)+(1*val22)));
				int gy=(((-1*val00)+(-2*val01)+(-1*val02))+((0*val10)+(0*val11)+(0*val12))+((1*val20)+(2*val21)+(1*val22)));

				double gval=Math.sqrt((gx*gx)+(gy*gy));
				int g=(int)gval;

				if (maxGradient < g) {
					maxGradient = g;
				}

				edgeColors[i][j] = g;

			}
		}

		double scale = 255.0 / maxGradient;

		for (int i = 1; i < x - 1; i++) {
			for (int j = 1; j < y - 1; j++) {
				int edgeColor = edgeColors[i][j];
				edgeColor = (int)(edgeColor * scale);
				edgeColor = 0xff000000 | (edgeColor << 16) | (edgeColor << 8) | edgeColor;

				image.setRGB(i, j, edgeColor);
			}
		}

		File outputfile = new File("sobel.png");
		ImageIO.write(image, "png", outputfile);
		imshow(image);
		imshow(projection(image));
		System.out.println("max : " + maxGradient);
		System.out.println("Finished");
	}
}



