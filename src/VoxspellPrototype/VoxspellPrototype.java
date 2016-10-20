package VoxspellPrototype;

import VoxspellScreens.MainScreen;
import VoxspellScreens.Window;
import WordList.WordList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * 
 * @author Charles Carey & Nathan Kear
 *
 */
public class VoxspellPrototype extends Application {

	//Global constants for use throughout the program
	public static final String LIGHT_BLUE = "#A7DBDB";
	public static final String DARK_BLUE = "#FFD464";
	public static final String WHITE = "#F5F5F5";
	public static final String DARK = "#c9c9c9";
	public static final int BTN_FONT_SIZE = 22;
	public static final int TXT_FONT_SIZE = 30;
	public static final int TXT_FONT_SIZE_FINE = 18;
	public static final int QUIZ_LENGTH = 10;
	public static final String FONT = " sansserif";

	private Window _window;
	
	private final String WINDOW_TITLE = "VOXSPELL";
	private final int WINDOW_WIDTH = 900;
	private final int WINDOW_HEIGHT = 563;

	public VoxspellPrototype() {
	}

	@Override
	/**
	 * This method handles the launching of the application
	 */
	public void start(Stage stage) throws Exception {
		Platform.setImplicitExit(false);
		
		// Fix window size
		stage.setResizable(false);
		
		//If the user clicks the 'x' exit button then call platform.exit() which in turn will trigger the saving of files
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				Platform.exit();
			}
			
		});
		
		// Create and format window and set to intial screen
		_window = new Window(stage, WINDOW_WIDTH, WINDOW_HEIGHT);
		_window.SetWindowScene(new Scene(new MainScreen(_window), _window.GetWidth(), _window.GetHeight()));
		_window.SetWindowTitle(WINDOW_TITLE);	
		_window.CenterOnScreen();
		_window.Show(true);
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	/**
	 * This is overriding the method that is called when the platform exits and is used to save the stats to file
	 */
	public void stop(){
	    //Save words to disk
		WordList wordList = WordList.GetWordList();
		wordList.saveWordListToDisk();
	}
	
}

