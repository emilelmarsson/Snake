import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

/*
 	Snake 2D
	LWJGL Projekt av Emil Elmarsson och Sixten Augustsson
	Oktober 2016
 */

public class Main {
	// Konstanter för bredden och höjden på fönstret. 
	// (final betyder att det är en konstant, för vi vill inte att bredden eller höjden på fönstret ska ändras.)
	public static final int W_WIDTH = 800, W_HEIGHT = 800;
	// Nuvarande stadiet spelet är i. Till exempel main screen, och game.
	public static State state;
	// Spelbrädet.
	public static Board board;
	
	public static void main(String args[]){
		// Default game state
		state = State.GAME;
		
		// Delar upp det i funktioner här så det blir mer läsbart.
		createWindow();
		
		initialize();
		
		// Skapar en matris på skärmen så att vi kan rita på den. Du behöver inte förstå allt som händer här. I don't.
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, W_WIDTH, W_HEIGHT, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		
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
			Display.setDisplayMode(new DisplayMode(W_WIDTH, W_HEIGHT));
			Display.setTitle("Snake");
			// Skapar fönstret.
			Display.create();
		}catch(LWJGLException e){
			// Om något går fel kommer vi hit.
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
	}
	
	public static void initialize(){
		// Laddar texturer i Texturesklassen.
		Textures.init();
	}
	
	public static void render(){
		// Rensa fönstret.
		glClear(GL_COLOR_BUFFER_BIT);
	}
	
	public static void update(){
		// Kollar efter input från spelaren. (Input är en klass vi skapat själva, inte inbyggd i LWJGL.)
		Input.update();
					
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
