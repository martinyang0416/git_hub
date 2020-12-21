package gateway;

import controller.Session;

import java.io.*;

/**
 * Session IO serialize/deSerialize a Session object.
 */
public class SessionIO{
	
	/**
	 * Restore the Session, return null if failed.
	 */
	public Session restoreSession(String path) throws ClassNotFoundException{
		try{
			InputStream file = new FileInputStream(path);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);
			
			// deserialize the session
			Session session = (Session) input.readObject();
			input.close();
			return session;
		}catch(IOException e){
			// we failed to load!
			System.out.println("Session Restored UnSuccessful with the following Exception Message : ");
			System.out.println("\t" + e.getLocalizedMessage());
			return null;
		}
	}
	
	/**
	 * Save the Session, return false if failed.
	 */
	public boolean saveSession(String path, Session session){
		try{
			OutputStream file = new FileOutputStream(path);
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);
			
			// serialize the session
			output.writeObject(session);
			output.close();
			return true;
		}catch(IOException e){
			System.out.println(e.getLocalizedMessage());
			return false;
		}
	}
}
