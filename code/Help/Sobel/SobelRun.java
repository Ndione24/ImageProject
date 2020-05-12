package projetImage;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

class SobelDemoRun {

	public void run(String[] args) {
		// First we declare the variables we are going to use
		Mat src, src_gray = new Mat();
		Mat grad = new Mat();
		String window_name = "Sobel Demo - Simple Edge Detector";
		int scale = 1;
		int delta = 0;
		int ddepth = CvType.CV_16S;
		// As usual we load our source image (src)
		// Check number of arguments
		if (args.length == 0){
			System.out.println("Not enough parameters!");
			System.out.println("Program Arguments: [image_path]");
			System.exit(-1);
		}
		// Load the image
		src = Imgcodecs.imread(args[0]);
		// Check if image is loaded fine
		if( src.empty() ) {
			System.out.println("Error opening image: " + args[0]);
			System.exit(-1);
		}
		// Remove noise by blurring with a Gaussian filter ( kernel size = 3 )
		Imgproc.GaussianBlur( src, src, new Size(3, 3), 0, 0, Core.BORDER_DEFAULT );
		// Convert the image to grayscale
		Imgproc.cvtColor( src, src_gray, Imgproc.COLOR_RGB2GRAY );
		Mat grad_x = new Mat(), grad_y = new Mat();
		Mat abs_grad_x = new Mat(), abs_grad_y = new Mat();
		//Imgproc.Scharr( src_gray, grad_x, ddepth, 1, 0, scale, delta, Core.BORDER_DEFAULT );
		Imgproc.Sobel( src_gray, grad_x, ddepth, 1, 0, 3, scale, delta, Core.BORDER_DEFAULT );
		//Imgproc.Scharr( src_gray, grad_y, ddepth, 0, 1, scale, delta, Core.BORDER_DEFAULT );
		Imgproc.Sobel( src_gray, grad_y, ddepth, 0, 1, 3, scale, delta, Core.BORDER_DEFAULT );
		// converting back to CV_8U
		Core.convertScaleAbs( grad_x, abs_grad_x );
		Core.convertScaleAbs( grad_y, abs_grad_y );
		Core.addWeighted( abs_grad_x, 0.5, abs_grad_y, 0.5, 0, grad );
		HighGui.imshow( window_name, grad);
		HighGui.waitKey(0);
		System.exit(0);
	}
}
