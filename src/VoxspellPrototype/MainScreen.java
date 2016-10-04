package VoxspellPrototype;

import java.io.File;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


public class MainScreen extends Parent {

	private Window _window;

	// Constants for GUI
	private final String DailyGoalsTXT = "Daily Goals";
	private final String WordsTextedTXT = "Words Tested Today:       ";
	private final String MasteredWordsTXT = "Words Mastered Today:   ";
	private final String QuizzesDoneTXT = "Quizzes Done Today:       ";
	private final String DailyGoalsAchievedTXT = "Daily Goals Achieved:   ";
	private final int BUTTON_SEPERATION = 6; 
	private final int MENU_BAR_PADDING = 10;
	private final double MENUBAR_SCREENWIDTH_RATIO = 0.333;
	private final int TITLE_FONT_SIZE = VoxspellPrototype.TXT_FONT_SIZE;
	private final int TXT_FONT_SIZE = VoxspellPrototype.TXT_FONT_SIZE - 8;
	private final int BTN_FONT_SIZE = VoxspellPrototype.BTN_FONT_SIZE;
	private final String BTN_NEW_TEXT = "New Quiz";
	private final String BTN_REVIEW_TEXT = "Review Mistakes";
	private final String BTN_STATS_TEXT = "View Stats";
	private final String BTN_CLEAR_TEXT = "Clear Stats";
	private final String BTN_QUIT_TEXT = "Quit";
	private final String BTN_OPTIONS_TEXT = "Options";
	private final String BTN_COLOR = "#FFD464";
	private final String BACK_COLOR = VoxspellPrototype.LIGHT_BLUE;
	private final String BTN_FONT_COLOR = "#F5F5F5";
	private final String TXT_FONT_COLOR = "#F5F5F5";
	private final String BORDER_COLOR = "#D4EDF4";

	private final int TEXT_CEILING_SEPERATION = 160;

	//Fields for users daily progress
	private static double _testedWords = 0;
	private static double _dailyGoalTestedWords = 40;
	private static double _masteredWords = 0;
	private static double _dailyGoalMasteredWords = 20;
	private static double _quizzesDone = 0;
	private static double _dailyGoalQuizzes = 5;



