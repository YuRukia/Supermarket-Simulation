//Connor Dawkins | 16/10/2020
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.AbstractMap;

public class Controller extends Thread
{
	//Default Values
	private int defaultMinute = 1;
	private int defaultHour = defaultMinute * 60;
	private int defaultDay = defaultHour * 23;
	private int defaultCheckoutTime = defaultMinute * 3;
	private int defaultTills = 5;
	private int defaultQueueLength = 2;
	private int defaultShoppersPerMinute = 4;
	private int defaultBasketSize = 10;
	
	//Default Toggles
	private boolean defaultShopperToggle = true;
	private boolean defaultEntersQueueToggle = true;
	private boolean defaultEntersCheckoutToggle = true;
	private boolean defaultLeavesCheckoutToggle = true;
	private boolean defaultQueueToggle = true;
	private boolean defaultCheckoutToggle = true;
	
	//Function Operation Queue, stores requests to perform functions
	private ConcurrentLinkedQueue<AbstractMap.SimpleEntry<String, Integer>> operationQueue = new ConcurrentLinkedQueue<AbstractMap.SimpleEntry<String, Integer>>();
	boolean running;
	
	@Override
	public void run() 
	{
		running = true;
		while (running) 
		{
			//If operationQueue isn't empty, look at what operation it is, then perform the requested operation
			if(!operationQueue.isEmpty())
			{
				switch(operationQueue.peek().getKey())
				{
					case "setOneMinute":
						setOneMinute(operationQueue.poll().getValue());
						break;
					case "setOneHour":
						setOneHour(operationQueue.poll().getValue());
						break;
					case "setDay":
						setDay(operationQueue.poll().getValue());
						break;
					case "setCheckoutTime":
						setCheckoutTime(operationQueue.poll().getValue());
						break;
					case "setTillNum":
						setTillNum(operationQueue.poll().getValue());
						break;
					case "setQueueLength":
						setQueueLength(operationQueue.poll().getValue());
						break;
					case "setShoppersPerMinute":
						setShoppersPerMinute(operationQueue.poll().getValue());
						break;
					case "setBasketSize":
						setBasketSize(operationQueue.poll().getValue());
						break;
					case "setST":
						if(operationQueue.poll().getValue() == 0) {setST(false);}
						else {setST(true);}
						break;
					case "setEQT":
						if(operationQueue.poll().getValue() == 0) {setEQT(false);}
						else {setEQT(true);}
						break;
					case "setECT":
						if(operationQueue.poll().getValue() == 0) {setECT(false);}
						else {setECT(true);}
						break;
					case "setLCT":
						if(operationQueue.poll().getValue() == 0) {setLCT(false);}
						else {setLCT(true);}
						break;
					case "setQT":
						if(operationQueue.poll().getValue() == 0) {setQT(false);}
						else {setQT(true);}
						break;
					case "setCT":
						if(operationQueue.poll().getValue() == 0) {setCT(false);}
						else {setCT(true);}
						break;
				}
			}
			if(operationQueue.isEmpty() && Model.time >= 23) {running = false;}
		}
	}
	
	//Add request to operationQueue
	public void addToOperationQueue(String str, int i) {operationQueue.add(new AbstractMap.SimpleEntry<String, Integer>(str, i));}
	
	public void setDefaultSettings() 
	{
		//MVC
		Model.viewer = new Viewer(getDefaultST(), getDefaultEQT(), getDefaultECT(), getDefaultLCT(), getDefaultQT(), getDefaultCT());
		
		//Initialise Global Variables
		Model.customersInStore = 0;
		Model.totalCustomers = 0;
		Model.tills = Collections.synchronizedList(new ArrayList<Till>());
		Model.cashiers = Collections.synchronizedList(new ArrayList<Cashier>());
		Model.shoppers = Collections.synchronizedList(new ArrayList<Shopper>());
		Model.products = Collections.synchronizedList(new ArrayList<Products>());
		Model.types = new LinkedList<String>();
		Model.subTypes = new LinkedList<String>();
		Model.output = Collections.synchronizedList(new ArrayList<String>());
		
		//Initialise Global Times
		Model.time = 0;
		Model.oneMinute = getDefaultMinute();
		Model.oneHour = getDefaultHour();
		Model.oneDay = getDefaultDay();
		Model.checkoutTime = getDefaultCheckoutTime();
		
		//Set number of tills to open and how long the queue is
		Model.tillNum = getDefaultTills();
		Model.queueLength = getDefaultQueueLength();
		
		//How many shoppers enter the store every minute
		Model.shoppersPerMinute = getDefaultShoppersPerMinute();
		
		//Set the number of products the shopper can carry
		Model.basketSize = getDefaultBasketSize();
	}
	
	public int getDefaultMinute() {return defaultMinute;}
	public void setOneMinute(int i) {Model.oneMinute = i;}
	
	public int getDefaultHour() {return defaultHour;}
	public void setOneHour(int i) {Model.oneHour = i;}
	
	public int getDefaultDay() {return defaultDay;}
	public void setDay(int i) {Model.oneDay = i;}
	
	public int getDefaultCheckoutTime() {return defaultCheckoutTime;}
	public void setCheckoutTime(int i) {Model.checkoutTime = i;}
	
	public int getDefaultTills() {return defaultTills;}
	public void setTillNum(int i) {Model.tillNum = i;}
	
	public int getDefaultQueueLength() {return defaultQueueLength;}
	public void setQueueLength(int i) {Model.queueLength = i;}
	
	public int getDefaultShoppersPerMinute() {return defaultShoppersPerMinute;}
	public void setShoppersPerMinute(int i) {Model.shoppersPerMinute = i;}
	
	public int getDefaultBasketSize() {return defaultBasketSize;}
	public void setBasketSize(int i) {Model.basketSize = i;}
	
	public boolean getDefaultST() {return defaultShopperToggle;}
	public void setST(Boolean bool) {Model.viewer.setST(bool);}
	
	public boolean getDefaultEQT() {return defaultEntersQueueToggle;}
	public void setEQT(Boolean bool) {Model.viewer.setEQT(bool);}
	
	public boolean getDefaultECT() {return defaultEntersCheckoutToggle;}
	public void setECT(Boolean bool) {Model.viewer.setECT(bool);}

	public boolean getDefaultLCT() {return defaultLeavesCheckoutToggle;}
	public void setLCT(Boolean bool) {Model.viewer.setLCT(bool);}

	public boolean getDefaultQT() {return defaultQueueToggle;}
	public void setQT(Boolean bool) {Model.viewer.setQT(bool);}

	public boolean getDefaultCT() {return defaultCheckoutToggle;}
	public void setCT(Boolean bool) {Model.viewer.setCT(bool);}
}
