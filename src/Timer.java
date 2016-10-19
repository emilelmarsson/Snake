// En timer.
public class Timer{
	// started avgör om timern är started
	// repeats är falsk om timern bara ska ticka en gång.
	private boolean started, repeats;
	// Begränsningen. När timern överskridit denna har vi ett tick. 
	private long delay;
	// Lagrar tiden sedan det senaste ticket.
	private long lastTime;
	
	// Om vi pausar timern.
	private long pauseTime;
	
	public Timer(long delay, boolean repeats){
		started = false;
		this.repeats = repeats;
		
		this.delay = delay;
	}
	
	// Returnerar true om tiden har passerat begränsningen.
	public boolean isPastDelay(){
		if(!started)
			return false;
		// Diff är skillnaden.
		long diff = (System.nanoTime() - lastTime) / 1000000;
		if(diff > delay){
			// Om denna är större än delay ska vi
			if(repeats)
				// omstarta timern om repeatsflaggan är true.
				start();
			else
				// Annars stannar vi den.
				stop();
			return true;
		}
		return false;
	}
	
	// Returnerar skillnaden sedan föregående tick.
	public long getDifference(){
		return System.nanoTime() - lastTime;
	}
	
	// Startar (eller omstartar) timern.
	public void start(){
		started = true;
		lastTime = System.nanoTime();
	}
	
	// Pausar
	public void pause(){
		pauseTime = System.nanoTime();
	}
	
	// Återupptar timern till samma stadie som den var innan pausen.
	public void resume(){
		lastTime += (System.nanoTime() - pauseTime);
	}
	
	// Stannar timern.
	public void stop(){
		started = false;
	}
	
	public long getDelay(){
		return delay;
	}
	
	// Ändrar delayen
	public void setDelay(long delay){
		this.delay = delay;
		if(repeats)
			start();
		else
			stop();
	}
}
