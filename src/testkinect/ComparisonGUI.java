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
		clearDisplayPanel();
		
		if (e.getSource() == calibrateButton) {
			System.out.println("calibrateButton: " + comparisonApplet);
			if (comparisonApplet == null) {
				System.out.println("initiating!");
				comparisonApplet = new ComparisonPApplet();
				if (comparisonApplet == null) {
					System.out.println("null pointer!");
				}
				displayPanel.add(comparisonApplet);
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

		else if (e.getSource() == modelButton) {
			System.out.println("modelButton: " + modelApplet);
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

		else if (e.getSource() == stopButton) {
			displayPanel.add(placeholder);
			state = State.STOP;
			stopAudio();
			validate();
		}
	}
	
	private void clearDisplayPanel() {
		if (state == State.MODELING && modelApplet != null) {
			System.out.println("Destroying model applet");
			modelApplet.destroy();
			modelApplet = null;
			System.out.println(modelApplet);
		}
		else if (state == State.CALIBRATING && comparisonApplet != null) {
			System.out.println("Destroying comparison applet");
			comparisonApplet.destroy();
			comparisonApplet = null;
			System.out.println(comparisonApplet);
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