package VoxspellScreens;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Sound.FestivalSpeakTask;
import Sound.SoundPlayer;
import VoxspellPrototype.VoxspellPrototype;
import WordList.WordList;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 * 
 * @author Charles Carey & Nathan Kear
 *
 */
public class QuizScreen extends Parent {

	private Window _window;

	private final String BTN_SPEAK_TEXT = "Speak";
	private final String BTN_ENTER_TEXT = "Enter";
	private final String BTN_DEF_TEXT = "Definition";
	private final int HBX_SPACING = 10;
	private final int VBX_SPACING = 50;
	private final String BTN_COLOR = VoxspellPrototype.DARK_BLUE;
	private final String BACK_COLOR = VoxspellPrototype.LIGHT_BLUE;
	private final String BTN_FONT_COLOR = VoxspellPrototype.WHITE;
	private final String TXT_FONT_COLOR = VoxspellPrototype.WHITE;
	private final String TFD_FONT_COLOR = VoxspellPrototype.DARK;
	private final int TXT_FONT_SIZE = VoxspellPrototype.TXT_FONT_SIZE;
	private final int BTN_FONT_SIZE = VoxspellPrototype.BTN_FONT_SIZE;
	private final int TFD_FONT_SIZE = VoxspellPrototype.BTN_FONT_SIZE;
	private final int SIDE_PADDING = 10;
	private final int TOP_BOTTOM_PADDING = 60;
	private final int BTN_WIDTH = 200;
	private final int BTN_HEIGHT = 70;
	private final int TFD_WIDTH = 300;

	private final List<ImageView> _quizProgress = new ArrayList<ImageView>();
	private final Text _txtQuiz;
	private final Text _txtProgress;
	private TextField _tfdAttempt;
	private String _level;
	private Text _Counter = new Text();
	final Timeline _countDownTimer = new Timeline();

	private List<String> _words;
	private int _wordIndex = 0;
	private int _time = 30;
	private boolean _firstGuess = true;
	private int _correctWords = 0;
	private HashMap<String, String> _userAttempts = new HashMap<String, String>();
	private LevelSelectionScreen.QuizType _quizType;

	private Image GreenCircle;
	private Image OrangeCircle;
	private Image RedCircle;



	public QuizScreen(Window window, String wordlistName, LevelSelectionScreen.QuizType quizType) {
		
		_quizType = quizType;

		//Setting up the circle images
		File greenCircleFile = new File("./images/green-circle.png");
		GreenCircle = new Image(greenCircleFile.toURI().toString());

		File orangeCircleFile = new File("./images/orange-circle.png");
		OrangeCircle = new Image(orangeCircleFile.toURI().toString());

		File redCircleFile = new File("./images/red-circle.png");
		RedCircle = new Image(redCircleFile.toURI().toString());


		this._window = window;

		_level = wordlistName;

		// Get words for this quiz
		if(quizType == LevelSelectionScreen.QuizType.NORMAL_QUIZ) {
			_words = WordList.GetWordList().GetRandomWords(wordlistName, VoxspellPrototype.QUIZ_LENGTH);
		} else {
			_words = WordList.GetWordList().GetRandomFailedWords(wordlistName, VoxspellPrototype.QUIZ_LENGTH);
		}

		MainScreen.addToTestedWordsProgress(_words.size());

		// Create root pane and set its size to whole window
		VBox root = new VBox(VBX_SPACING);
		root.setMaxWidth(_window.GetWidth());
		root.setMaxHeight(_window.GetHeight());
		root.setPadding(new Insets(TOP_BOTTOM_PADDING, SIDE_PADDING, TOP_BOTTOM_PADDING, SIDE_PADDING));


		// Create quiz title text
		_txtQuiz = new Text("Quiz");
		_txtQuiz.prefWidth(_window.GetWidth());
		_txtQuiz.setTextAlignment(TextAlignment.CENTER);
		_txtQuiz.setWrappingWidth(_window.GetWidth());
		_txtQuiz.setStyle("-fx-font: " + TXT_FONT_SIZE + " arial;" +
				" -fx-fill: " + TXT_FONT_COLOR + ";");

		// Create score progress counter text
		_txtProgress = new Text("\nCorrect: 0/" + _wordIndex);
		_txtProgress.prefWidth(_window.GetWidth());
		_txtProgress.setTextAlignment(TextAlignment.CENTER);
		_txtProgress.setWrappingWidth(_window.GetWidth() - (SIDE_PADDING * 2));
		_txtProgress.setStyle("-fx-font: " + TXT_FONT_SIZE + " arial;" +
				" -fx-fill: " + TXT_FONT_COLOR + ";");

		//Create count down timer
		_Counter = new Text("30");
		_Counter.prefWidth(_window.GetWidth());
		_Counter.setTextAlignment(TextAlignment.CENTER);
		_Counter.setWrappingWidth(_window.GetWidth());
		_Counter.setStyle("-fx-font: " + TXT_FONT_SIZE + " arial;" +
				" -fx-fill: " + TXT_FONT_COLOR + ";");


		// Add all nodes to root pane
		root.getChildren().addAll(_txtQuiz, _Counter, buildCenterGUIQuizPane(BTN_HEIGHT), _txtProgress);

		// Add root pane to parent
		this.getChildren().addAll(root);

		// Color background
		root.setStyle("-fx-background-color: " + BACK_COLOR + ";");

		new FestivalSpeakTask("Spell " + currentWord()).run();
	}


