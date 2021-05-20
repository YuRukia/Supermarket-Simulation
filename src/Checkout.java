//Connor Dawkins | 16/10/2020

public class Checkout extends Thread
{
	private int tillID;
	private Shopper shopper;
	private boolean running;
	private int checkoutTime = Model.checkoutTime;
	private int customersServed = 0;
	private boolean tillOpen = false;
	
	public Checkout(int tID) {this.tillID = tID;}

	@Override
	public void run() 
	{
		running = true;
		while (running) 
		{
			//Is Cashier at Till?
			if(Model.tills.get(tillID).getCashierAtTill())
			{
				//If Checkout was closed and is now open, tell viewer to display message
				if(!tillOpen) {tillOpen = true; Model.viewer.checkoutOpens(tillID);}
				if(Model.tills.get(tillID).getQueueSize() > 0) 
				{
					//Remove the first shopper from the queue and assign them to this Shopper variable
					shopper = Model.tills.get(tillID).moveToCheckout();
					//Set the shopper to leave the store before they are set to leave the queue
					//This prevents any possibility of the shopper being added to another queue before they can leave the store
					Model.shoppers.get(shopper.getShopperID()).setInStore(false);
					Model.shoppers.get(shopper.getShopperID()).setInQueue(false);
					//Add a customer served at this till and remove one shopper from the total number in the store
					customersServed++;
					Model.customersInStore--;
					Model.viewer.shopperAtCheckout(shopper.getShopperID());
					//Simulate the length of time it takes to go through checkout
					try {Thread.sleep(checkoutTime);}
			        catch (InterruptedException e) {e.printStackTrace();}
					Model.viewer.shopperLeavesCheckout(shopper);
				}
			}
			//If Checkout was open and is now closed, tell viewer to display message
			if(!Model.tills.get(tillID).getCashierAtTill() && tillOpen) {tillOpen = false; Model.viewer.checkoutCloses(tillID);}
			if(Model.time >= 22) {running = false; tillOpen = false;}
		}
		Model.tills.get(tillID).addCustomersServed(customersServed);
	}
}