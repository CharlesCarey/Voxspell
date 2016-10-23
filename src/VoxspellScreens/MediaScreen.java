package VoxspellScreens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import VoxspellPrototype.VoxspellPrototype;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * 
 * This class handles the display of the video reward to the user
 * 
 * @author Nathan Kear
 *
 */
public class MediaScreen extends Parent {

	private final int BTN_HEIGHT = 50;
	private final String PAUSE_BTN = "Pause";
	private final String LEAVE_BTN = "Leave";
	private final String MUTE_BTN = "Mute";
	private final String FORWARD_BTN = "Forward";
	private final String BACK_BTN = "Back";
	private final String SPOOKIFY_BTN = "Spookify";

	private Window _window;

	// Media window
	private EmbeddedMediaPlayerComponent _mediaPlayerComponent;

	// Player for video
	private EmbeddedMediaPlayer _mediaPlayer;

	// Name of current media
	private String _currentMedia;

	private boolean _specialReward;

	public MediaScreen(Window window, boolean specialReward) {	

		this._window = window;
		this._specialReward = specialReward;
		_window.Show(false);

		testVLCJPresence();

		//Checking that VLCJ does work, it fails here on some versions of VLCJ
		if(createJFrame() != null) {
			createJFrame().setVisible(true);
		}

		//Again checking if VLCJ works
		try {
			_mediaPlayer.playMedia(_currentMedia = "media/bunny.mp4");
			_mediaPlayer.mute(false);
		} catch (Exception e) {
			//Tell the user that video playing won't work and take them back to the main menu
			PopupWindow.DeployPopupWindow("Warning!", "There was an error trying to display the video. The video functionality may not work"
					+ " on your computer!");
			_window.SetWindowScene(new Scene(new MainScreen(_window), _window.GetWidth(), _window.GetHeight()));
			}
	}

	/**
	 * Is VLCJ supported on the machine
	 */
	private void testVLCJPresence() {
		boolean found = new NativeDiscovery().discover();

		//If VLCJ isn't on the user's computer then tell them and redirect them to the main screen
		if(!found) {
			PopupWindow.DeployPopupWindow("Error!", "VLCJ not found! The video won't be able to play!");
			_window.SetWindowScene(new Scene(new MainScreen(_window), _window.GetWidth(), _window.GetHeight()));
		}

		System.out.println("VLCJ test: " + found);
		System.out.println("VLCJ version: " + LibVlc.INSTANCE.libvlc_get_version());
	}

