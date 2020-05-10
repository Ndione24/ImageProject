package connexite;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Test {

	public static void main(String[] args) {
		
		int nombre;
		File path = new File("bdd\\escalier1.JPG");
		BufferedImage img = null;
		
		try {
			img = ImageIO.read(path);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
		boolean EightConnex = true ; // On travaille en 8-connexité.
		
		ConnectedComponentLabeling ccl = new Connexite() ;
		//nombre = ccl.NumberOfConnectedComponent();
		 ccl.Label(img, 0, EightConnex) ; // On calcule (étiquette) les composantes connexes.  On ne prend pas en compte la couleur noire car c'est le fond. Mettre -1 pour caractériser TOUTE la texture.
		int[][] Carte = ccl.Labels() ; // la carte contenant la numérotation de chaque composante.
		int[] Sizes = ccl.Sizes() ;
		nombre = ccl.NumberOfConnectedComponent();		
		System.out.println("nombre = "+nombre);
		
	}

}
