package connexite;
import java.awt.image.BufferedImage;

import java.awt.image.WritableRaster;
import java.util.List;
import java.util.Vector;



/*import utils.arrays.ArraysOperations;
import utils.times.Chronometer;*/


public class Connexite implements ConnectedComponentLabeling
{
	
	
	/**
	 * <p>Description : Cette classe propose des methodes qui permettent de calculer le nombre de trous et de composantes connexes dans une image.
	 *  Cette classe utilise un algorithme iteratif pour le calcul.</p>
	 * <p>Package(s) required: imageTiTi, utils.</p>
	 * <p>Copyright: Copyright (c) 2007-2011.</p>
	 * <p>Laboratories/Teams: CMM (Mines-ParisTech/ENSMP), I&M (ex LXAO) LSIS.</p>
	 * <p>Updates:<br>
	 * 17 Janvier 2010, 1.3 => Ajout des methodes Label(DV, ...) et Label(int[][][], ...).<br>
	 * 6 Aout 2009, 1.2 => Refonte complete avec la plateforme. Ajout de l'interface ConnectedComponentLabeling et separations des methodes en classes.<br>
	 * 13 Avril 2008 => Ajout de la methode IsolerComposantes3.<br>
	 * 02 Juillet 2007 => Creation.</p>
	 * 
	 * @author Guillaume THIBAULT
	 * @version 1.3
	 */



	/** La derniere image sur laquelle on a compte le nombre de composantes.*/
	protected BufferedImage source = null ;

	/** Le tableau contenant la numerotation des composantes.*/
	protected int[][] Labels = null ;

	/** Tableau contenant les tailles des composantes connexes.*/
	protected int[] Sizes = null ;

	/** Nombre de composantes connexes denombrees lors du dernier appel de la methode Label.*/
	protected int Counter = -1 ;


	protected int[] ComposanteIdentiques ;
	protected int Increment = 5000 ;






	protected int TrouverEquivalence(int Equi)
		{
		if ( Equi >= ComposanteIdentiques.length ) IncreaseAlloc(Equi) ;
		if ( (Equi == 1) || (ComposanteIdentiques[Equi] == 0) ) return Equi ;
		else return TrouverEquivalence( ComposanteIdentiques[Equi] ) ;
		}


	protected void IncreaseAlloc(int N)
		{
		int[] Tampon = ComposanteIdentiques.clone() ;
		ComposanteIdentiques = null ;
		ComposanteIdentiques = new int[N+Increment] ;
		for (int i=0 ; i < Tampon.length ; i++) ComposanteIdentiques[i] = Tampon[i] ;
		for (int i=Tampon.length ; i < ComposanteIdentiques.length ; i++) ComposanteIdentiques[i] = 0 ;
		Tampon = null ;
		}



		
	/** Calcule le nombre de composantes connexes dans l'image binaire.
	 * @param Original L'image dans laquelle on doit compter les composantes connexes.
	 * @param BackGround Couleur du fond, donc tout ce qui n'est pas des composantes.
	 * @param EightConnex Booleen qui permet de savoir si le calcul se fait en quatre ou huit connexites.
	 * @param Chrono Le chronometre pour mesurer la duree.
	 * @return Le nombre de composantes connexes de l'image.*/
	
