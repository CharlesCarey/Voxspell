package VoxspellScreens;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import VoxspellPrototype.VoxspellPrototype;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
/**
 * 
 * This class handles the displaying of the help dialogues to the user
 * 
 * @author Charles Carey
 *
 */
public class HelpScreen {

	private final String FONT = VoxspellPrototype.FONT;
	
	public HelpScreen(String helpFileName) {
		
		//Creating the stage to show the help text files
		Stage popUpHelpStage = new Stage();
		
		TextArea helpTA = new TextArea();
		
		File helpFile = new File(helpFileName);
		
		try {
			//Creating a reader to read all the lines in the help files
			BufferedReader helpReader = new BufferedReader(new FileReader(helpFile));
			
			String line = "";
			
			//While there are still lines to be read in the help file, they are appended to the text area
			while((line = helpReader.readLine()) != null) {
				helpTA.appendText(line + "\n");
			}
			
			helpReader.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		//Make it so that the user can't edit the help text area
		helpTA.setEditable(false);
		helpTA.setStyle("-fx-font: " + 24 + FONT);
		
		Pane root = new Pane();

		Scene helpScene = new Scene(root, 750, 400);
		
		//Setting wrap text to be true so that the text doesn't go off screen
		helpTA.setWrapText(true);
		
		helpTA.setPrefHeight(root.getHeight());
		helpTA.setPrefWidth(root.getWidth());

		root.getChildren().add(helpTA);
		
		popUpHelpStage.setScene(helpScene);
		popUpHelpStage.show();
	}
	
}
