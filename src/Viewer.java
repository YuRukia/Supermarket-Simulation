//Connor Dawkins | 16/10/2020
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Viewer extends Thread
{
	Scanner readLine = new Scanner(System.in);
	
	//Toggles
	private boolean shoppingToggle;
	private boolean entersQueueToggle;
	private boolean entersCheckoutToggle;
	private boolean leavesCheckoutToggle;
	private boolean queueToggle;
	private boolean checkoutToggle;
	
	//Output Queue, stores requests to perform functions
	private ConcurrentLinkedQueue<String> outputQueue = new ConcurrentLinkedQueue<String>();
	private boolean running;
	private boolean lastMsg = false;
	
	public Viewer(boolean sT, boolean eQT, boolean eCT, boolean lCT, boolean qT, boolean cT) 
	{
		this.shoppingToggle = sT;
		this.entersQueueToggle = eQT;
		this.entersCheckoutToggle = eCT;
		this.leavesCheckoutToggle = lCT;
		this.queueToggle = qT;
		this.checkoutToggle = cT;
	}
	
	@Override
	public void run() 
	{
		running = true;
		while(running) 
		{
			//If queue isn't empty, perform first task in the queue
			if(!outputQueue.isEmpty()) {printString();}
			if(lastMsg && outputQueue.isEmpty()) {running = false;}
		}
	}
	
	public void printDefaultSettings()
	{
		sendToOutputQueue("Default Values");
		sendToOutputQueue("One Minute: " + Model.controller.getDefaultMinute());
		sendToOutputQueue("One Hour: " +  Model.controller.getDefaultHour());
		sendToOutputQueue("One Day: " +  Model.controller.getDefaultDay());
		sendToOutputQueue("Checkout Time: " +  Model.controller.getDefaultCheckoutTime());
		sendToOutputQueue("Number Of Tills: " +  Model.controller.getDefaultTills());
		sendToOutputQueue("Queue Length: " +  Model.controller.getDefaultQueueLength());
		sendToOutputQueue("Shoppers Per Minute: " +  Model.controller.getDefaultShoppersPerMinute());
		sendToOutputQueue("Basket Size: " +  Model.controller.getDefaultBasketSize() + "\n");
		
		sendToOutputQueue("Output Default Options");
		sendToOutputQueue("Enters Queue: " +  Model.controller.getDefaultEQT());
		sendToOutputQueue("Enters Checkout: " +  Model.controller.getDefaultECT());
		sendToOutputQueue("Leaves Checkout: " +  Model.controller.getDefaultLCT());
		sendToOutputQueue("Open/Closed Queue Toggle: " +  Model.controller.getDefaultQT());
		sendToOutputQueue("Open/Closed Checkout Toggle: " +  Model.controller.getDefaultCT() + "\n");
		
		sendToOutputQueue("If you wish to enter custom values, edit the customValues.txt file provided.");
		sendToOutputQueue("Word of warning, it is entirely possible to break the code doing this.\n");
	}
	public String enterCustomValues() 
	{
		sendToOutputQueue("Enter Custom Values? Y/N");
		String input = readLine.nextLine();
		return input;
	}
	
	public void simulationStarted() {sendToOutputQueue("Simulation started");}
	
	public void pressAnyKey() 
	{
		sendToOutputQueue("Press Any Key");
        readLine.nextLine();
        sendToOutputQueue("Stop Simulation");
		readLine.close();
		lastMsg = true;
	}
	
	public void printString() {System.out.println(outputQueue.poll());}
	public void sendToOutput(String str) {Model.output.add(str);}
	public void sendToOutputQueue(String str) {sendToOutput(str); outputQueue.add(str);}
	
	public void addMainTime(int i) {sendToOutputQueue("Hour: " + i);}
	public void enterTimeSlice(int i) {sendToOutputQueue("Time Slice: " + i);}
	
	public void shopperStartsShopping(Shopper shopper) {if(shoppingToggle) {sendToOutputQueue("Shopper: " + shopper.getShopperID() + " Starts Shopping");}}
	public void shopperFinishesShopping(Shopper shopper) {if(shoppingToggle) {sendToOutputQueue("Shopper: " + shopper.getShopperID() + " Finishes Shopping With: " + shopper.getBasket().size() + " Items");}}
	
	public void shopperEntersQueue(Shopper shopper) {if(entersQueueToggle) {sendToOutputQueue("Shopper: " + shopper.getShopperID() + " Joins Queue: " + shopper.getTillID());}}
	public void shopperAtCheckout(int id) {if(entersCheckoutToggle) {sendToOutputQueue("Shopper: " + id + " Arrives At Checkout");}}
	
	public void shopperLeavesCheckout(Shopper shopper) 
	{
		if(leavesCheckoutToggle) 
		{
			LinkedList<Products> bag = shopper.getBag();
			String str = "";
			
			for(int i = 0; i < bag.size(); i++) 
			{
				if(i == bag.size() - 1) {str += bag.get(i).getProductName();}
				else {str += bag.get(i).getProductName() + ", ";}
			}
			sendToOutputQueue("Shopper: " + shopper.getShopperID() + " Leaves Checkout With: " + str);
		}
	}
	public void shopperWaitedTooLong(Shopper shopper) {sendToOutputQueue("Shopper: " + shopper.getShopperID() + " Waited Too Long");}
	
	public void queueOpens(int id) {if(queueToggle) {sendToOutputQueue("Queue: " + id + " Opens At: " + Model.time);}}
	public void queueCloses(int id) {if(queueToggle) {sendToOutputQueue("Queue: " + id + " Closes At: " + Model.time);}}
	
	public void checkoutOpens(int id) {if(checkoutToggle) {sendToOutputQueue("Checkout: " + id + " Opens At: " + Model.time);}}
	public void checkoutCloses(int id) {if(checkoutToggle) {sendToOutputQueue("Checkout: " + id + " Closes At: " + Model.time);}}
	
	public void endOutput() 
	{
		sendToOutputQueue("\nCustomers In Store: " + Model.customersInStore);
        for(int i = 0; i < Model.tills.size(); i++) 
        {
        	sendToOutputQueue("People In Queue: " + Model.tills.get(i).getQueueSize());
        	sendToOutputQueue("People Served By Till " + Model.tills.indexOf(Model.tills.get(i)) + ": " + Model.tills.get(i).getCustomersServed());
        }
	}
	
	public void debugOutput(int times, int times2, int shLeft, int totalCustomers) 
	{
		sendToOutputQueue("\n" + "Shoppers Shopped: " + times);
		sendToOutputQueue("Shoppers Shopped More Than Once: " + times2);
		sendToOutputQueue("Shoppers Left: " + shLeft);
		sendToOutputQueue("Total Customers: " + totalCustomers);
	}
	
	public void setST(boolean bool) {this.shoppingToggle = bool;}
	public void setEQT(Boolean bool) {this.entersQueueToggle = bool;}
	public void setECT(Boolean bool) {this.entersCheckoutToggle = bool;}
	public void setLCT(Boolean bool) {this.leavesCheckoutToggle = bool;}
	public void setQT(Boolean bool) {this.queueToggle = bool;}
	public void setCT(Boolean bool) {this.checkoutToggle = bool;}
}