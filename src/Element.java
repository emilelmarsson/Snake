import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Element{
	protected Point p;
	
	public Element(Point p){
		this.p = Board.getPoint(p);
	}
	
	int getX(){
		return p.getX();
	}
	
	int getY(){
		return p.getY();
	}
	
	Point getPoint(){
		return p;
	}
	
	public void setPoint(Point p){
		this.p = p;
	}
	
	abstract void render();
}

class Apple extends Element{
	public Apple(Snake s){
		super(new Point(0, 0));
		generateRandomPosition(s);
	}
	
	public Apple(int x, int y){
		super(new Point(x, y));
	}
	
	public void generateRandomPosition(Snake s){
		ArrayList<Point> points = new ArrayList<Point>();
		
		for(int i = 0; i < Board.TILES; i++)
			for(int j = 0; j < Board.TILES; j++)
				if(!s.snakeLiesHere(i, j))
					points.add(new Point(i, j));
		
		int index = Main.rng.nextInt(points.size());
		p.setX(points.get(index).getX());
		p.setY(points.get(index).getY());
	}
	
	public void render(){
		Textures.loadTexture(Textures.getSprite(0, 2));
		Textures.renderTexture(p);
	}
	
	public void update(Snake s){
		int sX = s.getHead().getX();
		int sY = s.getHead().getY();
		
		if(sX == p.getX() && sY == p.getY()){
			s.grow();
			generateRandomPosition(s);
		}
	}
}

class SnakePart extends Element{
	private static final List<Part> bodyParts = Arrays.asList(Part.values());
	
	private Direction d;
	private Part part;
	private int turned;
	
	public SnakePart(SnakePart p){
		super(p.getPoint());
		
		this.d = p.getDirection();
		this.part = p.getPart();
		this.turned = 0; //p.getTurn();
	}
	
	public SnakePart(Point p, int type){
		super(p);
		
		if(type >= 0 && type < bodyParts.size())
			part = bodyParts.get(type);
		else
			part = Part.BODY;
		
		d = Directions.getRandomDirection();
		
		turned = 0;
	}
	
	public SnakePart(Point p, int type, int direction){
		super(p);
		
		if(type >= 0 && type < bodyParts.size())
			part = bodyParts.get(type);
		else
			part = Part.BODY;
		
		if(direction >= 0 && direction < Directions.getSize())
			d = Directions.getDirection(direction);
		else
			d = Direction.NORTH;
		
		turned = 0;
	}
	
	public Direction getDirection(){
		return d;
	}
	
	public int getTurn(){
		return turned;
	}
	
	public Part getPart(){
		return part;
	}
	
	public void setDirection(boolean turnLeft){
		int offset = turnLeft ? -1 : 1;
		
		setDirection(Directions.getDirection(Directions.getIndex(d) + offset));
	}
	
	public void setDirection(Direction d){
		this.d = d;
	}
	
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