	public MainScreen(Window window) {
		super();

		this._window = window;

		// Create root node and set its size
		VBox vRoot = new VBox(0);
		vRoot.setPrefWidth(_window.GetWidth());
		vRoot.setPrefHeight(_window.GetHeight());

		HBox root = new HBox(0);
		root.setPrefWidth(_window.GetWidth());
		root.setPrefHeight(_window.GetHeight());

		// Create menu bar
		double menuBarWidth = _window.GetWidth() * MENUBAR_SCREENWIDTH_RATIO;
		Pane menuPane = buildMenuBar(menuBarWidth);

		//Create the menu
		MenuBar menuBar = new MenuBar();
		menuBar.setPrefWidth(_window.GetWidth());

		Menu fileMenu = new Menu();
		fileMenu.setText("File");

		MenuItem loadFile = new MenuItem("Load Files");
		loadFile.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				new WordListLoader();
			}

		});
		fileMenu.getItems().add(loadFile);

		Menu helpMenu = new Menu();
		helpMenu.setText("Help");

		MenuItem quizHelp = new MenuItem("Quiz Help");
		quizHelp.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				new HelpScreen("Help-Files/Quiz-Help.txt");
			}
			
		});
		
		MenuItem statsHelp = new MenuItem("Stats Help");
		statsHelp.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				new HelpScreen("Help-Files/Stats-Help.txt");
			}
			
		});

		MenuItem optionsHelp = new MenuItem("Options Help");
		optionsHelp.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				new HelpScreen("Help-Files/Options-Help.txt");
			}
			
		});
		
		helpMenu.getItems().addAll(quizHelp, statsHelp, optionsHelp);
		
		menuBar.getMenus().addAll(fileMenu, helpMenu);

		Pane dailyGoalsPane = buildDailyGoals(root.getPrefWidth()
				- menuPane.getPrefWidth() 
				- menuPane.getPadding().getLeft() 
				- menuPane.getPadding().getRight());

		// Add menu bar and text to root node
		root.getChildren().addAll(menuPane, dailyGoalsPane);

		//Add menu and HBox root to vRoot
		vRoot.getChildren().addAll(menuBar, root);

		this.getChildren().add(vRoot);

		// Set root node color
		root.setStyle("-fx-background-color: " + "#A7DBDB" + ";");
	}

	private Pane buildDailyGoals(double desiredWidth) {

		//Loading in the daily goals
		WordList.GetWordList();
		
		//Making VBox so user can track progress
		VBox dailyGoalsVBox = new VBox(50);
		dailyGoalsVBox.setPrefWidth(desiredWidth);
		dailyGoalsVBox.setStyle("-fx-padding: 10;" + 
				"-fx-border-style: solid inside;" + 
				"-fx-border-width: 2;" +
				"-fx-border-insets: 5;" + 
				"-fx-border-radius: 5;" + 
				"-fx-border-color: " + BORDER_COLOR + ";");

		//Making the Daily Goals banner

		Text dailyGoalsTXT = new Text(DailyGoalsTXT);

		dailyGoalsTXT.setWrappingWidth(desiredWidth - 80);

		dailyGoalsTXT.setTextAlignment(TextAlignment.CENTER);


		dailyGoalsTXT.setStyle("-fx-font: " + TITLE_FONT_SIZE + " sansserif;" +
				" -fx-fill: " + TXT_FONT_COLOR + ";");

		//Making the HBox to store everything for the daily goals achieved 
		HBox dailyGoalsHB = new HBox();

		Text dailyGoalsAchieved = new Text (DailyGoalsAchievedTXT);
		dailyGoalsAchieved.setStyle("-fx-font: " + TXT_FONT_SIZE + " sansserif;" +
				" -fx-fill: " + TXT_FONT_COLOR + ";");


		dailyGoalsHB.getChildren().addAll(dailyGoalsAchieved);

		//Making HBox to store stars
		HBox starHB = new HBox(30);
		starHB.setAlignment(Pos.CENTER);
		File starFile = new File("./images/star-greyed-out.png");
		File starYellowFile = new File("./images/star-yellow.png");

		Image yellowStar = new Image(starYellowFile.toURI().toString());
		Image greyedOutStar = new Image(starFile.toURI().toString());

		//first star
		ImageView firstStar = new ImageView();
		if(_testedWords/_dailyGoalTestedWords >= 1) {
			firstStar.setImage(yellowStar);
			firstStar.setFitWidth(80);
			firstStar.setPreserveRatio(true);
			firstStar.setVisible(true);
		} else {
			firstStar.setImage(greyedOutStar);
			firstStar.setFitWidth(80);
			firstStar.setPreserveRatio(true);
			firstStar.setVisible(true);
		}

		//second star
		ImageView secondStar = new ImageView();
		if(_masteredWords/_dailyGoalMasteredWords >= 1) {
			secondStar.setImage(yellowStar);
			secondStar.setFitWidth(80);
			secondStar.setPreserveRatio(true);
			secondStar.setVisible(true);
		} else {
			secondStar.setImage(greyedOutStar);
			secondStar.setFitWidth(80);
			secondStar.setPreserveRatio(true);
			secondStar.setVisible(true);
		}

		//third star
		ImageView thirdStar = new ImageView();
		if(_quizzesDone/_dailyGoalQuizzes >= 1) {
			thirdStar.setImage(yellowStar);
			thirdStar.setFitWidth(80);
			thirdStar.setPreserveRatio(true);
			thirdStar.setVisible(true);
		} else {
			thirdStar.setImage(greyedOutStar);
			thirdStar.setFitWidth(80);
			thirdStar.setPreserveRatio(true);
			thirdStar.setVisible(true);
		}


		starHB.getChildren().addAll(firstStar, secondStar, thirdStar);

		//Making the HBox to store everything for the words tested daily goals
		HBox wordsTestedHB = new HBox();
		
		//Making progress bar for tested words
		ProgressBar wordsTestedPB = new ProgressBar();
		wordsTestedPB.setStyle("-fx-accent: " + BTN_COLOR + ";");
		wordsTestedPB.setProgress(_testedWords/_dailyGoalTestedWords);
		wordsTestedPB.setPrefWidth(desiredWidth - 80);
		wordsTestedPB.setTooltip(new Tooltip((int)_testedWords + "/" + (int)_dailyGoalTestedWords + " words tested today"));
		
		//Making the text for the words tested
		Text wordsTestedTxt = new Text(WordsTextedTXT);
		wordsTestedTxt.setStyle("-fx-font: " + TXT_FONT_SIZE + " sansserif;" +
				" -fx-fill: " + TXT_FONT_COLOR + ";");

		wordsTestedHB.getChildren().addAll(wordsTestedTxt, wordsTestedPB);


		//Making the HBox to store everything for the mastered words daily goals
		HBox wordsMasteredHB = new HBox();

		//Making progress bar for correct words
		ProgressBar wordsMasteredPB = new ProgressBar();
		wordsMasteredPB.setStyle("-fx-accent: " + BTN_COLOR + ";");
		wordsMasteredPB.setProgress(_masteredWords/_dailyGoalMasteredWords);
		wordsMasteredPB.setPrefWidth(desiredWidth - 80);
		wordsMasteredPB.setTooltip(new Tooltip((int)_masteredWords + "/" + (int)_dailyGoalMasteredWords + " words mastered today"));


		//Making the text for the words mastered today
		Text masteredWordsTXT = new Text(MasteredWordsTXT);
		masteredWordsTXT.setStyle("-fx-font: " + TXT_FONT_SIZE + " sansserif;" +
				" -fx-fill: " + TXT_FONT_COLOR + ";");
		
		wordsMasteredHB.getChildren().addAll(masteredWordsTXT, wordsMasteredPB);

		//Making the HBox to store everything for the quizzes done daily goals
		HBox quizzesDoneHB = new HBox();

		//Making progress bar for correct words
		ProgressBar quizzesDonePB = new ProgressBar();
		quizzesDonePB.setStyle("-fx-accent: " + BTN_COLOR + ";");
		quizzesDonePB.setProgress(_quizzesDone/_dailyGoalQuizzes);
		quizzesDonePB.setPrefWidth(desiredWidth - 80);
		quizzesDonePB.setTooltip(new Tooltip((int)_quizzesDone + "/" + (int)_dailyGoalQuizzes + " quizzes completed today"));
		
		//Making the text for the quizzes done today
		Text quizesDoneTXT = new Text(QuizzesDoneTXT);
		quizesDoneTXT.setStyle("-fx-font: " + TXT_FONT_SIZE + " sansserif;" +
				" -fx-fill: " + TXT_FONT_COLOR + ";");

		quizzesDoneHB.getChildren().addAll(quizesDoneTXT, quizzesDonePB);


		dailyGoalsVBox.getChildren().addAll(dailyGoalsTXT, dailyGoalsHB, starHB, wordsTestedHB, wordsMasteredHB, quizzesDoneHB);

		return dailyGoalsVBox;
	}

	private Pane buildMenuBar(double desiredWidth) {
		Button btnNew, btnReview, btnStats, btnClear, btnQuit, btnOptions;

		// Create vbox with specific dimensions
		VBox menuButtons = new VBox(BUTTON_SEPERATION);
		menuButtons.setPrefWidth(desiredWidth);
		menuButtons.setPrefHeight(_window.GetHeight());

		menuButtons.setStyle("-fx-padding: 0;" + 
				"-fx-border-style: solid inside;" + 
				"-fx-border-width: 2;" +
				"-fx-border-insets: 5;" + 
				"-fx-border-radius: 5;" + 
				"-fx-border-color: " + BORDER_COLOR + ";");

		// Create buttons
		btnNew = new Button(BTN_NEW_TEXT);
		btnReview = new Button(BTN_REVIEW_TEXT);
		btnStats = new Button(BTN_STATS_TEXT);
		btnClear = new Button(BTN_CLEAR_TEXT);
		btnQuit = new Button(BTN_QUIT_TEXT);
		btnOptions = new Button(BTN_OPTIONS_TEXT);

		// Set button style properties
		btnNew.setStyle("-fx-font: " + BTN_FONT_SIZE + " sansserif;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		btnReview.setStyle("-fx-font: " + BTN_FONT_SIZE + " sansserif;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		btnStats.setStyle("-fx-font: " + BTN_FONT_SIZE + " sansserif;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		btnClear.setStyle("-fx-font: " + BTN_FONT_SIZE + " sansserif;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		btnQuit.setStyle("-fx-font: " + BTN_FONT_SIZE + " sansserif;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		btnOptions.setStyle("-fx-font: " + BTN_FONT_SIZE + " sansserif;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");

		// Set width and height of buttons
		btnNew.setMinWidth(menuButtons.getPrefWidth()); 
		btnNew.setPrefHeight(Integer.MAX_VALUE);

		btnReview.setMinWidth(menuButtons.getPrefWidth()); 
		btnReview.setPrefHeight(Integer.MAX_VALUE);

		btnStats.setMinWidth(menuButtons.getPrefWidth()); 
		btnStats.setPrefHeight(Integer.MAX_VALUE);

		btnClear.setMinWidth(menuButtons.getPrefWidth()); 
		btnClear.setPrefHeight(Integer.MAX_VALUE);

		btnQuit.setMinWidth(menuButtons.getPrefWidth()); 
		btnQuit.setPrefHeight(Integer.MAX_VALUE);

		btnOptions.setMinWidth(menuButtons.getPrefWidth()); 
		btnOptions.setPrefHeight(Integer.MAX_VALUE);
		
		//Add tooltips for buttons
		btnNew.setTooltip(new Tooltip("Click to start a new quiz!"));
		btnReview.setTooltip(new Tooltip("Click to start a quiz to review your failed words!"));
		btnStats.setTooltip(new Tooltip("Click to see your attempt history!"));
		btnClear.setTooltip(new Tooltip("Click to clear the statistics!"));
		btnOptions.setTooltip(new Tooltip("Click change the settings!"));

		//Add buttons to their respective grouping
		VBox quizButtonsVB = new VBox(BUTTON_SEPERATION);
		quizButtonsVB.getChildren().addAll(btnNew, btnReview);
		quizButtonsVB.setStyle("-fx-padding: 10;" + 
				"-fx-border-style: solid inside;" + 
				"-fx-border-width: 2;" +
				"-fx-border-insets: 5;" + 
				"-fx-border-radius: 5;" + 
				"-fx-border-color: " + BORDER_COLOR + ";");

		VBox statsButtonsVB = new VBox(BUTTON_SEPERATION);
		statsButtonsVB.getChildren().addAll(btnStats, btnClear);
		statsButtonsVB.setStyle("-fx-padding: 10;" + 
				"-fx-border-style: solid inside;" + 
				"-fx-border-width: 2;" +
				"-fx-border-insets: 5;" + 
				"-fx-border-radius: 5;" + 
				"-fx-border-color: " + BORDER_COLOR + ";");

		VBox optionsButtonsVB = new VBox(BUTTON_SEPERATION);
		optionsButtonsVB.getChildren().addAll(btnOptions);
		optionsButtonsVB.setStyle("-fx-padding: 10;" + 
				"-fx-border-style: solid inside;" + 
				"-fx-border-width: 2;" +
				"-fx-border-insets: 5;" + 
				"-fx-border-radius: 5;" + 
				"-fx-border-color: " + BORDER_COLOR + ";");



		// Add buttons to pane
		//menuButtons.getChildren().addAll(btnNew, btnReview, btnStats, btnClear, btnOptions, btnQuit);

		menuButtons.getChildren().addAll(quizButtonsVB, statsButtonsVB, optionsButtonsVB);

		// Add padding around vbox (so buttons don't touch screen edge)
		menuButtons.setPadding(new Insets(MENU_BAR_PADDING));

		// Define button actions
		btnNew.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				_window.SetWindowScene(new Scene(new LevelSelectionScreen(_window, "Normal_Quiz"), _window.GetWidth(), _window.GetHeight()));
			}	
		});

		btnReview.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				_window.SetWindowScene(new Scene(new LevelSelectionScreen(_window, "Review_Quiz"), _window.GetWidth(), _window.GetHeight()));
			}	
		});

		btnStats.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				_window.SetWindowScene(new Scene(new StatisticsScreen(_window), _window.GetWidth(), _window.GetHeight()));
			}	
		});

		btnClear.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				PopupWindow.DeployPopupWindow("", "Cleared Statistics");
				WordList.GetWordList().ClearStats();
			}	
		});

		btnQuit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				WordList wordList = WordList.GetWordList();
				wordList.saveWordListToDisk();
				Platform.exit();
			}	
		});

		btnOptions.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				_window.SetWindowScene(new Scene(new OptionsScreen(_window), _window.GetWidth(), _window.GetHeight()));
			}	
		});

		return menuButtons;
	}

	public static void addToTestedWordsProgress(int numOfWordsTested) {
		_testedWords += numOfWordsTested;
	}

	public static void addToMasteredWordsProgress() {
		_masteredWords += 1;
	}
	
	public static void addToQuizzesDone() {
		_quizzesDone += 1;
	}
	
	public static double[] getDailyGoals() {
		double[] dailyGoals = {_testedWords, _masteredWords, _quizzesDone};
		return dailyGoals;
	}

}
