package connexite;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class TestJMTest {
	


	    private static final int IMG__WIDTH = 100;
	    private static final int IMG__HEIGHT = 100;

	    public static void main(String[]args) throws InterruptedException{

	    try{

	    	
	        BufferedImage originalImage = ImageIO.read(new File("bdd//escalier1.JPG"));
	        int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
	        TestJM.imshow(originalImage);
	        Thread.sleep(3000);
	        int newWidth=originalImage.getWidth()/2;
	        int newHeight=originalImage.getHeight();
	        BufferedImage resizeImageJpg = cropImage(originalImage, 400, 0, newWidth, newHeight);
	        ImageIO.write(resizeImageJpg, "jpg", new File("bdd//escalier1_dup.JPG"));
	        
	        TestJM.imshow(resizeImageJpg);
	        Thread.sleep(5000);
	        BufferedImage yep = cropImage(resizeImageJpg, 0, 0, newWidth, newHeight);
	        ImageIO.write(yep, "jpg", new File("bdd//escalier3_dup.JPG"));
	        
	        TestJM.imshow(yep);
	        
	        
	        
	        
 /* 			BufferedImage resizeImagePng = resizeImage(originalImage, type);
	        ImageIO.write(resizeImagePng, "png", new File("c:\\image\\mkyong__png.jpg"));

	        BufferedImage resizeImageHintJpg = resizeImageWithHint(originalImage, type);
	        ImageIO.write(resizeImageHintJpg, "jpg", new File("c:\\image\\mkyong__hint__jpg.jpg"));

	        BufferedImage resizeImageHintPng = resizeImageWithHint(originalImage, type);
*/
	    }catch(IOException e){
	        System.out.println(e.getMessage());
	    }

	    }
/*
	    private static BufferedImage resizeImage(BufferedImage originalImage, int type,int newWidth,int newHeight){
	    BufferedImage resizedImage = new BufferedImage(newWidth,newHeight, type);
	    Graphics2D g = resizedImage.createGraphics();
	    g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
	    g.dispose();

	    return resizedImage;
	    }

	    private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int type){

	    BufferedImage resizedImage = new BufferedImage(IMG__WIDTH, IMG__HEIGHT, type);
	    Graphics2D g = resizedImage.createGraphics();
	    g.drawImage(originalImage, 0, 0, IMG__WIDTH, IMG__HEIGHT, null);
	    g.dispose();
	    g.setComposite(AlphaComposite.Src);

	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g.setRenderingHint(RenderingHints.KEY_RENDERING,
	    RenderingHints.VALUE_RENDER_QUALITY);
	    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	    RenderingHints.VALUE_ANTIALIAS_ON);

	    return resizedImage;
	    }*/
	
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
	    
	   
	    public void cropTest() throws IOException 
	    {
	    	File imageFile = new File("C:/Javapointers/image.jpg");
	    	BufferedImage bufferedImage = ImageIO.read(imageFile);
	    
	    }
}
