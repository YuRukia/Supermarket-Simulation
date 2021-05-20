//Connor Dawkins | 16/10/2020
import java.util.LinkedList;

public class Cashier 
{
	private int cashierID;
	private int runsTill;
	private boolean isWorking;
	//List of bools that says if the cashier is working at that time or not
	private LinkedList<Boolean> workingSchedule = new LinkedList<Boolean>();
	
	public Cashier(int tillID)
	{
		this.runsTill = tillID;
	}
	
	public int getCashierID() {return this.cashierID;}
	
	public int getTill() {return this.runsTill;}
	public void setTill(int till) {this.runsTill = till;}
	
	public boolean getWorking() {return this.isWorking;}
	public void setWorking(boolean working) { this.isWorking = working;}
	
	public Boolean getSchedule(int i) {return this.workingSchedule.get(i);}
	public void setSchedule(Boolean working) {this.workingSchedule.add(working);}
}