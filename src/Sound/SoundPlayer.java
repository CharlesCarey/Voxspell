package Sound;

import javafx.concurrent.Task;

public class SoundPlayer extends Task<Void>{

	private static boolean _isCorrect;
	private static boolean _excellentQuiz;

	/**
	 * This sets the field _isCorrect which determines which sound to play for the user
	 * @param wasCorrect - if the user was correct
	 */
	public static void userWasCorrect(boolean wasCorrect) {
		_isCorrect = wasCorrect;
	}

	/**
	 * If the user got 80% in a quiz then this method sets the field so that the sound player cheers
	 * @param userWasGreat - whether or not the user scored higher than or equal to 80%
	 */
	public static void userDidGreat(boolean userWasGreat) {
		_excellentQuiz = userWasGreat;
	}

	@Override
	/**
	 * Calling ffmpeg to play sounds for the user in background.
	 * 
	 * These sounds are all free to use and come from https://www.freesound.org/
	 * 
	 */
	protected Void call() throws Exception {

		//If the user got over 80% right then play cheering
		if(_excellentQuiz) {
			
			new ProcessBuilder("/bin/bash", "-c", "ffplay -autoexit -nodisp -loglevel panic ./Sound-Effects/Cheering.wav").start();

		} else {
			//Else if user got a word correct the play correct noise, otherwise play incorrect noise
			if(_isCorrect) {
				new ProcessBuilder("/bin/bash", "-c", "ffplay -autoexit -nodisp -loglevel panic ./Sound-Effects/Correct.wav").start();
			} else {
				new ProcessBuilder("/bin/bash", "-c", "ffplay -autoexit -nodisp -loglevel panic ./Sound-Effects/Incorrect.wav").start();
			}

		}
		return null;
	}

}

