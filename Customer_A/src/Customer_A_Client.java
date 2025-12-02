import java.io.*;
import java.net.*;

public class Customer_A_Client {
	//private static String ActionClientID = "Customer_A_Client";
    //private static int ActionSocketNumber = 4547;
    //private static String ActionServerName = "localhost";
	
    public static void main(String[] args) throws IOException {

        Socket ClientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        int socketNumber = 4548;
        String warehouseServerName = "localhost";
        String clientID = "Customer_A_Client";

        try {
            ClientSocket = new Socket(warehouseServerName, socketNumber);
            out = new PrintWriter(ClientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: localhost ");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: "+ socketNumber);
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;

        System.out.println("Initialised " + clientID + " client and IO connections");

        while (true) {
            
            fromUser = stdIn.readLine();
            
            if (fromUser != null) {
            	if (fromUser.equals("Check_stock")) {
            		fromUser+=" 0";
            	}
            	fromUser = "Customer "+fromUser;
            	
                System.out.println(clientID + " sending " + fromUser + " to WarehouseServer");
                out.println(fromUser);
            }
            fromServer = in.readLine();
            System.out.println(clientID + " received " + fromServer + " from WarehouseServer");
        }
            
    }
    public static int[] Check_stock() {
    	int[] stock = new int[2];
    	return stock;
    }
    public static void Buy_apples(int buyNumber) {
    	
    }
    public static void Buy_oranges(int buyNumber) {
    	
    }
    public static void request() {
    	
    }
}
