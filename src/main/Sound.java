package main;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

    // CONSTANTS
    public static final int MAX_SOUNDS = 30;

    Clip clip;
    URL soundURL[] = new URL[MAX_SOUNDS];
    public Sound() {

        // CONSTANTS
        String[] audioPath = { "BlueBoyAdventure.wav", "coin.wav", "powerup.wav", "unlock.wav", "fanfare.wav" };
        for (int i = 0; i < audioPath.length; i++) {
            
            try {
                
                soundURL[i] = getClass().getResource("/sound/" + audioPath[i]);
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
    }

    // LINKING EVENT TO SOUND
    public void setFile(int i) {
        try {
            
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.out.println("Error: " + e);
        }
    }

    // CONTROLS
    public void play() {
        clip.start();
        
    }
    public void loop() {
        
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop() {
        clip.stop();

    }
}
