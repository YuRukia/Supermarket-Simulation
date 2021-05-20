//Connor Dawkins | 16/10/2020
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Shopping extends Thread
{
	private int randSize;
	private int basketSize;
	private int maxTime = Model.oneMinute * 5;
	private int minTime = Model.oneMinute * 2;
	private LinkedList<Products> allowedProducts = new LinkedList<Products>();
	private List<Products> products = new LinkedList<Products>();
	Random shoppingChoice = new Random();

	public Shopping(int bskSize) 
	{
		this.basketSize = shoppingChoice.nextInt(bskSize + 1);
		this.products = Model.products;
	}
	
	//Generate a list of Types and SubTypes for this shopper
	public String generateTypes()
	{
		Set<String> tempType = new HashSet<String>();
		Set<String> tempSubType = new HashSet<String>();
		
		//Randomly select a random number of Types
		for(int i = 0; i < shoppingChoice.nextInt(Model.types.size()); i++) {tempType.add(Model.types.get(shoppingChoice.nextInt(Model.types.size())));}
		
		//Randomly select a random number of SubTypes
		for(int i = 0; i < shoppingChoice.nextInt(Model.subTypes.size()); i++) 
		{
			for(int x = 0; x < products.size(); x++) 
			{
				//Check to see if the selected SubType belongs to one of the selected Types
				if(tempType.contains(products.get(x).getType())) 
				{
					tempSubType.add(products.get(x).getSubType());
				}
			}
		}
		
		//Generate list of products that the shopper is allowed to pick based on Type + SubType
		for(int i = 0; i < products.size(); i++) 
		{
			if(tempType.contains(products.get(i).getType()))
			{
				if(tempSubType.contains(products.get(i).getSubType())) 
				{
					allowedProducts.add(products.get(i));
				}
			}
		}
		randSize = allowedProducts.size();
		String allTypes = tempType.toString() + " | " + tempSubType.toString();
		return allTypes;
	}
	
	//Generate a list of items that the shopper wants to buy from the list of allowed items
	public LinkedList<Products> generateShoppingList()
	{
		LinkedList<Products> shoppingList = new LinkedList<Products>();
		if(basketSize > 0) 
		{
			if(!allowedProducts.isEmpty()) 
			{
				int rand;
				double randWeight = shoppingChoice.nextDouble();
				
				//10% chance to buy nothing
				if(randWeight > 0.1) {rand = 1 + shoppingChoice.nextInt(basketSize + 1);
				if (rand > basketSize) {rand--;}}
				else {rand = shoppingChoice.nextInt(basketSize + 1);}
				//Pick products from the allowed list
				for(int i = 0; i < rand; i++) {shoppingList.add(allowedProducts.get(shoppingChoice.nextInt(randSize)));}
			}
			//If no products are on the allowed list, randomly pick from every product in the store
			//Represents aimlessly browsing the store
			else
			{
				int rand;
				double randWeight = shoppingChoice.nextDouble();
				
				//10% chance to buy nothing
				if(randWeight > 0.1) {rand = 1 + shoppingChoice.nextInt(basketSize + 1);
				if (rand > basketSize) {rand--;}}
				else {rand = shoppingChoice.nextInt(basketSize + 1);}
				for(int i = 0; i < rand; i++) {shoppingList.add(products.get(shoppingChoice.nextInt(products.size())));}
			}
		}
		return shoppingList;
	}
	
	//Purchase all of the items on the shopping list
	public LinkedList<Products> shop(LinkedList<Products> shoppingList, Shopper shopper)
	{		
		LinkedList<Products> basket = new LinkedList<Products>();
		int productChoice;
		Model.viewer.shopperStartsShopping(shopper);
		
		for(int i = 0; i < shoppingList.size(); i++) 
		{
			try {Thread.sleep(shoppingChoice.nextInt(maxTime + 1 - minTime) + minTime);}
			catch (InterruptedException e) {e.printStackTrace();}

			productChoice = shoppingList.get(i).getProductID();
			
			if(products.get(productChoice).getInStock()) 
			{
				basket.add(products.get(productChoice));
			}
		}
		
		return basket;
	}
}