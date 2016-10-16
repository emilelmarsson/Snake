import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import static org.lwjgl.opengl.GL11.*;

// I denna klass laddar vi texturer.
public class Textures{
	// Sökväg till sprite sheet
	public static final String PATH = "";
	
	// Storleken på en textur (i pixlar).
	public static final int S_SIZE = 50;
	
	// Alla texturer.
	private static Texture spriteSheet;
	
	// Här initialiserar vi texturerna.
	public static void init(){
		try{
			// Laddar texturer från den angivna sökvägen.
			spriteSheet = TextureLoader.getTexture("PNG", new FileInputStream(new File(PATH)));
		}catch(FileNotFoundException e){
			// Om något går fel kommer vi hit.
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}catch(IOException e){
			// Eller hit.
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
		
		// Det är här LWJGL kommer in.
		for(int i = 0; i < S_SIZE; i++){
			glTexCoord2f((float)i / S_SIZE, (float)i / S_SIZE);
			glVertex2f(i * S_SIZE, i * S_SIZE);
			
		}
	}
}