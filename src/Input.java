import org.lwjgl.input.Keyboard;

// I denna klass tar vi input från användaren.

public class Input{
	public static boolean LEFT = false, RIGHT = false;
	public static boolean PRESSED = false;
	
	public static long before = -1000;
	/*
	 *  Kollar om spelaren rör på sig och sparar det i de ovanstående statiska variablerna som vi sen kan använda från Main.
	 *  Vi kanske behöver ändra detta senare ifall vi introducerar flera spelare.
	 */
	public static void update(){
		// Kollar vilket stadium spelet är i just nu. Ifall vi är i home screen behöver vi inte kolla om spelaren rör sig.
		if(Main.state == State.GAME){
			/*LEFT = Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A);
			RIGHT = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D);*/
			
			while(Keyboard.next()){
				if (Keyboard.getEventKey() == Keyboard.KEY_A || Keyboard.getEventKey() == Keyboard.KEY_LEFT) {
				    if (Keyboard.getEventKeyState()){
				    	LEFT = true;
				    	before = Main.snake.getTick();
				    }else{
				    	if(Main.snake.getTick() < before)
				    		PRESSED = true;
				        LEFT = false;
				        before = -1000;
				    }
				}else if (Keyboard.getEventKey() == Keyboard.KEY_D || Keyboard.getEventKey() == Keyboard.KEY_RIGHT) {
				    if (Keyboard.getEventKeyState()) {
				        RIGHT = true;
				        before = Main.snake.getTick();
				    }else{
				    	if(Main.snake.getTick() < before)
				    		PRESSED = true;
				        RIGHT = false;
				        before = -1000;
				    }
				}
			}
		}
	}
}