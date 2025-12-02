import java.net.*;
import java.io.*;





public class WarehouseServer {
  public static void main(String[] args) throws IOException {

	ServerSocket WarehouseServerSocket = null;
    boolean listening = true;
    String warehouseServerName = "WarehouseServer";
    int warehouseServerNumber = 4548;
    
    int sharedAppleStock = 1000;
    int sharedOrangeStock = 1000;

    //Create the shared object in the global scope...
    
    SharedActionState ourSharedActionStateObject = new SharedActionState(sharedAppleStock,sharedOrangeStock);
        
    // Make the server socket

    try {
      WarehouseServerSocket = new ServerSocket(warehouseServerNumber);
    } catch (IOException e) {
      System.err.println("Could not start " + warehouseServerName + " specified port.");
      System.exit(-1);
    }
    System.out.println(warehouseServerName + " started");

    //Got to do this in the correct order with only four clients!  Can automate this...
    
    while (listening){
      new WarehouseServerThread(WarehouseServerSocket.accept(), "WarehouseServerThread1", ourSharedActionStateObject).start();
      new WarehouseServerThread(WarehouseServerSocket.accept(), "WarehouseServerThread2", ourSharedActionStateObject).start();
      new WarehouseServerThread(WarehouseServerSocket.accept(), "WarehouseServerThread3", ourSharedActionStateObject).start();
      new WarehouseServerThread(WarehouseServerSocket.accept(), "WarehouseServerThread4", ourSharedActionStateObject).start();
      System.out.println("New " + warehouseServerName + " thread started.");
    }
    WarehouseServerSocket.close();
  }
}