import java.util.ArrayList;

public class Snake{
	private ArrayList<SnakePart> body;
	private ArrayList<SnakePart> moves;
	
	private Timer tick;
	private final int decreaseTick;
	private final int minimumTick;

	private boolean timerStarted;
	private final int blinkAmount;
	private final int blinkTime;
	private int counter;
	private long crashTime;
	private boolean blink;
	
	public Snake(){
		tick = new Timer(200, true);
		decreaseTick = 50;
		minimumTick = 100;
		
		timerStarted = false;
		blinkAmount = 10;
		blinkTime = 200;
		counter = 0;
		crashTime = 0;
		blink = false;
		body = new ArrayList<SnakePart>();
		moves = new ArrayList<SnakePart>();
		
		int randomX = 3 + Main.rng.nextInt(Board.TILES - 2 * 3), randomY = 3 + Main.rng.nextInt(Board.TILES - 2 * 3);
		body.add(new SnakePart(new Point(randomX, randomY), 0));
		
		Direction direction = body.get(0).getDirection();
		Point offset = Directions.getOffset(direction);
		
		int d = Directions.getIndex(direction);
		
		body.add(new SnakePart(new Point(randomX - offset.getX(), randomY - offset.getY()), 1, d));
		body.add(new SnakePart(new Point(randomX - offset.getX() * 2, randomY - offset.getY() * 2), 2, d));
		
		tick.start();
	}
	
	public boolean isInvisible(){
		return blink;
	}
	
	public boolean timerOver(){
		return counter >= blinkAmount;
	}
	
	public int getLength(){
		return body.size();
	}
	
	public boolean snakeLiesHere(int x, int y){
		for(int i = 0; i < body.size(); i++){
			int sX = body.get(i).getX();
			int sY = body.get(i).getY();
			
			if(sX == x && sY == y)
				return true;
		}
		return false;
	}
	
	public SnakePart getHead(){
		return body.get(0);
	}
	
	public void grow(){
		if(tick.getDelay() > minimumTick)
			tick.setDelay(tick.getDelay() - decreaseTick);
		
		SnakePart tail = new SnakePart(body.get(body.size() - 1));
		
		body.remove(body.size() - 1);
		
		Direction d = tail.getDirection();
		Point offset = Directions.getOffset(d);
		SnakePart s = new SnakePart(new Point(tail.getX(), tail.getY()), 1, Directions.getIndex(d));
		
		body.add(s);
		tail.setPoint(new Point(tail.getX() - offset.getX(), tail.getY() - offset.getY()));
		body.add(tail);
	}
	
	public void render(){
		for(int i = 0; i < body.size(); i++){
			body.get(i).render();
		}
	}
	
	public boolean hasCrashed(){
		int hX = getHead().getX(), hY = getHead().getY();
		
		for(int i = 1; i < body.size(); i++)
			if(body.get(i).getX() == hX && body.get(i).getY() == hY){
				if(!timerStarted)
					startCrashTimer();
				return true;
			}
		return false;
	}
	
	public void startCrashTimer(){
		timerStarted = true;
		crashTime = System.nanoTime();
		counter = 0;
	}
	
	public void turn(int direction){
		body.get(0).setDirection(direction == -1);
	}
	
	public void update(){
		if(!hasCrashed()){	
			if(tick.isPastDelay()){	
				if(Input.LEFT || Input.RIGHT)
					turn(Input.LEFT ? -1 : 1);
				
				SnakePart p = new SnakePart(body.get(0));
				moves.add(p);
				body.get(0).changePosition();
				
				for(int i = 1; i < body.size(); i++){
					p = new SnakePart(body.get(i));
					SnakePart c = new SnakePart(moves.remove(0));
					body.get(i).setPosition(c);
					moves.add(p);
				}
				
				moves.clear();
			}
		}else{
			long diff = (System.nanoTime() - crashTime) / 1000000;
			
			if(diff > blinkTime){
				if(counter < blinkAmount){
					crashTime = System.nanoTime();
					counter++;
					blink = !blink;
				}else
					Main.resetGame();
			}
		}
	}
}
