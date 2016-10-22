import java.io.IOException;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Sound {
	public static Audio appleCrunch;
	public static Audio music;
	
	public static void init(){
		try{
			appleCrunch = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/applecrunch.wav"));
			music = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/sunflower.wav"));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void playAppleCrunch(){
		appleCrunch.playAsSoundEffect(1.0f, 0.5f, false);
	}
	
	public static void playMusic(){
		music.playAsSoundEffect(1.0f, 0.03f, true);
	}
}
