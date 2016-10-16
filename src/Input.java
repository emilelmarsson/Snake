import org.lwjgl.input.Keyboard;

// I denna klass tar vi input från användaren.

public class Input{
	public static boolean UP = false, LEFT = false, RIGHT = false, DOWN = false;
	
	/*
	 *  Kollar om spelaren rör på sig och sparar det i de ovanstående statiska variablerna som vi sen kan använda från Main.
	 *  Vi kanske behöver ändra detta senare ifall vi introducerar flera spelare.
	 */
	public static void update(){
		// Kollar vilket stadium spelet är i just nu. Ifall vi är i home screen behöver vi inte kolla om spelaren rör sig.
		if(Main.state == State.GAME){
			// Spelaren rör sig uppåt om antingen uppåtpilen eller W är nedtryckt. (Eller båda.)
			UP = Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W);
			// Samma princip för de andra tangenterna.
			LEFT = Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A);
			RIGHT = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D);	
			DOWN = Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S);
		}
	}
}