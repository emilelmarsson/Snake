public class Timer{
	private boolean started, repeats;
	private long delay;
	private long lastTime;
	
	private long pauseTime;
	
	public Timer(long delay, boolean repeats){
		started = false;
		this.repeats = repeats;
		
		this.delay = delay;
	}
	
	public boolean isPastDelay(){
		if(!started)
			return false;
		long diff = (System.nanoTime() - lastTime) / 1000000;
		if(diff > delay){
			if(repeats)
				start();
			else
				stop();
			return true;
		}
		return false;
	}
	
	public void start(){
		started = true;
		lastTime = System.nanoTime();
	}
	
	public void pause(){
		pauseTime = System.nanoTime();
	}
	
	public void resume(){
		lastTime += (System.nanoTime() - pauseTime);
	}
	
	public void stop(){
		started = false;
	}
	
	public long getDelay(){
		return delay;
	}
	
	public void setDelay(long delay){
		this.delay = delay;
		if(repeats)
			start();
		else
			stop();
	}
}
