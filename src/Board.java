
// Spelbrädet
public class Board{
	// Bredden och höjden på brädet. (I antal rutor, inte pixlar.)
	private int width, height;
	
	// En tvådimensionell array som innehåller alla rutor. För att hämta en ruta använder man tiles[x][y].
	private Tile tiles[][];
	
	public Board(int width, int height){
		// Eftersom variablerna har samma namn måste man skilja på dem på något sätt. De som tillhör denna klass
		// refererar man till med hjälp av nyckelordet this.
		this.width = width;
		this.height = height;
		
		// Definierar ett rutnät med de specifierade dimensionerna.
		tiles = new Tile[width][height];
	}
	
	public Tile getTile(int x, int y){
		/* 
		 * Modulooperatorn ger resten vid division. Till exempel om jag anger att jag vill ha (x,y)=(51, 51) men 
		 * höjden och bredden på rutnätet är 50, så kommer vi få (x,y) = (51 % 50, 51 % 50) = (1, 1). Detta gör vi
		 * för att ormen ska kunna gå runt om den kommer till en kant.
		 */
		x = x % width;
		y = y % height;
		
		return tiles[x][y];
	}
}
