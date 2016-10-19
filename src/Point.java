// En punkt vid en viss position. (Detta förenklar väldigt mycket då man kan får hälfen så många variabler att handskas att med då x och y-värdena sparas i ett objekt.
public class Point{
	private int x, y;
	
	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Point(Element p){
		x = p.getX();
		y = p.getY();
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
}
