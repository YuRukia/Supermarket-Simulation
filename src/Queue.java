//Connor Dawkins | 16/10/2020
import java.util.concurrent.ConcurrentLinkedQueue;

public class Queue extends Thread
{
	private int tillID;
	private int queueLength;
	boolean running;
	private ConcurrentLinkedQueue<Shopper> tillQueue = new ConcurrentLinkedQueue<Shopper>();
	private boolean queueOpen = false;
	
	public Queue(int tID, int qL) {this.tillID = tID; this.queueLength = qL;}
	
	@Override
	public void run()
	{
		running = true;
		while(running)
		{
			//Is Cashier at Till?
			if(Model.tills.get(tillID).getCashierAtTill())
			{
				//If Queue was closed and is now open, tell viewer to display message
				if(!queueOpen) {queueOpen = true; Model.viewer.queueOpens(tillID);}
				if(tillQueue.size() < queueLength)
				{
					//Search through all of the shoppers and pick the first one that is valid: inQueue = false && inStore = true
					for(int i = 0; i < Model.shoppers.size(); i++) 
					{
						if(!Model.shoppers.get(i).getInQueue(tillID))
						{
							//Create duplicate shopper object to insert into tillQueue
							//This is done for efficiency, it means that neither the Queue nor the Checkout have to access the `Shopper` list unless editing the original shopper
							//Speeds up the program significantly
							Shopper shopper = Model.shoppers.get(i);
							tillQueue.offer(shopper);
							Model.viewer.shopperEntersQueue(shopper);
							break;
						}
					}
				}
			}
			//If Queue was open and is now closed, tell viewer to display message
			if(!Model.tills.get(tillID).getCashierAtTill() && queueOpen) {queueOpen = false; Model.viewer.queueCloses(tillID);}
			if(Model.time >= 22) {running = false;}
		}
	}
	
	public int getQueueSize() {return tillQueue.size();}
	public Shopper moveToCheckout() {return tillQueue.poll();}
}