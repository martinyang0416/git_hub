package UI.textUI;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * A Screen Executor execute a queue of Screen one by one.
 *
 * A screen is rendered until it indicates it doesn't have next.
 */
public class ScreenExecutor{
	
	private final Queue<Screen> screenQueue;
	
	public ScreenExecutor(){
		screenQueue = new ArrayDeque<>();
	}
	
	public ScreenExecutor(Screen screen){
		this();
		pushScreen(screen);
	}
	
	/**
	 * Run the screen one by one.
	 */
	public void run(TextIO io){
		while(! screenQueue.isEmpty()){
			executeScreen(screenQueue.poll(), io);
		}
	}
	
	private void executeScreen(Screen screen, TextIO io){
		while(screen.hasNextScreen()){
			// clear the screen
			io.clearScreen();
			screen.render(io);
		}
	}
	
	/**
	 * Push a Screen.
	 */
	public void pushScreen(Screen screen){
		screenQueue.add(screen);
	}
}
