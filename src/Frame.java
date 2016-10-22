import java.awt.image.BufferedImage;

public class Frame{
	private BufferedImage image;
	private long time;
	
	public Frame(BufferedImage image, long time){
		setImage(image);
		setTime(time);
	}
	
	public void setTime(long time){
		this.time = time;
	}
	
	public void setImage(BufferedImage image){
		this.image = image;
	}
	
	public long getTime(){
		return time;
	}
	
	public BufferedImage getImage(){
		return image;
	}
}
