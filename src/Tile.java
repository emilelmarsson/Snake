
// En klass som beskriver rutor.
public class Tile {
	// Bredden och höjden på en ruta (i pixlar).
	private int width, height;
	
	// Det som rutan kommer innehålla. Eftersom vi inte vet exakt vad rutan ska innehålla behöver vi en klass som antingen kan vara
	// till exempel ett äpple eller en del av ormens kropp. Det är där Element kommer in. (Det är ett så kallat interface.)
	private Element content;
	
	public Tile(){ 
		// Sätter bredden och höjden på rutan. Vi får leka runt lite med detta tills vi hittar det rätta.
		width = 30;
		height = 30;
		// By default är en ruta tom.
		content = null;
	}
}
