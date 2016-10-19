import java.util.Arrays;
import java.util.List;

/* Alla metoder i denna klass är statiska eftersom vi inte vill skapa några instanser av den. Allt detta låg i Element klassen förut men 
 * jag flyttade det hit för att städa upp koden lite.
 */
public class Directions{
	// En lista som innehåller alla möjliga riktningar.
	private static final List<Direction> directions = Arrays.asList(Direction.values());
	
	// Antalet riktningar.
	public static int getSize(){
		return directions.size();
	}
	
	// Returnerar en slumpmässig riktning.
	public static Direction getRandomDirection(){
		// Använder sig av Randomobjektet rng i Main.
		return getDirection(Main.rng.nextInt(getSize()));
	}
	
	// Givet ett index returnerar denna metod ett index som garanterat ligger mellan 0 och 3. Alltså de tillåtna indexen för riktningar.
	public static int getIndex(int index){
		return ((index % getSize()) + getSize()) % getSize();
	}
	
	// Returnerar en riktning givet ett index.
	public static Direction getDirection(int index){
		return directions.get(getIndex(index));
	}
	
	/* Hämtar indexet på riktningen.
	 * Direction.NORTH har index 0
	 * Direction.EAST har index 1
	 * Direction.SOUTH har index 2
	 * Direction.WEST har index 3
	 */
	public static int getIndex(Direction d){
		return directions.indexOf(d);
	}
	
	// Returner en punkt där x- och y-koordinaterna är hur ormen ska förflytta sig beroende på vilken riktning den har.
	public static Point getOffset(Direction d){
		Point p;
		
		// Exempelvis om ormen har rikning NORTH så ska den hela tiden subtrahera sin y-position med 1 för att röra sig uppåt. (Origo ligger i översta vänstra hörnet.)
		if(d == Direction.NORTH)
			p = new Point(0, -1);
		else if(d == Direction.EAST)
			p = new Point(1, 0);
		else if(d == Direction.SOUTH)
			p = new Point(0, 1);
		else
			p = new Point(-1, 0);
		
		return p;
	}
	
	// Givet två riktningar så returnerar vi vilken typ av svängning det är så att vi får den lämpliga texturen för svängningen.
	public static int getTurningIndex(Direction d, Direction p){
		if((d == Direction.EAST && p == Direction.SOUTH) || (d == Direction.NORTH && p == Direction.WEST))
			return 1;
		else if((d == Direction.SOUTH && p == Direction.EAST) || (d == Direction.WEST && p == Direction.NORTH))
			return 2;
		else if((d == Direction.NORTH && p == Direction.EAST) || (d == Direction.WEST && p == Direction.SOUTH))
			return 3;
		return 4;
	}
}
