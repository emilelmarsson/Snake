public class Board{
	public static final String TITLE = "Snake";
	public static final int WIDTH = 640, HEIGHT = 640;
	public static int TILES = WIDTH / Tile.SIZE;
	
	public static Point getPoint(Point p){
		int x = ((p.getX() % TILES) + TILES) % TILES;
		int y = ((p.getY() % TILES) + TILES) % TILES;
		
		return new Point(x, y);
	}
}
