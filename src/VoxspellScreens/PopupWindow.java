package VoxspellScreens;

import VoxspellPrototype.VoxspellPrototype;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * Create pop-up window that displays simple text. The pop up can have an optional title
 * @author Charles Carey & Nathan Kear
 *
 */
public class PopupWindow {
	private final static int TXT_FONT_SIZE = VoxspellPrototype.TXT_FONT_SIZE_FINE;
	private final static String TXT_FONT_COLOR = VoxspellPrototype.WHITE;
	private final static String BACK_COLOR = VoxspellPrototype.LIGHT_BLUE;
	private final static int POPWINDOW_WIDTH = 600;
	private final static int POPWINDOW_HEIGHT = 150;
	private final static String BORDER_COLOR = "#D4EDF4";
	private final static int SIDE_PADDING = 10;
	private final static int TOP_BOTTOM_PADDING = 60;
	private final static String FONT = VoxspellPrototype.FONT;


	/**
	 * Deploy single pop-up window.
	 * @param message Message to display.
	 * @return Stage that is displayed.
	 */
	public static Stage DeployPopupWindow(String title, String content) {
		Stage popupStage = new Stage();

		VBox root = new VBox(5);
		
		//Formatting the root VBox
		root.setPadding(new Insets(TOP_BOTTOM_PADDING, SIDE_PADDING, TOP_BOTTOM_PADDING, SIDE_PADDING));
		root.setMinHeight(POPWINDOW_HEIGHT);
		root.setMinWidth(POPWINDOW_WIDTH);
		
		// Build text to display for title and content
		Text titleText = new Text(title);
		titleText.setStyle("-fx-font: " + (TXT_FONT_SIZE  + 10) + FONT + ";" +
				" -fx-fill: " + TXT_FONT_COLOR + ";");
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setWrappingWidth(POPWINDOW_WIDTH - 100);

		Text popupText = new Text(content);
		popupText.setTextAlignment(TextAlignment.CENTER);
		popupText.setStyle("-fx-font: " + TXT_FONT_SIZE + FONT + ";" +
				" -fx-fill: " + TXT_FONT_COLOR + ";");
		popupText.setWrappingWidth(POPWINDOW_WIDTH - 50);
		root.setAlignment(Pos.CENTER);

		//Creating the title HBox to hold the title
		HBox titleHBox = new HBox();
		titleHBox.setPrefWidth(POPWINDOW_WIDTH - 100);
		titleHBox.getChildren().add(titleText);
		titleHBox.setStyle("-fx-padding: 10;" + 
				"-fx-border-style: solid inside;" + 
				"-fx-border-width: 2;" +
				"-fx-border-insets: 5;" + 
				"-fx-border-radius: 5;" + 
				"-fx-border-color: " + BORDER_COLOR + ";");
		titleHBox.setAlignment(Pos.CENTER);

		//Creating the HBox to hold the content text
		HBox contentHBox = new HBox();
		contentHBox.setPrefWidth(POPWINDOW_WIDTH - 50);
		contentHBox.getChildren().add(popupText);
		contentHBox.setStyle("-fx-padding: 10;" + 
				"-fx-border-style: solid inside;" + 
				"-fx-border-width: 2;" +
				"-fx-border-insets: 5;" + 
				"-fx-border-radius: 5;" + 
				"-fx-border-color: " + BORDER_COLOR + ";");
		contentHBox.setAlignment(Pos.CENTER);

		//If title parameter was an empty string then just show the content, else show both content and title
		if(title.isEmpty()) {
			contentHBox.setPrefHeight(root.getHeight());
			root.getChildren().addAll(contentHBox);
		} else  {
			root.getChildren().addAll(titleHBox, contentHBox);
		}
		Scene popupScene = new Scene(root, POPWINDOW_WIDTH, POPWINDOW_HEIGHT);

		// Format window.
		popupStage.setScene(popupScene);
		popupStage.show();
		popupStage.requestFocus();
		popupStage.toFront();

		// Set root node color
		root.setStyle("-fx-background-color: " + BACK_COLOR + ";");

		return popupStage;
	}
}

