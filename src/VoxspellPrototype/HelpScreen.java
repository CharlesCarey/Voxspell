package VoxspellPrototype;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class HelpScreen {

	public HelpScreen(String helpFileName) {
		Stage popUpHelpStage = new Stage();
		
		TextArea helpTA = new TextArea();
		
		File helpFile = new File(helpFileName);
		
		try {
			BufferedReader helpReader = new BufferedReader(new FileReader(helpFile));
			
			String line = "";
			
			while((line = helpReader.readLine()) != null) {
				helpTA.appendText(line + "\n");
			}
			
			helpReader.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		helpTA.setEditable(false);
		helpTA.setStyle("-fx-font: " + 24 + " sansserif");
		
		Pane root = new Pane();

		Scene helpScene = new Scene(root, 750, 400);
		
		helpTA.setWrapText(true);
		
		helpTA.setPrefHeight(root.getHeight());
		helpTA.setPrefWidth(root.getWidth());

		root.getChildren().add(helpTA);
		
		popUpHelpStage.setScene(helpScene);
		popUpHelpStage.show();
	}
	
}
