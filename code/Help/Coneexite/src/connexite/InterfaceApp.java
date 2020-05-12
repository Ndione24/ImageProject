package connexite;

import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.TextArea;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class InterfaceApp extends Application {

	final Desktop desktop = Desktop.getDesktop();
	final FileChooser fileChooser = new FileChooser();
	final FlowPane paneForImage=new FlowPane();
	final ImageView imview = new ImageView();
	final Button button = new Button("Analyser");
	final ChoiceBox<String> choiceBox = new ChoiceBox<String>();
	static Label labResult=new Label("Result :");
	private static File file =new File("");

	public static void main(String args[]) {
		launch(args);
	}
	
    private static BufferedImage resizeImage(BufferedImage originalImage, int type){
	BufferedImage resizedImage = new BufferedImage(800, 700, type);
	Graphics2D g = resizedImage.createGraphics();
	g.drawImage(originalImage, 0, 0, 800, 700, null);
	g.dispose();
	return resizedImage;
    }
	

	@Override
	public void start(Stage dd) throws Exception {
		// TODO Auto-generated method stub

		// open desktop
		final Button openButton = new Button("Choice your Image");

		openButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				 file = fileChooser.showOpenDialog(dd);
				if (file != null) {
					//System.out.println(file.getAbsolutePath());
					String localUrlFile;
					try {
						BufferedImage originalImage = ImageIO.read(file);
						int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
						BufferedImage resizeImageJpg = resizeImage(originalImage, type);
						File test=new File("bdd//test.jpg");
						ImageIO.write(resizeImageJpg, "jpg", test);
						localUrlFile = test.toURI().toURL().toString();
						Image image = new Image(localUrlFile);
						imview.setImage(image);
					} catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
					
				}
			}
		});
		
		
		button.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				String value = (String) choiceBox.getValue();
				System.out.println(file.getAbsolutePath());
				char l=value.charAt(value.length()-1);
				int numAlgo=Character.getNumericValue(l);
				System.out.println(value);
				try {
					int nb=TestJM.analyse(file.getAbsolutePath(), numAlgo);
					System.out.println("marche "+nb);
					labResult.setText("Resultat :"+nb);
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//call algo
				//getResult et setResult
				
			}
			
			
		});
		
		
		//resize image before to show
		// choice algo

		// choice algo
		choiceBox.getItems().add("Algo1");
		choiceBox.getItems().add("Algo2");
		choiceBox.setPrefWidth(150);
		FlowPane fp = new FlowPane();
		
		BorderPane pane = new BorderPane();
		Label lab = new Label("Choice Algo");
		Label lab2 = new Label("mon texjsjt");
		File file = new File("bdd\\escalier1.jpg");
		String localUrlFile = file.toURI().toURL().toString();
		Image image = new Image(localUrlFile);
		
		fp.getChildren().addAll(openButton, lab, choiceBox,button,labResult);
		fp.setOrientation(Orientation.VERTICAL);
		fp.setVgap(15);
		
		String value = (String) choiceBox.getValue();
		if(value!=null) {
			System.out.println(value);
		}
		TextArea area= new TextArea();
		area.setSize(800, 3000);
		area.isVisible();
		// launch
		imview.setImage(image);
		
		imview.setFitWidth(800);
		imview.setFitHeight(6000);
		imview.setPreserveRatio(true);
		paneForImage.getChildren().add(imview);
		pane.setRight(paneForImage);
		pane.setCenter(fp);
		Scene scene = new Scene(pane);
		dd.sizeToScene(); 
		dd.setScene(scene);
		dd.show();
	}
}
