import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL12;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import static org.lwjgl.opengl.GL11.*;

// I denna klass laddar vi texturer.
public class Textures{
	public static int SPRITES_WIDTH, SPRITES_HEIGHT;
	
	private static BufferedImage spriteSheet;
	public static BufferedImage sprites[][];
	
	public static void init(){
		try{
			spriteSheet = ImageIO.read(new File("res/spriteSheet.png"));
			SPRITES_WIDTH = spriteSheet.getWidth() / Tile.SIZE;
			SPRITES_HEIGHT = spriteSheet.getWidth() / Tile.SIZE;
			sprites = new BufferedImage[SPRITES_WIDTH][SPRITES_HEIGHT];
			
			for(int y = 0; y < SPRITES_HEIGHT; y++){
				for(int x = 0; x < SPRITES_WIDTH; x++){
					sprites[x][y] = spriteSheet.getSubimage(x * Tile.SIZE, y * Tile.SIZE, Tile.SIZE, Tile.SIZE);
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private static final int BYTES_PER_PIXEL = 4;
	public static int loadTexture(BufferedImage image){
		int pixels[] = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL);
		
		for(int y = 0; y < image.getHeight(); y++){
			for(int x = 0; x < image.getWidth(); x++){
				int pixel = pixels[y * image.getWidth() + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF));
				buffer.put((byte) ((pixel >> 8) & 0xFF));
				buffer.put((byte) (pixel & 0xFF));
				buffer.put((byte) ((pixel >> 24) & 0xFF));
			}
		}
		
		buffer.flip();
		
		int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        return textureID;
	}
	
	public static BufferedImage getSprite(int x, int y){
		if(x >= 0 && x < SPRITES_WIDTH && y >= 0 && y < SPRITES_HEIGHT)
			return sprites[x][y];
		return null;
	}
	
	public static void renderTexture(Point p){
		glPushMatrix();
		
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex2f(p.getX() * Tile.SIZE, p.getY() * Tile.SIZE);
			glTexCoord2f(1, 0);
			glVertex2f(p.getX() * Tile.SIZE + Tile.SIZE, p.getY() * Tile.SIZE);
			glTexCoord2f(1, 1);
			glVertex2f(p.getX() * Tile.SIZE + Tile.SIZE, p.getY() * Tile.SIZE + Tile.SIZE);
			glTexCoord2f(0, 1);
			glVertex2f(p.getX() * Tile.SIZE, p.getY() * Tile.SIZE + Tile.SIZE);
		glEnd();
		
		glPopMatrix();
	}
	
	
	public static void rotateAndRenderTexture(Point p, Direction d, int turned){
		int index = Directions.getIndex(d);
		int angle = 90 * index;
		
		if(!(p.getX() >= 0 && p.getX() < Board.TILES && p.getY() >= 0 && p.getY() < Board.TILES && !Main.isInvisible()))
			return;
		
		glPushMatrix();
		
		if(angle > 0){
			if(turned == 0){
				int cX = p.getX() * Tile.SIZE + Tile.SIZE / 2;
				int cY = p.getY() * Tile.SIZE + Tile.SIZE / 2;
			
				glTranslatef(cX, cY, 0);
				glRotatef(angle, 0, 0, 1);
				glTranslatef(-cX, -cY, 0);
			}
		}
		
		renderTexture(p);
		
		glPopMatrix();
	}
	
	public static void renderBackground(){
		loadTexture(getSprite(0, 0));
		
		for(int y = 0; y < Board.TILES; y++){
			for(int x = 0; x < Board.HEIGHT; x++){
				renderTexture(new Point(x, y));
			}
		}
	}
}