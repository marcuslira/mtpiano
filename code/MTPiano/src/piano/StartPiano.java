package piano;

import org.mt4j.MTApplication;

public class StartPiano extends MTApplication {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		initialize();
	}
	
	@Override
	public void startUp() {
		addScene(new PianoScene(this, "Scene 1"));
	}

}