	private JFrame createJFrame() {
		final JFrame frame = new JFrame("Surprise!");	

		// Create all buttons
		final JButton btnPause = new JButton(PAUSE_BTN);
		final JButton btnLeave = new JButton(LEAVE_BTN);
		final JButton btnMute = new JButton(MUTE_BTN);
		final JButton btnForward = new JButton(FORWARD_BTN);
		final JButton btnBack = new JButton(BACK_BTN);
		final JButton btnSpookify = new JButton(SPOOKIFY_BTN);

		// Set buttons size
		btnPause.setPreferredSize(new Dimension(_window.GetWidth(), BTN_HEIGHT));
		btnLeave.setPreferredSize(new Dimension(_window.GetWidth(), BTN_HEIGHT));
		btnMute.setPreferredSize(new Dimension(_window.GetWidth(), BTN_HEIGHT));
		btnForward.setPreferredSize(new Dimension(_window.GetWidth() / 3, BTN_HEIGHT));
		btnBack.setPreferredSize(new Dimension(_window.GetWidth() / 3, BTN_HEIGHT));
		btnSpookify.setPreferredSize(new Dimension(_window.GetWidth() / 3, BTN_HEIGHT));

		// Set button color
		btnPause.setBackground(Color.decode(VoxspellPrototype.LIGHT_BLUE));
		btnLeave.setBackground(Color.decode(VoxspellPrototype.LIGHT_BLUE));
		btnMute.setBackground(Color.decode(VoxspellPrototype.LIGHT_BLUE));
		btnForward.setBackground(Color.decode(VoxspellPrototype.LIGHT_BLUE));
		btnBack.setBackground(Color.decode(VoxspellPrototype.LIGHT_BLUE));
		btnSpookify.setBackground(Color.decode(VoxspellPrototype.LIGHT_BLUE));

		// Set button text color
		btnPause.setForeground(Color.decode(VoxspellPrototype.WHITE));
		btnLeave.setForeground(Color.decode(VoxspellPrototype.WHITE));
		btnMute.setForeground(Color.decode(VoxspellPrototype.WHITE));
		btnForward.setForeground(Color.decode(VoxspellPrototype.WHITE));
		btnBack.setForeground(Color.decode(VoxspellPrototype.WHITE));
		btnSpookify.setForeground(Color.decode(VoxspellPrototype.WHITE));

		// Set font and font size
		btnPause.setFont(new Font("Arial", Font.PLAIN, VoxspellPrototype.BTN_FONT_SIZE));
		btnLeave.setFont(new Font("Arial", Font.PLAIN, VoxspellPrototype.BTN_FONT_SIZE));
		btnMute.setFont(new Font("Arial", Font.PLAIN, VoxspellPrototype.BTN_FONT_SIZE));
		btnForward.setFont(new Font("Arial", Font.PLAIN, VoxspellPrototype.BTN_FONT_SIZE));
		btnBack.setFont(new Font("Arial", Font.PLAIN, VoxspellPrototype.BTN_FONT_SIZE));
		btnSpookify.setFont(new Font("Arial", Font.PLAIN, VoxspellPrototype.BTN_FONT_SIZE));

		// Enable special button on 10/10
		btnSpookify.setEnabled(_specialReward);

		// Pause play button action
		btnPause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Change between pausing and playing media
				if (_mediaPlayer.isPlaying()) {
					_mediaPlayer.pause();
					btnPause.setText("Play");
				} else {
					_mediaPlayer.play();
					btnPause.setText("Pause");
				}
			}	
		});

		// Leave button action
		btnLeave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Switch back to main screen on queued JavaFX thread
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_window.SetWindowScene(new Scene(new MainScreen(_window), _window.GetWidth(), _window.GetHeight()));
						_window.SetWindowPosition(frame.getX(), frame.getY());
						_window.Show(true);
					}
				});

				_mediaPlayer.stop();
				frame.dispose();
			}	
		});

		// Mute unmute action
		btnMute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Switch between muted and unmuted
				if (_mediaPlayer.isMute()) {
					_mediaPlayer.mute(false);
					btnMute.setText("Mute");
				} else {
					_mediaPlayer.mute(true);
					btnMute.setText("Unmute");
				}
			}	
		});

		// Skip forward button action
		btnForward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				_mediaPlayer.skip(10000);
			}	
		});

		// Skip backwards button action
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				_mediaPlayer.skip(-10000);
			}	
		});

		// Make spooky special reward action
		btnSpookify.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Switch between media sources. 
				// Make sure to set time on new media
				// to where old media was.
				long time = _mediaPlayer.getTime();
				boolean isMute = _mediaPlayer.mute();
				if (_currentMedia == "media/bunny.mp4") {
					_mediaPlayer.playMedia(_currentMedia = "media/spooky.mp4");
					btnSpookify.setText("Too spooky!");

				} else if (_currentMedia == "media/spooky.mp4") {
					_mediaPlayer.playMedia(_currentMedia = "media/bunny.mp4");
					btnSpookify.setText("Spookify");
				}
				_mediaPlayer.mute(isMute);
				_mediaPlayer.setTime(time);
			}	
		});

		// Create media player window and the video player
		try {
			_mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
			_mediaPlayer = _mediaPlayerComponent.getMediaPlayer();

			// Layout buttons on JPanel
			JPanel lowerPanel = new JPanel();
			lowerPanel.setLayout(new BorderLayout());
			lowerPanel.add(btnForward, BorderLayout.EAST);
			lowerPanel.add(btnBack, BorderLayout.WEST);
			lowerPanel.add(btnPause, BorderLayout.SOUTH);
			lowerPanel.add(btnMute, BorderLayout.NORTH);
			lowerPanel.add(btnSpookify, BorderLayout.CENTER);

			frame.setResizable(false);
			frame.setLayout(new BorderLayout());
			frame.add(_mediaPlayerComponent, BorderLayout.CENTER);
			frame.add(btnLeave, BorderLayout.NORTH);
			frame.add(lowerPanel, BorderLayout.SOUTH);

			// Position JFrame to size and position of previous Application window
			// So transition looks seamless
			frame.setLocation(_window.GetPosX(), _window.GetPosY());
			frame.setSize(_window.GetWidth(), _window.GetHeight() + 30);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			return frame;
		} catch (Exception e) {
			return null;
		}
	}
}