	@Override
	public int Label(BufferedImage Original, int BackGround, boolean EightConnex)
		{
		//if ( ImageTools.isColored(Original) ) throw new IllegalArgumentException("Only binary or gray level images supported.") ;
		
		this.source = Original ;
		int i, j, x, y, Color, marker = 0 ;
		int largeur = Original.getWidth() ;
		int hauteur = Original.getHeight() ;
		int Trouve1, Trouve2, Equi1, Equi2 ;
		boolean Fin = true ;
		WritableRaster wr = Original.getRaster() ;
		
		/*if ( Chrono != null )
			{
			System.out.print("Number of connected components computed with iterative algorithm: ") ;
			marker = Chrono.setMarker() ;
			}*/
		
		Labels = null ;
		Labels = new int[hauteur][largeur] ;
		
		ComposanteIdentiques = null ;
		ComposanteIdentiques = new int[Increment] ;
		for (i=0 ; i < hauteur ; i++)
			for (j=0 ; j < largeur ; j++)
				if ( wr.getSample(j, i, 0) == BackGround ) Labels[i][j] = 0 ;
				else Labels[i][j] = -1 ;

		Counter = 0 ;
		for (i=0 ; i < hauteur ; i++)
			for (j=0 ; j < largeur ; j++)
				if ( Labels[i][j] == -1 )
					{
					Color = wr.getSample(j, i, 0) ;
					Trouve1 = Trouve2 = 0 ; // On vŽrifie les deux voisins

					if ( (i > 0) && (wr.getSample(j, i-1, 0) == Color) ) // Au dessus
						Trouve1 = Labels[i-1][j] ;
					if ( (j > 0) && (wr.getSample(j-1, i, 0) == Color) ) // A gauche
						Trouve2 = Labels[i][j-1] ;

					if ( EightConnex ) // Si on compte en huit connexitŽ
						{
						if ( (i > 0) && (j > 0) && (Trouve1 == 0) && (wr.getSample(j-1, i-1, 0) == Color) ) Trouve1 = Labels[i-1][j-1] ;
						else if ( (i > 0) && (j > 0) && (Trouve2 == 0) && (wr.getSample(j-1, i-1, 0) == Color) ) Trouve2 = Labels[i-1][j-1] ;
						else if ( (i > 0) && (j < largeur-1) && (Trouve1 ==0) && (wr.getSample(j+1, i-1, 0) == Color) ) Trouve1 = Labels[i-1][j+1] ;
						else if ( (i > 0) && (j < largeur-1) && (Trouve2 ==0) && (wr.getSample(j+1, i-1, 0) == Color) ) Trouve2 = Labels[i-1][j+1] ;
						}
					
					if ( (Trouve1 == 0) && (Trouve2 == 0) ) /* Aucun voisin n'est bon */
						Labels[i][j] = ++Counter ;

					if ( (Trouve1 != 0) && (Trouve2 == 0) ) /* Un seul des deux */
						Labels[i][j] = Trouve1 ;
					if ( (Trouve1 == 0) && (Trouve2 != 0) )
						Labels[i][j] = Trouve2 ;

					if ( (Trouve1 != 0) && (Trouve2 != 0) && (Trouve1 == Trouve2) ) /* Meme composante voisine */
						Labels[i][j] = Trouve1 ;

					if ( (Trouve1 != 0) && (Trouve2 != 0) && (Trouve1 != Trouve2) ) /* Tous les deux differents */
						{
						Fin = false ;
						Equi1 = TrouverEquivalence(Trouve1) ; /* On cherche l'origine de la composante */
						Equi2 = TrouverEquivalence(Trouve2) ;
						if ( (Equi1 < Equi2) || ((Equi1 == Equi2) && (Trouve1 < Trouve2)) )
							{
							Labels[i][j] = Equi1 ;
							ComposanteIdentiques[Trouve2] = Equi1 ;
							}
						else
							{
							Labels[i][j] = Equi2 ;
							ComposanteIdentiques[Trouve1] = Equi2 ;
							}
						}
					}

		while ( Fin == false ) /* On reactualise les equivalences */
				{
				Fin = true ;
				if ( Counter > ComposanteIdentiques.length ) IncreaseAlloc(Counter) ;
				for (i=1 ; i <= Counter ; i++)
					if ( ComposanteIdentiques[i] != 0 ) /* Il a un equivalent */
						if ( (ComposanteIdentiques[ComposanteIdentiques[i]] != 0) /* Si le prede...  a aussi un equivalent */
							&& (ComposanteIdentiques[ComposanteIdentiques[i]] != ComposanteIdentiques[i]) )
							{
							Fin = false ;
							ComposanteIdentiques[i] = ComposanteIdentiques[ComposanteIdentiques[i]] ;
							}
				}

		for (i=0 ; i < hauteur ; i++) /* On remet les bonnes valeurs */
			for (j=0 ; j < largeur ; j++)
				if ( Labels[i][j] > 0 )
					{
					if ( Labels[i][j] >= ComposanteIdentiques.length ) IncreaseAlloc(Labels[i][j]) ;
					if (ComposanteIdentiques[Labels[i][j]] != 0 )
						Labels[i][j] = ComposanteIdentiques[ Labels[i][j] ] ;
					}

		for (i=0 ; i <= Counter ; i++) /* On reinitialise */
			ComposanteIdentiques[i] = 0 ;

		for (i=0 ; i < hauteur ; i++) /* On note les composantes trouves */
			for (j=0 ; j < largeur ; j++)
				if ( Labels[i][j] > 0 ) ComposanteIdentiques[ Labels[i][j] ] = 1 ;

		Trouve1 = 0 ;
		for (i=1 ; i <= Counter ; i++) /* On renumerote les composantes trouves de maniere consecutive dans l'ordre */
			if ( ComposanteIdentiques[i] == 1 )
				ComposanteIdentiques[i] = ++Trouve1 ;

		for (i=0 ; i < hauteur ; i++) /* On les remplaces par les nouveaux numeros */
			for (j=0 ; j < largeur ; j++)
				if ( (Labels[i][j] > 0) && (ComposanteIdentiques[Labels[i][j]] != 0) )
					Labels[i][j] = ComposanteIdentiques[ Labels[i][j] ] ;

		Counter = Trouve1 ;
		
		
		Sizes = null ; // On remplit le tableau Sizes.
		Sizes = new int[Counter+1] ;
		for (y=0 ; y < hauteur ; y++)
			for (x=0 ; x < largeur ; x++)
				if ( Labels[y][x] > 0 )
					Sizes[Labels[y][x]]++ ;
		
		
		/*if ( Chrono != null )
			{
			System.out.println(Chrono.getTimeSinceMarker(marker)) ;
			Chrono.FreeMarker(marker) ;
			}*/
			
		return Counter ;
		}



