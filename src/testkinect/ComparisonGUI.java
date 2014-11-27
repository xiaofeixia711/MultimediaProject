package testkinect;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ComparisonGUI extends Applet implements ActionListener {
	AudioClip audio;
	Button calibrateButton;
	Button modelButton;
	Button stopButton;
	Label placeholder;
	Panel displayPanel;
	ModelInputPApplet modelApplet = null;
	ComparisonPApplet comparisonApplet = null;
	State state;
	
	enum State {MODELING, CALIBRATING, STOP};

	public void init() {

		audio = getAudioClip(getDocumentBase(), "music.au");

		setSize(1200, 600);
		
		setLayout(new BorderLayout());
		Panel top = new Panel();
        calibrateButton = new Button("Calibrate Me!");
        modelButton = new Button("Input Model...");
        stopButton = new Button("Ok, Stop!");
        top.add(calibrateButton);
        top.add(modelButton);
        top.add(stopButton);
        calibrateButton.addActionListener(this);
		modelButton.addActionListener(this);
		stopButton.addActionListener(this);
        this.add(top, BorderLayout.NORTH);

        displayPanel = new Panel();
        placeholder = new Label("Placeholder for Kinect image.");
        displayPanel.add(placeholder);
        this.add(displayPanel, BorderLayout.CENTER);
        
        state = State.STOP;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == calibrateButton) {
			clearDisplayPanel();
			if (comparisonApplet == null) {
				System.out.println("initiating!");
				comparisonApplet = new ComparisonPApplet();
				if (comparisonApplet == null) {
					System.out.println("null pointer!");
				}
				displayPanel.add(comparisonApplet);
				// important to call this whenever embedding a PApplet.
		        // It ensures that the animation thread is started and
		        // that other internal variables are properly set.
				comparisonApplet.init();
			}
			else {

				System.out.println("Resuming!");
				comparisonApplet.draw();
			}
			state = State.CALIBRATING;
			playAudio();
			validate();
		}

		if (e.getSource() == modelButton) {
			clearDisplayPanel();
			if (modelApplet == null) {
				System.out.println("initiating!");
				modelApplet = new ModelInputPApplet();
				displayPanel.add(modelApplet);
				modelApplet.init();
				this.getAppletContext().showDocument( this.getDocumentBase() );
			}
			else {
				System.out.println("Resuming!");
				modelApplet.start();
			}
			state = State.MODELING;
			playAudio();
			validate();
		}

		if (e.getSource() == stopButton) {
			clearDisplayPanel();
			displayPanel.add(placeholder);
			
			state = State.STOP;
			stopAudio();
			validate();
		}
	}
	
	private void clearDisplayPanel() {
		if (state == State.MODELING) {
			modelApplet.destroy();
			modelApplet = null;
//			applet.getAppletContext().showDocument(appletCloseURL);
		}
		else if (state == State.CALIBRATING) {
			comparisonApplet.stop();
			comparisonApplet = null;
		}
		else {
			displayPanel.removeAll();
		}
		displayPanel.removeAll();
		validate();
	}

	private void playAudio() {
		if (audio != null)
			stop();
		audio.play();
	}

	private void loopAudio() {
		if (audio != null)
			audio.loop();
	}

	private void stopAudio() {
		if (audio != null)
			audio.stop();
	}
	
	// To close the application:
	static class WL extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		ComparisonGUI applet = new ComparisonGUI();
		Frame aFrame = new Frame("Hey");
		aFrame.addWindowListener(new WL());
		aFrame.add(applet, BorderLayout.CENTER);
//		aFrame.setSize(1200, 600);
		applet.init();
		applet.start();
		aFrame.setVisible(true);
	}
	
}