package cardgame;

import java.applet.AudioClip;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame extends Frame implements ActionListener {
	
	Game game;
	Button playSound, loopSound,
	stopSound, changeSound;
	
	boolean changeSong = false;
	
	public GameFrame (Game X){
		setLayout(new FlowLayout());
		game = X;

		this.playSound = new Button("Play Music");
		playSound.addActionListener(this);
		add(playSound);
		this.playSound.setSize(100, 40);
		this.playSound.setLocation(100, 50);

		this.loopSound = new Button("Loop");
		loopSound.addActionListener(this);
		add(loopSound);
		this.loopSound.setSize(60, 40);
		this.loopSound.setLocation(225, 50);

		this.stopSound = new Button("Stop");
		stopSound.addActionListener(this);
		add(stopSound);
		this.stopSound.setSize(60, 40);
		this.stopSound.setLocation(325, 50);
		
		this.changeSound = new Button("Change Music");
		changeSound.addActionListener(this);
		add(changeSound);
		this.changeSound.setSize(100, 40);
		this.changeSound.setLocation(450, 50);
	}
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == playSound) {
			if (changeSong == false)
				game.playSound();
			else
				game.playSound2();
		}
		else if (source == stopSound)
			game.stopSound();
		else if (source == changeSound) {
			changeSong = !changeSong;
			game.stopSound();
			game.playSound2();
		}
		else if (source == loopSound) {
			if (changeSong == false)
				game.loopSound();
			else
				game.loopSound2();
		}

	}
}

