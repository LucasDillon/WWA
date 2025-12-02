
import java.net.*;
import java.io.*;


public class WarehouseServerThread extends Thread {

	
  private Socket actionSocket = null;
  private SharedActionState mySharedActionStateObject;
  private String myWarehouseServerThreadName;
   
  //Setup the thread
  	public WarehouseServerThread(Socket actionSocket, String WarehosueServerThreadName, SharedActionState SharedObject) {
	
//	  super(ActionServerThreadName);
	  this.actionSocket = actionSocket;
	  mySharedActionStateObject = SharedObject;
	  myWarehouseServerThreadName = WarehosueServerThreadName;
	}

  public void run() {
    try {
      System.out.println(myWarehouseServerThreadName + "initialising.");
      PrintWriter out = new PrintWriter(actionSocket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(new InputStreamReader(actionSocket.getInputStream()));
      String inputLine, outputLine;

      while ((inputLine = in.readLine()) != null) {
    	  // Get a lock first
    	  try { 
    		  mySharedActionStateObject.acquireLock();  
    		  outputLine = mySharedActionStateObject.processInput(myWarehouseServerThreadName, inputLine);
    		  out.println(outputLine);
    		  mySharedActionStateObject.releaseLock();  
    	  } 
    	  catch(InterruptedException e) {
    		  System.err.println("Failed to get lock when reading:"+e);
    	  }
      }

       out.close();
       in.close();
       actionSocket.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}