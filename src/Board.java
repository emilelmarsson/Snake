public class Board{
	public static final String TITLE = "Snake";
	public static final int WIDTH = 640, HEIGHT = 640;
	// Antalet rutor på brädet.
	public static int TILES = WIDTH / Tile.SIZE;
	
	// Givet en punkt p returnerar denna funktion en ny punkt som garanterat är inom fönstrets begränsningar.
	public static Point getPoint(Point p){
		int x = ((p.getX() % TILES) + TILES) % TILES;
		int y = ((p.getY() % TILES) + TILES) % TILES;
		
		return new Point(x, y);
	}
}
