import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class HistogrammeDeProjection_V2  {


	public static void imshow(BufferedImage image) throws IOException {

		//Instantiate JFrame 
		JFrame frame = new JFrame(); 

		//Set Content to the JFrame 
		frame.getContentPane().add(new JLabel(new ImageIcon(image))); 
		frame.pack(); 
		frame.setVisible(true);
	}

	
	/**
	 * il permet de creer l histogramme de projection horizontal et vertical
	 * @param i l image pour laquelle on va creer l histogramme
	 * @throws IOException
	 */
	public static void projection(BufferedImage i) throws IOException {

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
				System.out.println(pHor[v]);

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

		imshow(img1);

	}

	public static void main(String[] args) throws IOException {


		File path = new File("C:\\Users\\boual\\OneDrive\\Bureau\\doc4.jpg");
		BufferedImage img = null;
		img = ImageIO.read(path);

//		binariser l image 
		int[] couleurNoir = {0,0,0,255}; 
		int[] couleurBlanc = {255,255,255,255};

		for(int x=0 ; x < img.getHeight() ; x++) {
			for(int y = 0 ; y < img.getWidth() ; y++) {
				int p = img.getRGB(y, x);
				int r = (p>>16)&0xff;
				if(r>150) {
					img.getRaster().setPixel(y, x, couleurBlanc);
				}else {
					img.getRaster().setPixel(y, x, couleurNoir);
				}
			}
		}

		projection(img);

		imshow(img);
	}


}