	/**
	 * This builds the central area of the quiz pane
	 * 
	 * @param desiredHeight
	 * @return
	 */
	private Pane buildCenterGUIQuizPane(double desiredHeight) {

		VBox root = new VBox(HBX_SPACING);

		// Build center pane
		HBox centerPane = new HBox(HBX_SPACING);

		// Create center pane nodes
		Button btnSpeak = new Button(BTN_SPEAK_TEXT);
		Button btnEnter = new Button(BTN_ENTER_TEXT);
		_tfdAttempt = new TextField();

		// Add nodes to center pane
		centerPane.getChildren().addAll(btnSpeak, _tfdAttempt, btnEnter);

		// Set node styles
		btnSpeak.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		btnEnter.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		_tfdAttempt.setStyle("-fx-font: " + TFD_FONT_SIZE + " arial;" +
				"-fx-text-fill: " + TFD_FONT_COLOR + ";");

		// Center text in text-field
		_tfdAttempt.setAlignment(Pos.CENTER);

		// Set node dimensions
		btnEnter.setPrefWidth(BTN_WIDTH);
		btnEnter.setPrefHeight(BTN_HEIGHT);
		btnSpeak.setPrefWidth(BTN_WIDTH);
		btnSpeak.setPrefHeight(BTN_HEIGHT);
		_tfdAttempt.setPrefWidth(TFD_WIDTH);
		_tfdAttempt.setPrefHeight(BTN_HEIGHT);

		centerPane.setAlignment(Pos.CENTER);

		// Set action for speak button
		btnSpeak.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				new FestivalSpeakTask(currentWord()).run();
			}
		});

		btnEnter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {				
				attemptWord(_tfdAttempt.getText());
			}
		});

		_tfdAttempt.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ENTER) {
					attemptWord(_tfdAttempt.getText());
				}
			}	
		});

		HBox defBox = new HBox(HBX_SPACING);
		defBox.setAlignment(Pos.CENTER);

		Button btnDefinition = new Button(BTN_DEF_TEXT);

		btnDefinition.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		btnDefinition.setPrefWidth(BTN_WIDTH);
		btnDefinition.setPrefHeight(BTN_HEIGHT);

		btnDefinition.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				getDefinition(currentWord());

			}

		});

		defBox.getChildren().add(btnDefinition);

		//Creating the HBox to hold all the images which indicate the users progress
		HBox quizProgressHB = new HBox(HBX_SPACING * 4);
		quizProgressHB.setAlignment(Pos.CENTER);
		quizProgressHB.setMaxWidth(_window.GetWidth());
		quizProgressHB.setPadding(new Insets(30, 0, 0, 0));

		int numOfCircles = _words.size();

		File greyCircleFile = new File("./images/grey-circle.png");

		Image greyCircle = new Image(greyCircleFile.toURI().toString());

		int size = _window.GetWidth()/(numOfCircles + HBX_SPACING * 2);

		
		//Adding each circle to the HBox and an array list
		for(int i = 0; i < numOfCircles; i++) {
			ImageView circle = new ImageView();
			circle.setImage(greyCircle);
			circle.setFitHeight(size);
			circle.setFitWidth(size);
			circle.setPreserveRatio(true);

			_quizProgress.add(circle);

			quizProgressHB.getChildren().add(circle);
		}

		root.getChildren().addAll(centerPane, defBox, quizProgressHB);

		printTimer();

		return root;
	}

	/**
	 * This method prints out the timer
	 * @return 
	 */
	private void printTimer() {
		//Set up timer
		_time = 31;
		
		KeyFrame keyframe = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				_time = _time - 1;
				
				//If timer is bigger than 0, then display the timer
				if(_time >= 0) {
					_Counter.setText("" + _time);
					
				//Else, reset the timer and enter the users currently typed word
				} else {
					_time = 31;
					_Counter.setText("" + _time);	

					if(_tfdAttempt.getText().isEmpty()) {
						attemptWord("ranxoutxofxtime");
					} else {
						attemptWord(_tfdAttempt.getText());
					}
				}
			}

		});
		_countDownTimer.setCycleCount(Timeline.INDEFINITE);
		_countDownTimer.getKeyFrames().add(keyframe);
		_countDownTimer.play();

	}

	/**
	 * Test entered word for correctness and update the GUI
	 * @param word
	 * @return whether word is correct or not
	 */
	private boolean attemptWord(String word) {

		_txtQuiz.setText("Quiz");

		word = word.trim();

		if (word.equals("")) {
			// Word attempt must contain some characters		
			_txtQuiz.setText("Quiz\n\nEnter a word"); 

			return false;
		}

		if (word.contains(" ")) {
			// Word attempt may not contain white space
			_txtQuiz.setText("Quiz\n\nMay not contain spaces"); 

			return false;
		}

		if (!word.matches("[a-zA-Z]+")) {
			// Word attempt may only contain alphabet characters.

			_txtQuiz.setText("Quiz\n\nMay only contain letters"); 

			return false;
		}


		boolean correct = (word.toLowerCase().equals(currentWord().toLowerCase()));
		boolean advance = false;
		String speechOutput = "";

		if (_firstGuess) {
			if (correct) {
				// Correct on first guess
				_correctWords++;

				_quizProgress.get(_wordIndex).setImage(GreenCircle);

				//PLaying the correct sound
				SoundPlayer.userWasCorrect(true);
				new SoundPlayer().run();

				//Saying the user got the word correct
				speechOutput = speechOutput + "Correct..";
				WordList.GetWordList().masteredWord(currentWord(), _level);

				//Adding it to the mastered progress
				MainScreen.addToMasteredWordsProgress();
				advance = true;

				//Setting the timer again
				_time = 31;
				_userAttempts.put(currentWord(), word);
			} else {
				// Incorrect on first guess
				
				//Resetting timer
				_time = 31;
				
				//Playing the incorrect tone
				SoundPlayer.userWasCorrect(false);
				new SoundPlayer().run();
				
				//Saying the word was incorrect, spell it again
				speechOutput = speechOutput + "Incorrect.. try again.. " + currentWord() + ".. " + currentWord() + ".";
				_firstGuess = false;
			}
		} else {
			if (correct) {
				// Correct on second guess
				_correctWords++;
				
				//Resetting timer
				_time = 31;
				
				//Saying user got it right
				speechOutput = speechOutput + "Correct..";
				
				//Setting the quiz progress to be orange to indicate the user faulted
				_quizProgress.get(_wordIndex).setImage(OrangeCircle);
				
				//Adding it to faulted word
				WordList.GetWordList().faultedWord(currentWord(), _level);
				advance = true;
				_userAttempts.put(currentWord(), word);
			} else {
				// Incorrect on second guess
				
				//Resetting the timer
				_time = 31;
				
				//Playing the incorrect attempt noise
				SoundPlayer.userWasCorrect(false);
				new SoundPlayer().run();
				
				//Saying the word was incorrect
				speechOutput = speechOutput + "Incorrect..";
				
				//Setting the quiz progress to be red
				_quizProgress.get(_wordIndex).setImage(RedCircle);
				
				WordList.GetWordList().failedWord(currentWord(), _level);
				advance = true;
				if(word.equals("ranxoutxofxtime")) {
					_userAttempts.put(currentWord(), " ");
				} else {
					_userAttempts.put(currentWord(), word);
				}
			}
		}

		if (advance && nextWord()) {
			speechOutput = speechOutput + " Spell " + currentWord();
		}

		new FestivalSpeakTask(speechOutput).run();
		_tfdAttempt.clear();

		return correct;
	}

	/**
	 * Move on to next word
	 * @return true if a next word is available.
	 */
	private boolean nextWord() {
		if (_wordIndex + 1 < _words.size()) {
			// There are words left to spell
			_wordIndex++;
			_firstGuess = true;
			_txtProgress.setText("\nCorrect: " + _correctWords + "/" + _wordIndex);

			return true;
		} else {
			// No words left to spell
			
			//Stop timer
			_countDownTimer.stop();
			
			//Open the results screen
			_window.SetWindowScene(new Scene(new ResultsScreen(_window, _correctWords, _words.size(), _level, _userAttempts, _quizType.name()), _window.GetWidth(), _window.GetHeight()));

			return false;
		}
	}

	/**
	 * Returns the current word
	 * @return
	 */
	private String currentWord() {
		return _words.get(_wordIndex);
	}

	/**
	 * This method attempts to find the defintion for a word
	 * @param word
	 */
	private void getDefinition(String word) {

		try {
			
			//Opening a connection to merriam websters result page for the word specified
			URL dictionaryAPICall = new URL("http://www.merriam-webster.com/dictionary/" + word);
			URLConnection myURLConnection = dictionaryAPICall.openConnection();
			myURLConnection.connect();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					myURLConnection.getInputStream()));
			String inputLine;

			boolean defFound = false;
			int counter = 0;

			String def = "";

			//Reading through the HTML code from the website
			while ((inputLine = in.readLine()) != null) {

				//If basic meta is found then the definition is three lines away
				if(inputLine.equals("    <!--Basic meta-->")) {
					defFound = true;
				} else if (defFound) {
					counter++;
				}

				if(counter == 2) {
					def = inputLine;
					break;
				}

			}

			//Split string by spaces
			String[] splitDef = def.split("\\s+");

			String parsedDef = "";

			//The definition always starts 5 words in 
			for(int i = 5; i < splitDef.length; i++) {
				if(splitDef[i].equals(currentWord())) {
					break;
				}
				parsedDef += splitDef[i] + " ";
			}
			in.close();

			//Remove all symbols
			parsedDef = parsedDef.replaceAll("—", "");
			parsedDef = parsedDef.replaceAll(">", "");
			parsedDef = parsedDef.replaceAll("\"", "");
			parsedDef = parsedDef.replaceAll("&", "");


			//Displaying the def or saying no def could be found
			if(parsedDef.isEmpty()) {
				PopupWindow.DeployPopupWindow("Sorry!", "Definition not found!");
			} else {
				PopupWindow.DeployPopupWindow("Def.", parsedDef);
			}

		} catch (MalformedURLException e) { 
			// new URL() failed
			PopupWindow.DeployPopupWindow("No Internet Connection!", "Please connect to internet and try again!");
		} 
		catch (IOException e) {   
			// openConnection() failed
			PopupWindow.DeployPopupWindow("No Internet Connection!", "Please connect to internet and try again!");
		}

	}


}
