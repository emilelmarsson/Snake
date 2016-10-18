import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

import java.util.Random;

/*
 	Snake 2D
	LWJGL Projekt av Emil Elmarsson och Sixten Augustsson
	Oktober 2016
 */

public class Main{
	// Nuvarande stadiet spelet är i. Till exempel main screen, och game.
	public static State state;
	// Spelbrädet.
	public static Board board;
	// Slumpgenerator
	public static Random rng = new Random();
	// Ormen
	public static Snake snake;
	// Äpplet
	public static Apple apple;
	
	
	public static void main(String args[]){
		// Delar upp det i funktioner här så det blir mer läsbart.
		createWindow();
		
		initialize();
		
		// Spelets huvudloop. "Så länge spelaren inte stängt fönstret, fortsätt köra."
		while(!Display.isCloseRequested()){
			render();
			
			update();
		}
		
		destroy();
	}
	
	public static void createWindow(){
		try{
			// Startar ett nytt fönster med den specifierade bredden och höjden.
			Display.setDisplayMode(new DisplayMode(Board.WIDTH, Board.HEIGHT));
			Display.setTitle(Board.TITLE);
			// Skapar fönstret.
			Display.create();
		}catch(LWJGLException e){
			// Om något går fel kommer vi hit.
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
	}
	
	public static boolean isInvisible(){
		return snake.isInvisible();
	}
	
	public static void resetGame(){
		state = State.GAME;
		snake = new Snake();
		apple = new Apple(snake);
	}
	
	public static void initialize(){
		resetGame();
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Board.WIDTH, Board.HEIGHT, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		
		Textures.init();
	}
	
	public static void render(){
		// Rensa fönstret.
		glLoadIdentity();
		glClear(GL_COLOR_BUFFER_BIT);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);  
		
		Textures.renderBackground();
		
		snake.render();
		
		apple.render();
		
		glDisable(GL_BLEND);
	}
	
	public static void update(){
		// Kollar efter input från spelaren. (Input är en klass vi skapat själva, inte inbyggd i LWJGL.)
		Input.update();
		
		apple.update(snake);
		snake.update();
					
		// Uppdaterar skärmen
		Display.update();
		
		// Syncar frame rate (60 FPS)
		Display.sync(60);
	}
	
	public static void destroy(){
		// Förstör fönstret
		Display.destroy();
		System.exit(0);
	}
}
