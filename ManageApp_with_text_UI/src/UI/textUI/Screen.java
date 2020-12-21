package UI.textUI;

/**
 * A Screen contains a procedure to render the screen also
 * a method indicates is there a next screen.
 */
public interface Screen{
	
	void render(TextIO io);
	
	boolean hasNextScreen();
}
