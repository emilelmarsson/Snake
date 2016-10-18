
// En klass som beskriver rutor.
public class Tile {
	// Storleken på en ruta. (De är kvadratiska.)
	public static final int SIZE = 32;
	
	// Det som rutan kommer innehålla. Eftersom vi inte vet exakt vad rutan ska innehålla behöver vi en klass som antingen kan vara
	// till exempel ett äpple eller en del av ormens kropp. Det är där Element kommer in. (Det är ett så kallat interface.)
	private Element content;
	
	public Tile(){ 
		// By default är en ruta tom.
		content = null;
	}
	
	public void setContent(Element content){
		if(content != null)
			this.content = content;
	}
	
	public void render(){
		content.render();
	}
}
