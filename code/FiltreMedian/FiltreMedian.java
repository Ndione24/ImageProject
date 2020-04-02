package projet;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;



public class FiltreMedian {

	public static void imshow(BufferedImage image) throws IOException {
	      //Instantiate JFrame 
	      JFrame frame = new JFrame(); 

	      //Set Content to the JFrame 
	      frame.getContentPane().add(new JLabel(new ImageIcon(image))); 
	      frame.pack(); 
	      frame.setVisible(true);
	 }
	
	//Code pour lisser l'image
	
	public static BufferedImage filtremedian(BufferedImage img) {
		Color[] pixel=new Color[9];
        int[] R=new int[9];
        int[] B=new int[9];
        int[] G=new int[9];
        for(int i=1;i<img.getWidth()-1;i++) {
            for(int j=1;j<img.getHeight()-1;j++)
            {
               pixel[0]=new Color(img.getRGB(i-1,j-1));
               pixel[1]=new Color(img.getRGB(i-1,j));
               pixel[2]=new Color(img.getRGB(i-1,j+1));
               pixel[3]=new Color(img.getRGB(i,j+1));
               pixel[4]=new Color(img.getRGB(i+1,j+1));
               pixel[5]=new Color(img.getRGB(i+1,j));
               pixel[6]=new Color(img.getRGB(i+1,j-1));
               pixel[7]=new Color(img.getRGB(i,j-1));
               pixel[8]=new Color(img.getRGB(i,j));
               for(int k=0;k<9;k++){
                   R[k]=pixel[k].getRed();
                   B[k]=pixel[k].getBlue();
                   G[k]=pixel[k].getGreen();
               }
               Arrays.sort(R);
               Arrays.sort(G);
               Arrays.sort(B);
               img.setRGB(i,j,new Color(R[4],B[4],G[4]).getRGB());
            }
        }
        return img ;
	}
	
	//code pour binariser l'image
	
	public static BufferedImage exo3bin(BufferedImage imgbin) throws IOException {

		
		int width  = imgbin.getWidth();
		int height = imgbin.getHeight();
		
		int noir[] = {0,0,0,255} ;
		int blanc[] = {255,255,255,255} ;
		for(int i=0 ; i<height ; i++) {
			for(int j=0 ; j<width ; j++) {
				int p = imgbin.getRGB(j,i);
				int r = (p>>16)&0xff;
				if(r<225) {
					imgbin.getRaster().setPixel(j, i, noir);
				}
				else {
					imgbin.getRaster().setPixel(j, i, blanc);
				}
			}
		}
		
		return imgbin ;
		
	}
	
	//code pour mettre l'image en niveau de gris
	
	public static BufferedImage nvgris(BufferedImage imgris) throws IOException {
		
		int width  = imgris.getWidth();
		int height = imgris.getHeight();
		
		for(int i=0 ; i<height ; i++) {
			for(int j=0 ; j<width ; j++) {
				int p = imgris.getRGB(j,i);
				int r = (p>>16)&0xff; 
		        int g = (p>>8)&0xff; 
		        int b = p&0xff; 
		        int l = (r + g + b)/3 ;
		        int tab[] = {l,l,l} ;
		        imgris.getRaster().setPixel(j, i, tab );
				
			}
		}
		return imgris ;
	}
	
	public static void main(String[] args) throws IOException {
		File path = new File("/Users/Jaym/Documents/ImageL3-master/Test_Images/shapesGray.jpg");

		BufferedImage img = null;
		try {
			img = ImageIO.read(path);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		BufferedImage res = filtremedian(img) ;
		
		imshow(res) ;
		
	}


}
