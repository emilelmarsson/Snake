import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Detta är en abstrakt klass, vilket ungefär är som en mall för andra klasser att bygga på. Som exempel skulle man kunna säga att Bil är en
 * abstrakt klass för alla bilar, som innehåller grundläggande variabler och klasser för hur en bil fungerar. Sen kan olika typer av bilar
 * "extenda" den klassen genom att implementera fler funktioner. Till exempel skulle man kunna tänka sig att man har en klass FyrsitsigFyrhjulsdrivenBil
 * eller något liknande. Denna skiljer sig lite från den abstrakta klassen som den ärver ifrån.
 * 
 * I vårt fall är denna abstrakta klass ett element som kan visas på skärmen. Vi har två klasser som extendar denna, Apple och SnakePart.
 */
public abstract class Element{
	// En punkt med koordinater till elementet.
	protected Point p;
	
	/* Konstruktorn tar en punkt och ser till att den är inom fönstrets begränsningar. Om vi till exempel ger den en punkt med koordinaterna (-1, -1)
	 * vill vi justera den punkten så att den hamnar innanför skärmen. Hur detta går till kan ni se i Boardklassen.
	 */
	public Element(Point p){
		this.p = Board.getPoint(p);
	}
	
	// Returnerar x-koordinaten.
	int getX(){
		return p.getX();
	}
	
	// Returnerar y-koordinaten.
	int getY(){
		return p.getY();
	}
	
	// Returnerar hela punkten.
	Point getPoint(){
		return p;
	}
	
	// Ändrar positionen.
	public void setPoint(Point p){
		this.p = p;
	}
	
	// En abstrakt funktion. De klasser som ärver av Element måste implementera denna.
	abstract void render();
}

// Äppelklassen
class Apple extends Element{
	public Apple(Snake s){
		super(new Point(0, 0));
		generateRandomPosition(s);
	}
	
	public Apple(int x, int y){
		super(new Point(x, y));
	}
	
	// Genererar en slumpmässig position baserat på ormen. Ett äpple får nämligen inte placeras på ormen.
	public void generateRandomPosition(Snake s){
		// En lista med alla potentiella positioner.
		ArrayList<Point> points = new ArrayList<Point>();
	
		for(int i = 0; i < Board.TILES; i++)
			for(int j = 0; j < Board.TILES; j++)
				// Ifall ormen inte ligger på nuvarande position lägger vi till den positionen som potentiell.
				if(!s.snakeLiesHere(i, j))
					points.add(new Point(i, j));
		
		// Genererar ett slumpmässigt index baserat på längden av listan med potentiella positioner.
		int index = Main.rng.nextInt(points.size());
		p.setX(points.get(index).getX());
		p.setY(points.get(index).getY());
	}
	
	public void render(){
		// Laddar äpplets textur som ligger på position (0, 2).
		Textures.loadTexture(Textures.getSprite(0, 2));
		// Renderar texturen på punkt p.
		Textures.renderTexture(p);
	}
	
	public void update(Snake s){
		int sX = s.getHead().getX();
		int sY = s.getHead().getY();
		
		// Om huvudet är på samma position som äpplet, så äter ormen upp äpplet. Därmed växer ormen, och en ny position till äpplet måste genereras.
		if(sX == p.getX() && sY == p.getY()){
			s.grow();
			Sound.playAppleCrunch();
			generateRandomPosition(s);
		}
	}
}

// En klass för delarna i ormen.
class SnakePart extends Element{
	// Alla de möjliga kroppsdelarna. (Part.HEAD, Part.BODY, Part.TAIL.)
	private static final List<Part> bodyParts = Arrays.asList(Part.values());
	
	// Varje del har en riktning för att vi ska veta vilket håll den rör sig åt samt hur vi bör rotera den när vi renderar.
	private Direction d;
	// Beskriver vilken typ av del detta är. (Part.HEAD, Part.BODY, Part.TAIL.)
	private Part part;
	/*
	 * turned == 0 om ormen inte svänger.
	 * turned är från 1 till 4 om ormen svänger. De olika indexen används för att ta fram vilken typ av svängning det är. (Från väst till syd etc.)
	 */
	private int turned;
	
	// Kopierar en SnakePart.
	public SnakePart(SnakePart p){
		super(p.getPoint());
		
		this.d = p.getDirection();
		this.part = p.getPart();
		this.turned = 0; //p.getTurn();
	}
	
	// Gör en ny ormdel. Den får en punkt och en typ.
	public SnakePart(Point p, Part part){
		super(p);
		
		this.part = part;
		
		// Hämtar en slumpmässig riktning.
		d = Directions.getRandomDirection();
		
		turned = 0;
	}
	
	// Gör en ny ormdel av en punkt, en del och en riktning.
	public SnakePart(Point p, Part part, Direction d){
		super(p);
		
		this.part = part;
		
		this.d = d;
		
		turned = 0;
	}
	
	// Returnerar delens riktning
	public Direction getDirection(){
		return d;
	}
	
	// Returnerar svängningsindexet.
	public int getTurn(){
		return turned;
	}
	
	// Returnerar vilken typ denna del är. (Part.HEAD, Part.BODY, Part.TAIL.)
	public Part getPart(){
		return part;
	}
	
	// Ändrar riktning. Om turnLeft är sann svänger vi vänster, annars höger.
	public void setDirection(boolean turnLeft){
		int offset = turnLeft ? -1 : 1;
		
		setDirection(Directions.getDirection(Directions.getIndex(d) + offset));
	}
	
	// Ändrar riktning
	public void setDirection(Direction d){
		this.d = d;
	}
	
	// Ändrar position till en annan ormdels position.
	public void setPosition(SnakePart p){
		this.p.setX(p.getX());
		this.p.setY(p.getY());
		
		if(d == p.getDirection()){
			turned = 0;
		}else{		
			turned = Directions.getTurningIndex(d, p.getDirection());
			d = p.getDirection();
		}
	}
	
	// Ändrar positionen.
	public void changePosition(){
		Point offset = Directions.getOffset(d);
		
		p.setX(p.getX() + offset.getX());
		p.setY(p.getY() + offset.getY());
		
		p = Board.getPoint(p);
	}
	
	public void render(){
		if(part == Part.HEAD){
			Textures.loadTexture(Textures.getSprite(1, 0));
			Textures.rotateAndRenderTexture(p, d, 0);
		}else if(part == Part.BODY){
			if(turned == 0)
				Textures.loadTexture(Textures.getSprite(2, 0));
			else
				Textures.loadTexture(Textures.getSprite(4, turned - 1));
			
			Textures.rotateAndRenderTexture(p, d, turned);
		}else{
			Textures.loadTexture(Textures.getSprite(3, 0));
			Textures.rotateAndRenderTexture(p, d, 0);
		}
	}
}