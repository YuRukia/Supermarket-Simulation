//Connor Dawkins | 16/10/2020
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class Shopper extends Thread
{
	private int ID;
	private int basketSize;
	private int tillID = -1;
	private int timesRun = 0;
	private int shopperLeft = 0;
	private AtomicBoolean inQueue = new AtomicBoolean(false);
	private AtomicBoolean inStore = new AtomicBoolean(false);
	private String allTypes;
	private LinkedList<Products> shoppingList = new LinkedList<Products>();
	private LinkedList<Products> basket = new LinkedList<Products>();
	private LinkedList<Products> bag = new LinkedList<Products>();
	
	public Shopper(int id, int bskSize)
	{
		this.ID = id;
		this.basketSize = bskSize;
	}
	
	public void run() 
	{	
		//How long the Shopper is willing to wait to enter a queue
		int patience = ThreadLocalRandom.current().nextInt(5, 15 + 1);
		
		//Create local shopping class to handle this shoppers needs
		//Speeds up simulation by not making threads have to wait on other threads to finish with `Shopping`
		Shopping shopping = new Shopping(basketSize);
		
		//Generate list of products this shopper is allowed to pick from
		allTypes = shopping.generateTypes();
		
		//Generate shopping list of items the shopper wants to have from the allowed list
		shoppingList = shopping.generateShoppingList();
		
		//Shop for the items they have on the shopping list
		basket = shopping.shop(shoppingList, this);
		Model.viewer.shopperFinishesShopping(this);
		inStore.getAndSet(true);
		
		//Sleep for the length of time the shopper is willing to wait to enter a queue
		try {Thread.sleep(patience);}
        catch (InterruptedException e) {e.printStackTrace();}
		
		//If not in a queue after their time limit has ended, leave the store without purchasing anything
		if(tillID == -1 && inStore.get()) {Model.shoppers.get(ID).leaveStore();}
	}
		
	public int getShopperID() {return this.ID;}
	public void setShopperID(int id) {this.ID = id;}
	
	public int getBasketSize() {return this.basketSize;}
	public void setBasketSize(int size) {this.basketSize = size;}
	
	public int getTimesRun() {return timesRun;}
	public int getShopperLeft() {return shopperLeft;}
	
	public LinkedList<Products> getBasket(){return basket;}
	public void setBasket(Products product) {this.basket.add(product);}
	
	public synchronized int getTillID() {return tillID;}
	public synchronized void setTillID(int tillid) {tillID = tillid;}
	
	//Check if shopper is valid for queue and assign them to the queue
	//Methods combined into one to avoid Race conditions, doing it in one method like this removes any possibility of Race conditions
	//Such as one shopper being assigned to multiple tills or a shopper being put back into a queue before their `inStore` can be set to false when leaving the checkout
	public synchronized boolean getInQueue(int tID)
	{
		if(!inQueue.get() && inStore.get())
		{
			setInQueue(true); 
			this.tillID = tID;
			return false;
		}
		else {return true;}
	}
	
	public synchronized void setInQueue(boolean bool) {inQueue.getAndSet(bool);}
	//Methods combined to prevent race conditions
	public synchronized void setInStore(boolean bool) 
	{
		inStore.getAndSet(bool);
		timesRun++;
		setBag();
	}
	
	public synchronized void leaveStore() 
	{
		inStore.getAndSet(false);
		basket.clear();
		bag.clear();
		Model.viewer.shopperWaitedTooLong(this);
		Model.customersInStore--;
		shopperLeft++;
	}
	
	//Remove all of the items from the basket and add them to the shoppers bag
	public synchronized void setBag() { while(!basket.isEmpty()) {bag.add(basket.poll());} basket.clear();}
	public int getBagSize() {return bag.size();}
	public LinkedList<Products> getBag() {return bag;}
	
	public void printBasket() {for(int i = 0; i < bag.size(); i++) {System.out.println("Shopper: " + ID + " Purchased: " + bag.get(i).getProductName());}}
	public void printTypes() {System.out.println(allTypes);}
}