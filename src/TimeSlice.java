//Connor Dawkins | 16/10/2020
import java.util.TimerTask;

public class TimeSlice extends TimerTask
{
	int i = 0;
	
	//Every 1ms print a time slice
	@Override
	public void run() 
	{
		i++;
		Model.viewer.enterTimeSlice(i);
	}
}
