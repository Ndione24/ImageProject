package TP3;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Egalisation_Image {

	public static void imshow(BufferedImage image) throws IOException {

		//Instantiate JFrame 
		JFrame frame = new JFrame(); 

		//Set Content to the JFrame 
		frame.getContentPane().add(new JLabel(new ImageIcon(image))); 
		frame.pack(); 
		frame.setVisible(true);

	}


	public static void main(String[] args) throws IOException {

		// TODO Auto-generated method stub
		File path = new File("C:\\Users\\boual\\OneDrive\\Bureau\\ImageL3-master\\Test_Images\\landscape.png");

		BufferedImage img = null;
		img = ImageIO.read(path);

		//		initialisation de l histogramme

		int histogrammeTab[] =new int[256] ;

		for(int i = 0 ; i < 256 ; i++) {
			histogrammeTab[i] = 0 ; 
		}

		//		remplir l histogramme avec l image d origine

		for( int x = 0 ; x < img.getHeight() ; x++) {
			for(int y = 0 ; y < img.getWidth() ; y++) {
				int p = img.getRGB(y,x); 
				int r = (p>>16)&0xff;
				histogrammeTab[r]++;
			}
		}


		//		afficher l histrogramme
		for(int i = 0 ; i<256 ; i++) {
			System.out.print(histogrammeTab[i] + " ");
		}

		System.out.println("");

		//		initialiser l histogramme cumuler

		int hist_cumule[] = new int[256];

		for(int t=0 ; t<256 ; t++) {
			hist_cumule[t]=0;
		}

		//remplir l'histogramme cumuler
		for(int y=0 ; y<256 ; y++) {

			for(int x=0 ; x<=y ; x++) {

				hist_cumule[y] += histogrammeTab[x]; 
			}

		}

		//		egaliser l image initiale
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

		//		afficher l histogramme cumuler
		for(int i = 0 ; i<256 ; i++) {
			System.out.print(hist_cumule[i] + " ");
		}

		//		construction de l histogramme cummuler

		BufferedImage img1 = new BufferedImage(256,100,BufferedImage.TYPE_INT_ARGB);

		for(int x = 0 ; x < 100 ; x++) {

			for(int y = 0 ; y < 256 ; y++) {

				img1.setRGB(y, x,Color.BLACK.getRGB());;				 
			}

		}
		imshow(img);

		int max =  hist_cumule[0] ; 

		for(int i=1 ; i < hist_cumule.length ; i++) {

			if(max < hist_cumule[i]) {
				max = hist_cumule[i];
			}

		}
		System.out.println(max);

		int hist_norm[] = new int[256];

		for(int t=0 ; t < 256 ; t++ ) {
			hist_norm[t] = ((hist_cumule[t]*100)/max);	
		}

		for(int i = 0 ; i<256 ; i++) {
			System.out.print(hist_norm[i] + " ");
		}
		System.out.println("");

		for(int y = 0 ; y < 256 ; y++) {
			for(int x = 100-hist_norm[y] ; x < 100 ; x++) {

				img1.setRGB(y, x, Color.WHITE.getRGB());

			}

		}
		imshow(img1);
		imshow(img);
		

	}


}
