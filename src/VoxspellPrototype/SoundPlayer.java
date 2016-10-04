package VoxspellPrototype;

import javafx.concurrent.Task;

public class SoundPlayer extends Task<Void>{
	
	private static boolean _isCorrect;
	
	public static void userWasCorrect(boolean wasCorrect) {
		_isCorrect = wasCorrect;
	}

	@Override
	protected Void call() throws Exception {

		if(_isCorrect) {
			new ProcessBuilder("/bin/bash", "-c", "ffplay -autoexit -nodisp -loglevel panic ./Sound-Effects/Correct.wav").start();
		} else {
			new ProcessBuilder("/bin/bash", "-c", "ffplay -autoexit -nodisp -loglevel panic ./Sound-Effects/Incorrect.wav").start();
		}
		
		
		return null;
	}

}

