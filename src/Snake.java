import java.util.ArrayList;

// Denna klass innehåller ormens delar. Det är den mest komplicerade, då den basically kombinerar alla andra klasser.
public class Snake{
	// Det här är en lista som innehåller alla ormens kroppsdelar. Varje kroppsdel representeras av ett SnakePartobjekt.
	private ArrayList<SnakePart> body;
	// Det här är en lista som innehåller de drag som spelaren gör. Detta görs för att alla andra kroppsdelar ska vara
	// synkroniserade gentemot huvudet.
	private ArrayList<SnakePart> moves;
	
	// Ifall spelaren trycker på vänsterpiltangenten blir denna variabel satt till -1.
	// Ifall spelaren trycker på högerpiltangenten blir denna variabel satt till 1.
	private int shouldTurn;
	
	// En timer som räknar när ormen ska röra sig.
	private Timer tick;
	// Denna variabel reglerar hur mycket snabbare ormen rör sig när den äter ett äpple.
	private final int decreaseTick;
	// Det minsta värdet som ticken kan vara. Med andra ord, det snabbaste ormen kan röra sig.
	private final int minimumTick;

	// Alla dessa variabler är för att reglera hur ormen blinkar när den dör. Som en simpel övergångsanimation.
	private boolean timerStarted;
	// Bestämmer hur mycket timern ska blinka på den bestämda tiden. Ökar vi dess värde blinkar det snabbare.
	private final int blinkAmount;
	// Hur lång en blinkning ska vara (i millisekunder).
	private final int blinkTime;
	// En räknare som håller koll på hur många gånger vi blinkat, med andra ord hur många gånger crashTime passerat blinkTime.
	private int counter;
	// Variabel som sparar tiden vid ett visst stadie. Används för att ta fram förflutna tiden.
	private long crashTime;
	// Är sann om ormen inte ska visas just nu. Alltså själva blinkningen.
	private boolean blink;
	
	public Snake(){
		// Initialiserar alla variabler
		shouldTurn = 0;
		
		tick = new Timer(500, true);
		decreaseTick = 25;
		minimumTick = 100;
		
		timerStarted = false;
		blinkAmount = 10;
		blinkTime = 200;
		counter = 0;
		crashTime = 0;
		blink = false;
		
		body = new ArrayList<SnakePart>();
		moves = new ArrayList<SnakePart>();
		
		/*
		 * Lägger till de första kroppsdelarna till ormen, alltså ett huvud, en kropp och en svans.
		 * randomX är positionen på huvudet, och genereras som ett slumpmässigt tal mellan 3 och storleken
		 * på fönstret minus 3. Detta är för att man inte ska krocka på en gång när man spawnar. randomY
		 * genereras på samma sätt. För att göra detta använder vi oss av standardklassen Random, som vi har en instans av
		 * i Main, kallad rng.
		 */
		int randomX = Main.rng.nextInt(Board.TILES), randomY = Main.rng.nextInt(Board.TILES);
		body.add(new SnakePart(new Point(randomX, randomY), Part.HEAD));
		
		// Riktningen på kroppen och svansen ska vara samma som huvudets riktning när man spawnar.
		Direction direction = body.get(0).getDirection();
		
		/* Beroende på riktningen på huvudet så ska positionen på de andra kroppsdelarna vara olika.
		 * Till exempel om ormen rör sig uppåt (detta beskrivs genom Direction.NORTH) så ska varje kroppsdel efter
		 * det förflyttas en y-position nedåt. Då blir vårt offset (0, 1), eftersom vi inte ska förflytta i x-led i så fall.
		 * Gå till Directionsklassen för att se hur just den funktionen fungerar. Då blir det klarare.
		 */
		Point offset = Directions.getOffset(direction);
		
		// Lägger till kroppen.
		body.add(new SnakePart(new Point(randomX - offset.getX(), randomY - offset.getY()), Part.BODY, direction));
		body.add(new SnakePart(new Point(randomX - offset.getX() * 2, randomY - offset.getY() * 2), Part.TAIL, direction));
		
		// Startar timer.
		tick.start();
	}
	
	public long getTick(){
		return tick.getDifference() / 1000000;
	}
	
	public long getDelay(){
		return tick.getDelay();
	}
	
	// Om ormen har dött så vill vi kolla om den ska renderas just nu eller inte. Detta görs för att vi ska kunna få ormen att blinka när den dör.
	public boolean isInvisible(){
		return blink;
	}
	
	// Returnerar true om blinkTimern är över.
	public boolean timerOver(){
		return counter >= blinkAmount;
	}
	
	// Returnerar nuvarande längden på ormen. Med andra ord, längden på listan med kroppsdelar.
	public int getLength(){
		return body.size();
	}
	
	// Givet parametrarna x och y kollar denna funktion om någon av ormens kroppsdelar ligger på just den positionen.
	public boolean snakeLiesHere(int x, int y){
		// Iterera genom listan av kroppsdelar.
		for(int i = 0; i < body.size(); i++){
			// För varje kroppsdel hämtar vi dess position
			int sX = body.get(i).getX();
			int sY = body.get(i).getY();
			
			// Om positionerna är lika, returnera true. Annars fortsätter vi loopen.
			if(sX == x && sY == y)
				return true;
		}
		return false;
	}
	
	// Returnerar huvudet.
	public SnakePart getHead(){
		return body.get(0);
	}
	
