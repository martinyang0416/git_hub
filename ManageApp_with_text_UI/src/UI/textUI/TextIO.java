package UI.textUI;

import java.io.*;

/**
 * Define the Text IO to print the a Screen.
 * (i.e. all Screen's interaction should comes from here)
 */
public class TextIO implements Closeable{
	
	private final BufferedWriter output;
	private final BufferedReader input;
	
	public TextIO(){
		// if the console is null, we use stdout and stdin
		if(System.console() != null){
			output = null;
			input = null;
		}else{
			output = new BufferedWriter(new OutputStreamWriter(System.out));
			input = new BufferedReader(new InputStreamReader(System.in));
		}
	}
	
	/**
	 * Print a format string. (no newline)
	 */
	public void printf(String format, Object... objs){
		if(System.console() != null){
			System.console().printf(format, objs);
		}else{
			try{
				output.write(String.format(format, objs));
				output.flush();
			}catch(IOException e){
				throw new IOError(e);
			}
		}
	}
	
	/**
	 * Print a String, with a new line character appended.
	 */
	public void printLine(String s){
		if(System.console() != null){
			System.console().printf("%s\n", s);
		}else{
			try{
				output.write(s);
				output.newLine();
				output.flush();
			}catch(IOException e){
				throw new IOError(e);
			}
		}
	}
	
	/**
	 * Print a format String, with a new line character append.
	 */
	public void printLine(String format, Object... objs){
		if(System.console() != null){
			System.console().printf(format + "\n", objs);
		}else{
			try{
				output.write(String.format(format, objs));
				output.newLine();
				output.flush();
			}catch(IOException e){
				throw new IOError(e);
			}
		}
	}
	
	/**
	 * Print all lines from the iterable, one by one, and each iteration
	 * with a new line appended.
	 */
	public void printLines(Iterable<String> lines){
		if(System.console() != null){
			for(String line : lines)
				System.console().printf("%s\n", line);
		}else{
			try{
				for(String line : lines){
					output.write(line);
					output.newLine();
				}
				output.flush();
			}catch(IOException e){
				throw new IOError(e);
			}
		}
	}
	
	/**
	 * Print a divider consisting 60 "="
	 */
	public void printDivider1(){
		printLine("============================================================");
	}
	
	/**
	 * Print a divider consisting 60 "-"
	 */
	public void printDivider2(){
		printLine("------------------------------------------------------------");
	}
	
	/**
	 * Print a divider consisting 60 "~"
	 */
	public void printDivider3(){
		printLine("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}
	
	/**
	 * Clear the screen by printing 15 new line character.
	 */
	public void clearScreen(){
		clearScreen(15);
	}
	
	public void clearScreen(int scale){
		if(System.console() != null){
			for(int i = 0; i < scale; i++)
				System.console().printf("\n");
		}else{
			try{
				for(int i = 0; i < scale; i++)
					output.newLine();
				output.flush();
			}catch(IOException e){
				throw new IOError(e);
			}
		}
	}
	
	/**
	 * Read a line.
	 */
	public String readLine(){
		if(System.console() != null){
			return System.console().readLine();
		}else{
			try{
				return input.readLine();
			}catch(IOException e){
				throw new IOError(e);
			}
		}
	}
	
	/**
	 * Read a line with a prompt.
	 */
	public String readLineWithPrompt(String format, Object... objs){
		if(System.console() != null){
			return System.console().readLine(format, objs);
		}else{
			try{
				output.write(String.format(format, objs));
				output.flush();
				return input.readLine();
			}catch(IOException e){
				throw new IOError(e);
			}
		}
	}
	
	/**
	 * Read a line of password (may or may not hide echoing).
	 */
	public char[] readPassword(){
		if(System.console() != null){
			return System.console().readPassword();
		}else{
			try{
				return input.readLine().toCharArray();
			}catch(IOException e){
				throw new IOError(e);
			}
		}
	}
	
	/**
	 * Read a line of password with a prompt (may or may not hide echoing).
	 */
	public char[] readPasswordWithPrompt(String format, Object... objs){
		if(System.console() != null){
			return System.console().readPassword(format, objs);
		}else{
			try{
				output.write(String.format(format, objs));
				output.flush();
				return input.readLine().toCharArray();
			}catch(IOException e){
				throw new IOError(e);
			}
		}
	}
	
	@Override
	public void close() throws IOException{
		if(output != null) output.close();
		if(input != null) input.close();
	}
}
