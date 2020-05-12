package connexite;

//import td03.Exo2;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class TestJM {

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
	
	/*
	public static BufferedImage divideImage(BufferedImage img) 
	{
		int height=img.getHeight();
		int width=img.getWidth();
		
	} */

	public static int[][] arrayImageBinaire(BufferedImage img) throws IOException {
		int arrayBinaire[][];
		
		File textBinaire= new File("bdd\\binaire.txt");
		textBinaire.createNewFile();
		BufferedWriter bw=new BufferedWriter(new FileWriter(textBinaire));	
		BufferedImage result = img;
		int height = result.getHeight();
		int width = result.getWidth();
		arrayBinaire = new int[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int p = result.getRGB(j, i);
				int r = p >> 16 & 0xff;
				int v = p >> 8 & 0xff;
				int b = p & 0xff;
				
				// System.out.println("r="+r+" p="+p +" ble"+b+" v"+v);
				
				arrayBinaire[i][j] = r;
			}
		}

		//System.out.print("+---+---+---+---+---+---+---+\n");
		//System.out.print("|");
		String bar="+---";
		for(int o=0;o<20;o++) 
		{
			bar+="+---";
		}
		for (int i = 0; i < 20; i++) {
			//System.out.print("+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+\n");
			System.out.println(bar);
			System.out.print( " " + " " + "|");
			for (int j = 0; j < 20; j++) {
				
				if(arrayBinaire[i][j]==255) {
					System.out.print(1 + " " + "|");
				}
				else {
					System.out.print(0 + " " + "|");
				}
				
			}
			System.out.print("\n");
		}

		return arrayBinaire;

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
		// egalisation(imgris);
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
		// System.out.println(threshold);
		return threshold;
	}
	public static void egalisation(BufferedImage img) throws IOException {
		
		//	initialisation de l histogramme

		int histogrammeTab[] =new int[256] ;

		for(int i = 0 ; i < 256 ; i++) {
			histogrammeTab[i] = 0 ; 
		}

		//	remplir l histogramme avec l image d origine

		for( int x = 0 ; x < img.getHeight() ; x++) {
			for(int y = 0 ; y < img.getWidth() ; y++) {
				int p = img.getRGB(y,x); 
				int r = (p>>16)&0xff;
				histogrammeTab[r]++;
			}
		}


		//	initialiser l histogramme cumuler

		int hist_cumule[] = new int[256];

		for(int t=0 ; t<256 ; t++) {
			hist_cumule[t]=0;
		}

		//	remplir l'histogramme cumuler
		for(int y=0 ; y<256 ; y++) {

			for(int x=0 ; x<=y ; x++) {

				hist_cumule[y] += histogrammeTab[x]; 
			}

		}

		//	egaliser l image initiale
		int nivGris[] = new int[3];
		for(int x=0 ; x<img.getHeight() ; x++) {
			for(int y=0 ; y<img.getWidth() ; y++) {
				int p = img.getRGB(y, x);
				int r = (p>>16)&0xff; 
				int j = (((256*(hist_cumule[r]-1))/(img.getHeight()*img.getWidth())));
				nivGris[0] = j;
				nivGris[1] = j;
				nivGris[2] = j;
				img.getRaster().setPixel(y, x,nivGris);				
			}
		}
		
		imshow(img);

	}

	/**
	 * La dilation permet de boucher les trous de l'objet, autrement dit augmente la
	 * taille de l'image
	 * 
	 * @param filtre : filtre qui sera appliqué à l'image
	 * @param img    : l'image qui sera filtrée
	 * @return une image filtrée
	 */
	public static BufferedImage dilatation(int filtre[][], BufferedImage img) {
		return null;
	}
	
	public static BufferedImage sobel() 
	{
		return null;
	}
	
	
	/**
	 * il permet de creer l histogramme de projection horizontal et vertical
	 * @param i l image pour laquelle on va creer l histogramme
	 * @throws IOException
	 */
	
	public static BufferedImage projection(BufferedImage i) throws IOException {

		int longeur  = i.getWidth();
		int largeur = i.getHeight();
		int[] pHor = new int[largeur];
		int[] pVer = new int[longeur];

		for(int v=0 ; v<largeur ; v++) {

			for(int u=0; u<longeur ; u++) {

				int p = i.getRGB(u,v);
				int r = (p>>16)&0xff;

				if(r != 255) {
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
	
    /**
     * Crops an image to the specified region
     * @param bufferedImage the image that will be crop
     * @param x the upper left x coordinate that this region will start
     * @param y the upper left y coordinate that this region will start
     * @param width the width of the region that will be crop
     * @param height the height of the region that will be crop
     * @return the image that was cropped.
     * @throws IOException 
     */
    public static BufferedImage cropImage(BufferedImage bufferedImage, int x, int y, int width, int height){
        BufferedImage croppedImage = bufferedImage.getSubimage(x, y, width, height);
        return croppedImage;
    } 
    
    
	/*

	public static BufferedImage projection(BufferedImage i) throws IOException {

		int longeur  = i.getWidth();
		int largeur = i.getHeight();
		int[] pHor = new int[largeur];
		int[] pVer = new int[longeur];

		for(int v=0 ; v<largeur ; v++) {

			for(int u=0; u<longeur ; u++) {

				int p = i.getRGB(u, v);
				int r = (p>>16)&0xff;

				if(r != 255) {
					pHor[v]++;
					pVer[u]++;
				}
				//System.out.println(pHor[v]);

			}

		}

		//System.out.println(pHor[5]);

		BufferedImage img1 = new BufferedImage(i.getWidth(), i.getHeight(), BufferedImage.TYPE_INT_ARGB);

		for(int x = 0 ; x < i.getHeight() ; x++) {

			for(int y = 0 ; y < pHor[x] ; y++) {

				img1.setRGB(y, x,Color.BLACK.getRGB());;				 
			}

		}

				for(int x=0 ; x<i.getHeight();x++) {
					
					for(int y = 0 ; y < pHor[y] ; y++) {
		
						
						img1.setRGB(y, x,Color.WHITE.getRGB());;				 
					}
				}

				return img1;

	}
*/
    
    public static int analyse(String pathImage, int numAlgo) throws IOException, InterruptedException 
    {
		
		File path = new File(pathImage); // Chemin de l'image

		BufferedImage img1 = null;
		BufferedImage img2 = null;
		try {
			img1 = ImageIO.read(path);
			img2 = ImageIO.read(path);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
		//imshow(img1);
		BufferedImage res,tet;
        	res=nvgris(img2);
        	tet=nvgris(img2);
		
		if(numAlgo==1) 
		{
        	
			res = exo3bin(tet);
			 filtremedian(res);
			BufferedImage coloredescaliers = Label8.getCC(res);
			imshow(coloredescaliers);
			return Label8.getNumberOfCC(coloredescaliers);
		}
		
		else if(numAlgo==2) {
//		egalisation(img2);
		filtremedian(res);
		res = exo3bin(tet);
		imshow(res);
		tet=projection(res);
		//imshow(tet);
        BufferedImage imgCrop= cropImage(tet, 300, 0, tet.getWidth()-300, tet.getHeight());
        //imshow(imgCrop);
		boolean EightConnex = true ; // On travaille en 8-connexité.
		ConnectedComponentLabeling ccl = new Connexite() ;
		//nombre = ccl.NumberOfConnectedComponent();
		 ccl.Label(imgCrop, 255, EightConnex) ;
		int[][] Carte = ccl.Labels() ; 
		int[] Sizes = ccl.Sizes() ;
		int nombre = ccl.NumberOfConnectedComponent();
	
		System.out.println("nombre = "+nombre);
		//arrayImageBinaire(res);
		return nombre;
		}
		else 
		{
			return -1;
		}
    }

	public static void main(String[] args) throws IOException, InterruptedException {
		String imgPath = "bdd//myImg.JPG";
		String imgPath0 = "bdd//myImgCrop.JPG";
		String imgPath4 = "bdd//myImgCrop2.JPG";
		String imgPath5 = "bdd//myImgCrop3.JPG";
		String imgPath1 = "bdd//escalier1.JPG";
		String imgPath2 = "bdd//escalier2.JPG";
		String imgPath3 = "bdd//escalier3.JPG";
		String imgPath6="bdd//myImgF.JPG";
		String imgPath7="bdd//imagMourad.JPG";
		String imgPath8="bdd//im3.JPG";
		String imgPath9="bdd//imag5.JPG";
		
		File path = new File(imgPath8); // Chemin vers l'image

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
		BufferedImage res,tet;
		res = nvgris(img2);
		tet=nvgris(img2);		
		imshow(res);
		Thread.sleep(5000);
		filtremedian(res);
		res = exo3bin(tet);
		imshow(res);
		
		/*tet=projection(res);
		imshow(tet);
        BufferedImage imgCrop= cropImage(tet, 300, 0, tet.getWidth()-300, tet.getHeight());
        imshow(imgCrop);*/
		/*boolean EightConnex = true ; // On travaille en 8-connexité.
		ConnectedComponentLabeling ccl = new Connexite() ;
		//nombre = ccl.NumberOfConnectedComponent();
		 ccl.Label(imgCrop, 255, EightConnex) ; // On calcule (étiquette) les composantes connexes.  On ne prend pas en compte la couleur noire car c'est le fond. Mettre -1 pour caractériser TOUTE la texture.
		int[][] Carte = ccl.Labels() ; // la carte contenant la numérotation de chaque composante.
		int[] Sizes = ccl.Sizes() ;
		int nombre = ccl.NumberOfConnectedComponent();
		*/
		//System.out.println("nombre = "+nombre);
		
		
	}
}
