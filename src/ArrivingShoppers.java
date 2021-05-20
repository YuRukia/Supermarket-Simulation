//Connor Dawkins | 16/10/2020

import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class ArrivingShoppers extends TimerTask
{
	private int shoppers = 0;
	private int basketSize;
	private int shoppersPerMinute;
	
	public ArrivingShoppers(int bskSz, int sPM)
	{
		this.basketSize = bskSz;
		this.shoppersPerMinute = sPM;
	}

	@Override
	public void run() 
	{
		//Open the store at 08:00 and close at 22:00
		if(Model.time >= 8 && Model.time <= 22)
		{
			//Randomly select the number of shoppers to add from 1-shoppersPerMinute
			for(int i = 0; i < ThreadLocalRandom.current().nextInt(1, shoppersPerMinute + 1); i++) 
			{
				//Add new shopper, assign ID and basketSize
				Model.shoppers.add(new Shopper(shoppers, basketSize));
				Model.customersInStore++;
				Model.totalCustomers++;
				//Start new shopper thread
				Model.shoppers.get(shoppers).start();
				shoppers++;
			}
		}
	}
}