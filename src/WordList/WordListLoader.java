package WordList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import VoxspellPrototype.VoxspellPrototype;
import VoxspellScreens.PopupWindow;
import VoxspellScreens.Window;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class WordListLoader extends Parent{

	private Window _window;

	private final int BUTTON_SEPERATION = 6;
	private final int SELECTION_BAR_PADDING = 10;
	private final int TXT_FONT_SIZE = VoxspellPrototype.TXT_FONT_SIZE;
	private final int BTN_FONT_SIZE = VoxspellPrototype.BTN_FONT_SIZE;
	private final String BTN_COLOR = VoxspellPrototype.DARK_BLUE;
	private final String BACK_COLOR = VoxspellPrototype.LIGHT_BLUE;
	private final String TXT_FONT_COLOR = VoxspellPrototype.WHITE;

	/**
	 * This method allows the user to choose a word list to be loaded into the system 
	 */
	public WordListLoader() {
		//Creating a file chooser and getting the file the user chooses
		FileChooser fileChooser = new FileChooser();
		int levelStart = WordList.GetWordList().size();
		File newWordList = fileChooser.showOpenDialog(new Stage());

		//Checking that the user actually selected a file and didn't close the filechooser
		if(newWordList != null) {

			//Checking if the file is the correct type
			boolean isOKToLoad = fileTypeChecker(newWordList);

			//Checking if the file is not empty and that the first lines are laid our correctly
			boolean isNotEmpty = isFileEmpty(newWordList);

			//If the file type is ok then load it, else show the user that the file was invalid
			if(isOKToLoad && isNotEmpty) {
				try {
					WordList.loadLevel(newWordList);
					int levelEnd = WordList.GetWordList().size();
					WordList.GetWordList().addWordList(newWordList.getAbsolutePath());
					ChooseLevelScreen(levelStart, levelEnd);
				} catch (Exception e) {
					PopupWindow.DeployPopupWindow("Warning!", "The layout of the levels in the text files is invalid! Please try again!");
				}
				
			//Else if the file is a .txt file but it is not laid out properly then tell the user they layout is invalid
			} else if(isOKToLoad && !isNotEmpty) {
				PopupWindow.DeployPopupWindow("Warning!", "The layout of the levels in the text files is invalid! Please try again!");
			
			//Else the file type is invalid so tell the user that they have chosen the wrong file type
			} else {
				PopupWindow.DeployPopupWindow("Warning!", "The file type appears to be invalid. Please try again!");
			}

		}
	}

	/**
	 * This method determines the type of file
	 */
	public boolean fileTypeChecker(File f) {

		//Working out the extension of the file and checking it equals .txt
		String fileName = f.getName();
		String extension = fileName.substring(fileName.length() - 3, fileName.length());
		if(extension.equals("txt")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method checks the file to load has at least one level with at least one word
	 */
	public boolean isFileEmpty(File f) {

		boolean fileIsOK = true;

		try {
			BufferedReader emptyChecker = new BufferedReader(new FileReader(f));

			//Creating a counter to check there are two lines and the first one starts with %
			int count = 0;

			String line = "";

			while((line = emptyChecker.readLine()) != null) {
				count++;

				//Checking the first line starts with w %
				if(count == 1) {
					if(line.charAt(0) != '%') {
						fileIsOK = false;
					}

					//Checking the second line does not contain a %
				} else if (count == 2) {
					if(line.contains("%")) {
						fileIsOK = false;
					}
				}
			}

			if(count < 2) {
				fileIsOK = false;
			}

			emptyChecker.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return fileIsOK;

	}

	/**
	 * This generates the screen where users can choose which level to start at
	 */
	public void ChooseLevelScreen(final int startLevel, final int endLevel) {

		//Get the WordList
		final WordList wordlist = WordList.GetWordList();

		//Create the data structure for the level ComboBox
		ObservableList<String> options = FXCollections.observableArrayList();

		_window = new Window(new Stage(), 600, 300);
		_window.CenterOnScreen();
		_window.Show(true);

		//Getting the names of each level and adding it to the options list
		for(int i = startLevel; i < endLevel; i++) {
			Level level = wordlist.get(i);
			String levelName = level.levelName();
			options.add(levelName);
		}

		//Creating the ComboBox
		final ChoiceBox<String> levelSelect = new ChoiceBox<String>(options);

		VBox root = new VBox(BUTTON_SEPERATION);

		// Set root node size
		root.setPrefWidth(_window.GetWidth());

		//Creating the label to tell the user what to do
		Text levelSelectLabel = new Text("Please select which level you wish to start at. All levels below "
				+ "the level you choose, and the level itself, will be unlocked!");
		levelSelectLabel.setStyle("-fx-font: " + TXT_FONT_SIZE + " arial;" +
				" -fx-fill: " + TXT_FONT_COLOR + ";");
		levelSelectLabel.setWrappingWidth(_window.GetWidth());

		root.setStyle("-fx-background-color: " + BACK_COLOR);
		root.setPadding(new Insets(SELECTION_BAR_PADDING));
		root.setPrefHeight(_window.GetHeight());
		root.setPrefWidth(_window.GetWidth());
		root.getChildren().addAll(levelSelectLabel, levelSelect);

		this.getChildren().add(root);

		_window.SetWindowScene(new Scene(this));

		//levelSelect.setPromptText("Select a level");
		levelSelect.setStyle("-fx-base: " + BTN_COLOR + "; -fx-font: " + BTN_FONT_SIZE + " arial; -fx-text-fill: " + TXT_FONT_COLOR + ";");
		//levelSelect.setPrefWidth(_window.GetWidth() - (SELECTION_BAR_PADDING * 2));
		levelSelect.setPrefHeight(30);
		levelSelect.autosize();

		//Adding a listener to see what level the user selects
		levelSelect.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String oldValue, String newValue) {
				//Unlocking every level up to and including the level the user selected
				boolean levelFound = false;
				for(int i = startLevel; i < endLevel; i++) {
					Level level = wordlist.get(i);
					String levelName = level.levelName();

					if(!levelFound) {
						level.unlockLevel();
					}

					if(levelName.equals(newValue)) {
						levelFound = true;
					}
				}
				_window.Show(false);
			}
		});

	}


}
