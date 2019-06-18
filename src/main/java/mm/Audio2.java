import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.sound.sampled.*;
import java.io.*;

public class Audio2 extends Thread{

    private String filePath;
    private AudioStream audioStream;
    private InputStream inputStream;
    private int mode;

    public Audio2(String filePath, int mode) {
        this.mode = mode;
        this.filePath = filePath;
    }


    @Override
    public void run() {
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            if (mode == 2) clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } 
        while (audioStream != null) { }
    }

    public static void playShot() {
         new Audio2("audio/shoot.wav",1).start();
    }

    public static void playBGM() {
         new Audio2("audio/main.wav", 2).start();
    }
    public static void playEat() {
         new Audio2("audio/eat.wav", 3).start();
    }
}