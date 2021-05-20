//Connor Dawkins | 16/10/2020

public class Till extends Thread
{
	//Operating Variables
	private int tillID;
	private int queueLength;
	private boolean cashierAtTill = false;
	private Queue queue;
	private Checkout checkout;
	private int customersServed = 0;
	private boolean running;
	
	public Till(int tID, int qL) {this.tillID = tID; this.queueLength = qL;}
	
	@Override
	public void run() 
	{
		queue = new Queue(tillID, queueLength);
		queue.start();
		
		checkout = new Checkout(tillID);
		checkout.start();

		int currentTime = 0;
		int lastTime = 0;
		running = true;
		//Update the till if there is a Cashier there or not
		while (running) 
		{
			currentTime = Model.time;
			//If the hour has changed since last checked, see if cashier is at till or not
			//Efficiency measure to prevent thread from spamming requests at the `cashiers` list
			if(currentTime != lastTime) {setCashierAtTill(Model.cashiers.get(tillID).getWorking());}
			if(Model.time >= 22) {running = false;}
			try {Thread.sleep(1);}
	        catch (InterruptedException e) {e.printStackTrace();}
			lastTime = Model.time;
		}
	}
	
	public int getQueueSize() {return queue.getQueueSize();}
	public Shopper moveToCheckout() {return queue.moveToCheckout();}
	
	public boolean getCashierAtTill() {return this.cashierAtTill;}
	public void setCashierAtTill(boolean atTill) {this.cashierAtTill = atTill;}
	public int getCustomersServed() {return this.customersServed;}
	public void addCustomersServed(int i) {customersServed = i;}
}