import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Animation{
	private int currentFrame;
	private Timer timer;
	private ArrayList<Frame> frames;
	
	public Animation(){
		currentFrame = 0;
		timer = new Timer(0, true);
		frames = new ArrayList<Frame>();
	}
	
	// Add an image to the animation and the time it should be displayed (in ms as usual).
	public void addFrame(BufferedImage frame, long time){
		frames.add(new Frame(frame, timer.getDelay()));
		timer.setDelay(timer.getDelay() + time);
	}
	
	public BufferedImage getCurrentImage(){
		return frames.get(currentFrame).getImage();
	}
	
	public boolean isStarted(){
		return timer.isStarted();
	}
	
	public void update(){
		if(timer.isStarted()){
			for(int i = 0; i < frames.size(); i++){
				//System.out.println(timer.getDifference() + " " + frames.get(i).getTime());
				if(i < frames.size() - 1 && timer.getDifference() > frames.get(i).getTime() && timer.getDifference() < frames.get(i + 1).getTime()){
					currentFrame = i;
					break;
				}else if(i == frames.size() - 1 && timer.getDifference() > frames.get(i).getTime())
					currentFrame = i;
			}
			timer.isPastDelay();
		}
	}
	
	public void start(){
		timer.start();
	}
	
	public void stop(){
		timer.stop();
	}
	
	public int getFrameCount(){
		return frames.size();
	}
	
	public void updateTimer(long time){
		timer.setDelay(time * getFrameCount());
		for(int i = 0; i < getFrameCount(); i++)
			frames.get(i).setTime(time * i);
	}
	
	public long getTime(){
		return timer.getDifference();
	}
	
	public long getDelay(){
		return timer.getDelay();
	}
	
	public int getIndex(){
		return currentFrame;
	}
}