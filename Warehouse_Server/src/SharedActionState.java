import java.net.*;
import java.io.*;

public class SharedActionState {

	private SharedActionState mySharedObj;
	private String myThreadName;
	private int mySharedAppleStock;
	private int mySharedOrangeStock;
	private boolean accessing = false; // true a thread has a lock, false otherwise
	private int threadsWaiting = 0; // number of waiting writers

// Constructor	

	SharedActionState(int sharedAppleStock, int sharedOrangeStock) {
		mySharedAppleStock = sharedAppleStock;
		mySharedOrangeStock = sharedOrangeStock;
	}

//Attempt to aquire a lock

	public synchronized void acquireLock() throws InterruptedException {
		Thread me = Thread.currentThread(); // get a ref to the current thread
		System.out.println(me.getName() + " is attempting to acquire a lock!");
		++threadsWaiting;
		while (accessing) { // while someone else is accessing or threadsWaiting > 0
			System.out.println(me.getName() + " waiting to get a lock as someone else is accessing...");
			// wait for the lock to be released - see releaseLock() below
			wait();
		}
		// nobody has got a lock so get one
		--threadsWaiting;
		accessing = true;
		System.out.println(me.getName() + " got a lock!");
	}

	// Releases a lock to when a thread is finished

	public synchronized void releaseLock() {
		// release the lock and tell everyone
		accessing = false;
		notifyAll();
		Thread me = Thread.currentThread(); // get a ref to the current thread
		System.out.println(me.getName() + " released a lock!");
	}

	/* The processInput method */

	public synchronized String processInput(String myThreadName, String theInput) {
		System.out.println(myThreadName + " received " + theInput);
		String theOutput = null;

		theInput.strip();
		// String inputCommand = theInput.substring(0, theInput.indexOf(" "));
		// int inputParameter=Integer.parseInt(theInput.substring(theInput.indexOf("
		// ")+1));
		String clientType;
		String inputCommand;
		int inputParameter;

		try {
			clientType = theInput.substring(0, theInput.indexOf(" "));
			inputCommand = theInput.substring(theInput.indexOf(" ")+1, theInput.indexOf(" ",theInput.indexOf(" ")+1));
			inputParameter = Integer.parseInt(theInput.substring(theInput.indexOf(" ",theInput.indexOf(" ")+1) + 1));
		} catch (Exception e) {
			theOutput = "Received unknown message: " + theInput;
			System.out.println(theOutput);
			return theOutput;
		}

		if (inputCommand.equals("Buy_apples") && clientType.equals("Customer")) {
			if (mySharedAppleStock-inputParameter>=0) {
				mySharedAppleStock -= inputParameter;
				theOutput = "Purchased " + inputParameter + " apples. There are now " + mySharedAppleStock + " apples in stock";
			}
			else {
				theOutput = "Unable to purchase " + inputParameter + " apples as there are only " + mySharedAppleStock + " apples in stock";
			}
		} 
		else if (inputCommand.equals("Buy_oranges") && clientType.equals("Customer")) {
			if (mySharedOrangeStock-inputParameter>=0) {
				mySharedOrangeStock -= inputParameter;
				theOutput = "Purchased " + inputParameter + " oranges. There are now " + mySharedOrangeStock + " oranges in stock";
			}
			else {
				theOutput = "Unable to purchase " + inputParameter + " oranges as there are only " + mySharedOrangeStock + " oranges in stock";
			}
		}
		else if (inputCommand.equals("Add_apples") && clientType.equals("Supplier")) {
			mySharedAppleStock += inputParameter;
			theOutput = "Added " + inputParameter + " apples. There are now " + mySharedAppleStock + " apples in stock";
		} 
		else if (inputCommand.equals("Add_oranges") && clientType.equals("Supplier")) {
			mySharedOrangeStock += inputParameter;
			theOutput = "Added " + inputParameter + " oranges. There are now " + mySharedOrangeStock + " oranges in stock";
		} 
		else if (inputCommand.equals("Check_stock") && ((clientType.equals("Customer") || clientType.equals("Supplier")))) {
			theOutput = "There are " + mySharedAppleStock + " apples and " + mySharedOrangeStock + " oranges in stock";
		} 
		else {
			theOutput = inputCommand + " is an invalid command for a " + clientType + " client";
		}

		// Return the output message to the WarehouseServer
		System.out.println(theOutput);
		return theOutput;
	}
}