	// Denna funktion ökar storleken på ormen, och kallas på när ormen ätit ett äpple.
	public void grow(){
		/* Här gör vi så att ormen åker snabbare, genom att minska ticket på timern. Ursprungligen ligger den på 500 ms, d.v.s. den kommer
		 * förflytta sig två gånger varje sekund. Sedan minskar vi denna tid, till ett minimum på 100 ms som bestäms av variabeln minimumTick.
		 * Då kommer ormen röra sig 10 gånger per sekund.
		 */
		if(tick.getDelay() > minimumTick)
			tick.setDelay(tick.getDelay() - decreaseTick);
		
		/* Här hämtar vi svansen på ormen. Det kommer vara den sista kroppsdelen i listan så därför hämtar vi från  indexet "längden på listan minus 1".
		 * Obs. att vi skapar ett nytt SnakePartobjekt. Detta görs för att vi inte vill få en referens till svansen, utan endast en kopia av svansen.
		 */
		SnakePart tail = new SnakePart(body.get(body.size() - 1));
		
		// Här tar vi bort den långa ludna svansen.
		body.remove(body.size() - 1);
		
		
		Direction d = tail.getDirection();
		Point offset = Directions.getOffset(d);
		// Sätter den nya delens position och riktning baserat på svansen.
		SnakePart s = new SnakePart(new Point(tail.getX(), tail.getY()), Part.BODY, d);
		
		// Lägger till den nya delen, samt lägger tillbaka svansen med en ny position.
		body.add(s);
		tail.setPoint(new Point(tail.getX() - offset.getX(), tail.getY() - offset.getY()));
		body.add(tail);
	}
	
	// Rendera alla ormens delar.
	public void render(){
		for(int i = 0; i < body.size(); i++){
			body.get(i).render();
		}
	}
	
	// Denna funktion kollar om ormen har kraschat. Om den gjort dör ormen och det startas en timer för blinkningen.
	public boolean hasCrashed(){
		int hX = getHead().getX(), hY = getHead().getY();
		
		for(int i = 1; i < body.size(); i++)
			if(body.get(i).getX() == hX && body.get(i).getY() == hY){
				// Om timern inte redan är startad, starta den.
				if(!timerStarted)
					startCrashTimer();
				return true;
			}
		return false;
	}
	
	// Startar timern.
	public void startCrashTimer(){
		timerStarted = true;
		// Sätter denna variabel till tiden i nanosekunder. Detta gör för att vi sedan ska kunna kolla skillnaden och därmed se hur mycket tid som passerat.
		crashTime = System.nanoTime();
		counter = 0;
	}
	
	public void turn(int direction){
		body.get(0).setDirection(direction == -1);
	}
	
	public void update(){
		// Så länge inte ormen har kraschat ska den röra på sig som vanligt.
		if(!hasCrashed()){	
			/* Om spelaren trycker på vänster eller höger piltangent så ska variabeln shouldTurn sättas til -1 eller 1.
			 * Detta görs för att vi i nästa tick ska kunna kolla om spelaren ska röra sig eller inte.
			 */
			if(Input.LEFT || Input.RIGHT){
				//System.out.println(tick.getDifference() / 1000000 + " av " + tick.getDelay());
				shouldTurn = Input.LEFT ? -1 : 1;
			}if(Input.PRESSED)
				shouldTurn = 0;
			
			/* Om timern överskridit sin delay (som ursprungligen ligger på 500 millisekunder, as of now) så ska ormen röra på sig.
			 * Hade vi inte haft denna begränsning hade ormen rört sig varje frame, och eftersom spelet körs i 60 fps så hade det varit
			 * ungefär var 16:e millisekund, vilket är extremt snabbt.
			 */
			if(tick.isPastDelay()){	
				Input.PRESSED = false;
				// Om spelaren har begärt få svänga under föregående tick, så ska ormen svänga.
				if(shouldTurn != 0){
					turn(shouldTurn);
					shouldTurn = 0;
				}
					
				// Tar det förflyttade huvudet och lägger det i moveslistan.
				SnakePart p = new SnakePart(body.get(0));
				moves.add(p);
				body.get(0).changePosition();
				
				// Rör på alla de andra kroppsdelarna beroende på hur huvudet rörde sig.
				// Itererar genom alla kroppsdelar.
				for(int i = 1; i < body.size(); i++){
					p = new SnakePart(body.get(i));
					SnakePart c = new SnakePart(moves.remove(0));
					// Sätter denna kroppsdels position till föregående kroppsdels position. (Med föregående syftar jag på länge fram på ormen eftersom vi börjar från huvudet.)
					body.get(i).setPosition(c);
					// Lägger till detta drag i moveslistan så att nästa kroppsdel också rör sig åt det här hållet.
					moves.add(p);
				}
				
				// Efter att alla kroppsdelar är förflyttade rensar vi listan.
				moves.clear();
			}
		}else{
			// Om ormen kraschat kommer vi hit.
			// diff är passerad tid sedan senaste ticken. Till slut blir värdet större än tiden för en blinkning.
			long diff = (System.nanoTime() - crashTime) / 1000000;
			
			if(diff > blinkTime){
				// Då kommer vi hit.
				if(counter < blinkAmount){
					// Om antalet blinkningar är färre än det maximala startar vi om timern och kör samma process igen.
					crashTime = System.nanoTime();
					counter++;
					// Dessutom sätter vi blink till negation av sig själv. Det vill säga om blink är true blir den false och vice versa.
					// Detta är alltså själva alterneringen i blinkingen.
					blink = !blink;
				}else
					// Om ormen har blinkat tillräckligt många gånger startar vi om spelet.
					Main.resetGame();
			}
		}
	}
}
