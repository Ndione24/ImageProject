package Image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Label8 {
	
	  static final int NPASS = 11;
	    public static Graphics2D graphique ;
	  static Color white = new Color (255,255,255);
	  static int couleurblanc = white.getRGB();
	   static int keymax =0;
	   static int nbescaliers=0;
	
	
	static void preparation(int[][] fb, int iw, int ih) {
        for(int y=0;y < ih;y++) {
            for(int x=0;x < iw;x++) {
		int ptr = y * iw + x;
		fb[0][ptr] = (fb[0][ptr] == 0) ? -1 : ptr;
            }
        }
    }

    static int CCLSub(int[][] fb, int pass, int x0, int y0, int iw, int ih) {
	int g = fb[pass-1][y0 * iw + x0];

	for(int y=-1;y<=1;y++) {
	    if (y + y0 < 0 || y + y0 >= ih) continue;
	    for(int x=-1;x<=1;x++) {
		if (x + x0 < 0 || x + x0 >= iw) continue;
		int q = (y + y0)*iw + x + x0;
		if (fb[pass-1][q] != -1 && fb[pass-1][q] < g) g = fb[pass-1][q];
	    }
	}

	return g;
    }

    static void propagation(int[][] fb, int pass, int iw, int ih) {
	for(int y=0;y < ih;y++) {
	    for(int x=0;x < iw;x++) {
		int ptr = y * iw + x;

		fb[pass][ptr] = fb[pass-1][ptr];

		int h = fb[pass-1][ptr];
		int g = CCLSub(fb, pass, x, y, iw, ih);

		if (g != -1) {
		    for(int i=0;i<6;i++) g = fb[pass-1][g];

		    fb[pass][h  ] = fb[pass][h  ] < g ? fb[pass][h  ] : g; // !! Atomic, referring result of current pass
		    fb[pass][ptr] = fb[pass][ptr] < g ? fb[pass][ptr] : g; // !! Atomic
		}
	    }
	}
    }

    static void label8(int[][] fb, int iw, int ih) {
	preparation(fb, iw, ih);

        for(int pass=1;pass<NPASS;pass++) {
	    propagation(fb, pass, iw, ih);
        }
    }

    static public BufferedImage getCC(BufferedImage inImage) {
    	
    	int iw = inImage.getWidth(), 
		ih = inImage.getHeight();
	
		int[][] fb = new int[NPASS][iw * ih];
	
        for(int y = 0;y < ih;y++) {
            for(int x = 0;x < iw;x++) {
		fb[0][y * iw + x] = ((inImage.getRGB(x, y) >> 8) & 255) > 127 ? 1 : 0;
            }
        }
	
		label8(fb, iw, ih);
	
		BufferedImage outImage = new BufferedImage(iw, ih, BufferedImage.TYPE_3BYTE_BGR);
        for(int y = 0;y < ih;y++) {
            for(int x = 0;x < iw;x++) {
		outImage.setRGB(x, y, fb[NPASS-1][y * iw + x] == -1 ? 0 : (fb[NPASS-1][y * iw + x]  * 1103515245 + 12345));
            }
        }
        
        return outImage;
    }
    
    static public int getNumberOfCC(BufferedImage image)  {
    	Set<Integer> colors = new HashSet<Integer>();
    	
    	
    		Map <Integer,Double> map = new TreeMap<>();
    	 Double max = 0.0;
    	
    	 int sommedesaires =0;
      
        
    	
    	//Iterator<Integer> iterator = colors.iterator();
            
        int w = image.getWidth();
        int h = image.getHeight();
        
        // compte le nombre de pixel qu'a une couleur
        for(int y = 0; y < h; y++) 
        {
            for(int x = 0; x < w; x++) 
            	{
            	
            	
                int pixel = image.getRGB(x, y);     
                colors.add(pixel);
                
                
               
                int r = (pixel>>16)&0xff; 
                int g = (pixel>>8)&0xff; 
                int b = pixel&0xff; 
                //pixel!= -16777216
                	if (pixel!= -16777216 ) 
                	{
                		
                		//aire++;
                		double count = map.getOrDefault(pixel, 0.0);
                		count++;
                		map.put(pixel, count);
                		
                	}
                }
        }
        
        //parcours le hashmap(treemap pour avoir dans l'ordre les valeurs) ppur trouver la plus grande valeur de pixel qu'ont les couleurs dans le but de normaliser 
        
        for (Entry<Integer, Double> entry : map.entrySet())
        {
           
          sommedesaires+= entry.getValue();
          max = Collections.max(map.values());
              // Print the key with max value
         if (entry.getValue()==max) {
              System.out.println(entry.getKey());     // Print the key with max value
          }
  
          
        }
        System.out.println("max"+max);
        System.out.println(map);
        System.out.println("somme des aires est "+sommedesaires);
     
     
     // POUR NORMALISER les valeurs des aires entre 0 et 1 avec la valeur max 
       /**for (Entry<Integer, Double> entry : map.entrySet())
        {
        	Double buffer = entry.getValue();
        	buffer/= max;
        	map.replace(entry.getKey(), buffer);
        	
        	
        }**/
       
       // reste à trouver le bon seuillage du nombre de pixels sur des images 
       
       
    
       	for(Entry<Integer, Double> entry : map.entrySet())
       	{
       		if(entry.getValue()>=50000)
       		{
       			keymax= entry.getKey();

       			for(int y = 0; y < h; y++) 
       			{
       				for(int x = 0; x < w; x++) 
                   		{
                  	 
                  	 			int pixel= image.getRGB(x, y);
                  	 			/*int r = (pixel>>16)&0xff; 
                  	
                  	 			int g = (pixel>>8)&0xff; 
                  	 			int b = pixel&0xff; */
                  	
                  	 			//image.setRGB(x, y, couleurblanc);
                  	 			if (pixel == keymax)
                  	 			{
                  	 				image.setRGB(x, y,-16777216) ;
                  	 			}
                  		 
                   		}
       		
       			}
       			//nbescaliers--;
       			
       		}
       		else if(entry.getValue()>=2500 && entry.getValue()<=50000)
       		{
       		//keymax = entry.getKey();
       		
       		nbescaliers++;
       	
       		
       		
       	
       		}
       	}
       	
       	
       
       	for(int y = 0; y < h; y++) 
           {
               for(int x = 0; x < w; x++) 
               	{
              	 
              	 	int pixel= image.getRGB(x, y);
              	 	int r = (pixel>>16)&0xff; 
              	
                   int g = (pixel>>8)&0xff; 
                   int b = pixel&0xff; 
                   if (r>0 || b>0 || g>0) 
                   {
                	  
                   image.setRGB(x, y, couleurblanc);
                   
                   }
                  }
               	
               
           }
       	
       	
       
       	
       
       
       
       System.out.println("NOMBRE ESCALIERS ="+nbescaliers);
       	
     
       

       return colors.size();
}
    

}