	/** Calcule le nombre de composantes connexes dans l'image.
	 * @param Original Le tableau dans lequel on doit compter les composantes connexes.
	 * @param BackGround Couleur du fond, donc a ne pas prendre en compte.
	 * @param EightConnex Booleen qui permet de savoir si le calcul se fait en quatre ou huit connexites.
	 * @param Chrono Le chronometre pour mesurer la duree.
	 * @return Le nombre de composantes connexes de l'image.*/
	public int Labels(int[][] Original, int BackGround, boolean EightConnex)
		{	
		int i, j, x, y, Color, marker = 0 ;
		int largeur = Original[0].length ;
		int hauteur = Original.length ;
		int Trouve1, Trouve2, Equi1, Equi2 ;
		boolean Fin = true ;
		
		/*if ( Chrono != null )
			{
			System.out.print("Number of connected components computed with iterative algorithm: ") ;
			marker = Chrono.setMarker() ;
			}*/
		
		Labels = null ;
		Labels = new int[hauteur][largeur] ;
		
		ComposanteIdentiques = null ;
		ComposanteIdentiques = new int[Increment] ;
		//ArraysOperations.SetConstant(ComposanteIdentiques, 0) ;

		for (i=0 ; i < hauteur ; i++)
			for (j=0 ; j < largeur ; j++)
				if ( Original[i][j] == BackGround ) Labels[i][j] = 0 ;
				else Labels[i][j] = -1 ;

		Counter = 0 ;
		for (i=0 ; i < hauteur ; i++)
			for (j=0 ; j < largeur ; j++)
				if ( Labels[i][j] == -1 )
					{
					Color = Original[i][j] ;
					Trouve1 = Trouve2 = 0 ; // On vŽrifie les deux voisins

					if ( (i > 0) && (Original[i-1][j] == Color) ) // Au dessus
						Trouve1 = Labels[i-1][j] ;
					if ( (j > 0) && (Original[i][j-1] == Color) ) // A gauche
						Trouve2 = Labels[i][j-1] ;

					if ( EightConnex ) // Si on compte en huit connexitŽ
						{
						if ( (i > 0) && (j > 0) && (Trouve1 == 0) && (Original[i-1][j-1] == Color) ) Trouve1 = Labels[i-1][j-1] ;
						else if ( (i > 0) && (j > 0) && (Trouve2 == 0) && (Original[i-1][j-1] == Color) ) Trouve2 = Labels[i-1][j-1] ;
						else if ( (i > 0) && (j < largeur-1) && (Trouve1 ==0) && (Original[i-1][j+1] == Color) ) Trouve1 = Labels[i-1][j+1] ;
						else if ( (i > 0) && (j < largeur-1) && (Trouve2 ==0) && (Original[i-1][j+1] == Color) ) Trouve2 = Labels[i-1][j+1] ;
						}
					
					if ( (Trouve1 == 0) && (Trouve2 == 0) ) /* Aucun voisin n'est bon */
						Labels[i][j] = ++Counter ;

					if ( (Trouve1 != 0) && (Trouve2 == 0) ) /* Un seul des deux */
						Labels[i][j] = Trouve1 ;
					if ( (Trouve1 == 0) && (Trouve2 != 0) )
						Labels[i][j] = Trouve2 ;

					if ( (Trouve1 != 0) && (Trouve2 != 0) && (Trouve1 == Trouve2) ) /* Meme composante voisine */
						Labels[i][j] = Trouve1 ;

					if ( (Trouve1 != 0) && (Trouve2 != 0) && (Trouve1 != Trouve2) ) /* Tous les deux differents */
						{
						Fin = false ;
						Equi1 = TrouverEquivalence(Trouve1) ; /* On cherche l'origine de la composante */
						Equi2 = TrouverEquivalence(Trouve2) ;
						if ( (Equi1 < Equi2) || ((Equi1 == Equi2) && (Trouve1 < Trouve2)) )
							{
							Labels[i][j] = Equi1 ;
							ComposanteIdentiques[Trouve2] = Equi1 ;
							}
						else
							{
							Labels[i][j] = Equi2 ;
							ComposanteIdentiques[Trouve1] = Equi2 ;
							}
						}
					}

		while ( Fin == false ) /* On reactualise les equivalences */
				{
				Fin = true ;
				if ( Counter > ComposanteIdentiques.length ) IncreaseAlloc(Counter) ;
				for (i=1 ; i <= Counter ; i++)
					if ( ComposanteIdentiques[i] != 0 ) /* Il a un equivalent */
						if ( (ComposanteIdentiques[ComposanteIdentiques[i]] != 0) /* Si le prede...  a aussi un equivalent */
							&& (ComposanteIdentiques[ComposanteIdentiques[i]] != ComposanteIdentiques[i]) )
							{
							Fin = false ;
							ComposanteIdentiques[i] = ComposanteIdentiques[ComposanteIdentiques[i]] ;
							}
				}

		for (i=0 ; i < hauteur ; i++) /* On remet les bonnes valeurs */
			for (j=0 ; j < largeur ; j++)
				if ( Labels[i][j] > 0 )
					{
					if ( Labels[i][j] >= ComposanteIdentiques.length ) IncreaseAlloc(Labels[i][j]) ;
					if (ComposanteIdentiques[Labels[i][j]] != 0 )
						Labels[i][j] = ComposanteIdentiques[ Labels[i][j] ] ;
					}

		for (i=0 ; i <= Counter ; i++) /* On reinitialise */
			ComposanteIdentiques[i] = 0 ;

		for (i=0 ; i < hauteur ; i++) /* On note les composantes trouves */
			for (j=0 ; j < largeur ; j++)
				if ( Labels[i][j] > 0 ) ComposanteIdentiques[ Labels[i][j] ] = 1 ;

		Trouve1 = 0 ;
		for (i=1 ; i <= Counter ; i++) /* On renumerote les composantes trouves de maniere consecutive dans l'ordre */
			if ( ComposanteIdentiques[i] == 1 )
				ComposanteIdentiques[i] = ++Trouve1 ;

		for (i=0 ; i < hauteur ; i++) /* On les remplaces par les nouveaux numeros */
			for (j=0 ; j < largeur ; j++)
				if ( (Labels[i][j] > 0) && (ComposanteIdentiques[Labels[i][j]] != 0) )
					Labels[i][j] = ComposanteIdentiques[ Labels[i][j] ] ;

		Counter = Trouve1 ;
		
		
		Sizes = null ; // On remplit le tableau Sizes.
		Sizes = new int[Counter+1] ;
		for (y=0 ; y < hauteur ; y++)
			for (x=0 ; x < largeur ; x++)
				if ( Labels[y][x] > 0 )
					Sizes[Labels[y][x]]++ ;
		
		/*if ( Chrono != null )
			{
			System.out.println(Chrono.getTimeSinceMarker(marker)) ;
			Chrono.FreeMarker(marker) ;
			}*/
			
		return Counter ;
		}













