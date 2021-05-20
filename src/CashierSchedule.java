//Connor Dawkins | 16/10/2020
import java.util.LinkedList;
import java.util.TimerTask;

public class CashierSchedule extends TimerTask
{
	int tillNum;
	boolean updateTills = true;
	int iterations = 0;
	LinkedList<Boolean> schedule = new LinkedList<Boolean>();
	
	public CashierSchedule(int tlNm, LinkedList<Boolean> schedule2) 
	{
		this.tillNum = tlNm;
		this.schedule = schedule2;
	}

	@Override
	public void run()
	{
		if(Model.time >= 23) {Model.time = 0; iterations = 0;}
		else 
		{	
			//Update Tills and Cashiers
			for(int i = 0; i < tillNum; i++) 
			{
				//Set if they are working or not based on their schedule at this time
				Model.cashiers.get(i).setWorking(Model.cashiers.get(i).getSchedule(iterations));
				//Update Tills with Cashiers current status
				Model.tills.get(Model.cashiers.get(i).getTill()).setCashierAtTill(Model.cashiers.get(i).getWorking());
			}
			iterations++;
			Model.time++;
			Model.viewer.addMainTime(Model.time);
		}
	}
}