package connexite;
import java.awt.image.BufferedImage;
import java.util.List;


public interface ConnectedComponentLabeling {
	
	

	

	/**
	 * <p>Description : Interface qui permet de savoir si une classe peut calculer le nombre de composantes connexes
	 * presentes dans une image. Il est conseille d'utiliser FifoCcl ou IterativeCcl.<br>
	 * UnionFindCcl est tres lent et RecursiveCcl genere un StackOverFlow si une composante est trop grande.</p>
	 * <p>Package(s) required: imageTiTi, utils.</p>
	 * <p>Copyright: Copyright (c) 2007-2011.</p>
	 * <p>Laboratories/Teams: CMM (Mines-ParisTech/ENSMP), I&M (ex LXAO) LSIS.</p>
	 * <p>Updates:<br>
	 * 11 Avril 2010 => Modification des methodes Separate* afin de pouvoir eventuellement passer une image a decouper.<br>
	 * 19 Janvier 2010, 1.1 => Ajout des methodes Label(*) avec pour types d'entree : DV, int[][] et int[][][].<br>
	 * 06 Aout 2009 => Creation.</p>
	 * 
	 * @author Guillaume THIBAULT
	 * @version 1.1
	 */

	
	


	/** Calcule le nombre de composantes connexes dans l'image.
	 * @param Original L'image dans laquelle on doit compter les composantes connexes.
	 * @param Background Couleur du fond, donc a ne pas prendre en compte.
	 * @param EightConnex Booleen qui permet de savoir si le calcul se fait en quatre ou huit connexites.
	 * @param Chrono Le chronometre pour mesurer la duree.
	 * @return Le nombre de composantes connexes de l'image.*/
	public int Label(BufferedImage Original, int Background, boolean EightConnex);

	/** Calcule le nombre de composantes connexes dans l'image.
	 * @param Original Le tableau dans lequel on doit compter les composantes connexes.
	 * @param Background Couleur du fond, donc a ne pas prendre en compte.
	 * @param EightConnex Booleen qui permet de savoir si le calcul se fait en quatre ou huit connexites.
	 * @param Chrono Le chronometre pour mesurer la duree.
	 * @return Le nombre de composantes connexes de l'image.*/
	public int Label(int[][] Original, int Background, boolean EightConnex) ;

	/** Calcule le nombre de composantes connexes dans l'image mais ne compte que les composantes ayant une surface superieure a
	 *  Threshold.
	 * @param Threshold Surface minimum que doit avoir une composante connexe afin d'etre comptabilisee. 
	 * @return Le nombre de composantes connexes de l'image.*/
	public int NumberOfConnectedComponent(int Threshold) ;


	/** Methode qui renvoit le nombre de composantes connexes denombrees lors du dernier appel de la methode Label.
	 * @return Le nombre de composantes.*/
	public int NumberOfConnectedComponent() ;


	/** Method which return array of labels.
	 * @return Array.*/
	public int[][] Labels() ;


	/** Method which return sizes of connected components.
	 * @return Array.*/
	public int[] Sizes() ;
	//int Label(BufferedImage Original, int BackGround, boolean EightConnex);
	//int Label(BufferedImage Original, int BackGround, boolean EightConnex);

	int Label(BufferedImage Original, int Background);
	

	}

	