	/* --------------------------------------------------------------------- Les getters --------------------------------------------------------------------- */
	/** Calcule le nombre de composantes connexes dans l'image mais ne compte que les composantes ayant une surface superieure a Threshold.
	 * @param Threshold Surface minimum que doit avoir une composante connexe afin d'etre comptabilisee. 
	 * @return Le nombre de composantes connexes de l'image.*/
	public int NumberOfConnectedComponent(int Threshold)
		{
		if ( Labels == null ) throw new NullPointerException("Execution of Label method required before this one.") ;

		int nbccf = 0 ;
		for (int i=1 ; i <= Counter ; i++)
			if ( Sizes[i] >= Threshold ) nbccf++ ;
		
		return nbccf ;
		}


	/** Methode qui renvoit le nombre de composantes connexes denombrees lors du dernier appel de la methode Label.
	 * @return Le nombre de composantes.*/
	public int NumberOfConnectedComponent()
		{
		return Counter ;
		}


	/** Method which return sizes of connected components.
	 * @return Array.*/
	public int[] Sizes()
		{
		return Sizes;
		}


	/** Method which return array of labels.
	 * @return Array.*/
	public int[][] Labels()
		{
		return Labels ;
		}


	@Override
	public int Label(BufferedImage Original, int Background) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int Label(int[][] Original, int Background, boolean EightConnex) {
		// TODO Auto-generated method stub
		return 0;
	}



	/*@Override
	public int Label(BufferedImage Original, int Background, boolean EightConnex, connexite.Chronometer Chrono) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int Label(int[][] Original, int Background, boolean EightConnex, connexite.Chronometer Chrono) {
		// TODO Auto-generated method stub
		return 0;
	}

	}
*/
	}

	
	
	

	
	
	
	
	
	
	
	
	
