import java.util.Arrays;
import java.util.List;

public class Directions{
	private static final List<Direction> directions = Arrays.asList(Direction.values());
	
	public static int getSize(){
		return directions.size();
	}
	
	public static Direction getRandomDirection(){
		return getDirection(Main.rng.nextInt(getSize()));
	}
	
	public static int getIndex(int index){
		return ((index % getSize()) + getSize()) % getSize();
	}
	
	public static Direction getDirection(int index){
		return directions.get(getIndex(index));
	}
	
	public static int getIndex(Direction d){
		return directions.indexOf(d);
	}
	
	public static Point getOffset(Direction d){
		Point p;
		
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
	
	public static int getTurningIndex(Direction d, Direction p){
		if((d == Direction.EAST && p == Direction.SOUTH) || (d == Direction.NORTH && p == Direction.WEST))
			return 1;
		else if((d == Direction.SOUTH && p == Direction.EAST) || (d == Direction.WEST && p == Direction.NORTH))
			return 2;
		else if((d == Direction.NORTH && p == Direction.EAST) || (d == Direction.WEST && p == Direction.SOUTH))
			return 3;
		//else if((d == Direction.SOUTH && p == Direction.WEST) || (d == Direction.EAST && p == Direction.NORTH))
		return 4;
	}
}
