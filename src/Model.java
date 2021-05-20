//Connor Dawkins | 16/10/2020
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Model 
{
	//MVC
	static Viewer viewer;
	static Controller controller = new Controller();
	
	//Static Variables
	static volatile int customersInStore;
	static volatile int totalCustomers;
	static volatile List<Till> tills;
	static volatile List<Cashier> cashiers;
	static volatile List<Shopper> shoppers;
	static volatile List<Products> products;
	static LinkedList<String> types;
	static LinkedList<String> subTypes;
	static List<String> output;
	
	//Timing Variables
	static int time;
	static int oneMinute;
	static int oneHour;
	static int oneDay;
	static int checkoutTime;
	
	//Simulation Operation Variables
	static int tillNum;
	static int queueLength;
	static int shoppersPerMinute;
	static int basketSize;

	public static void main(String[] args)
	{
		//Start Controller and Viewer threads
		controller.start();
		controller.setDefaultSettings();
		viewer.start();
		viewer.printDefaultSettings();
		
		//Check if the user wants to load custom values or not
		String input = viewer.enterCustomValues();
		String y = "y";
		if(input.equalsIgnoreCase(y))
		{
			int lineCount = -1;
			String line;
			try 
			{
				BufferedReader customReader = new BufferedReader(new FileReader("customValues.txt"));
				while ((line = customReader.readLine()) != null) 
				{
					lineCount++;
					//Read custom values from customValues.txt
					//Hands the variables to the controller and tells it to set them in the Model
					switch(lineCount)
					{
						case 1:
							controller.addToOperationQueue("setOneMinute", Integer.parseInt(line));
							break;
						case 3:
							controller.addToOperationQueue("setOneHour", oneMinute * Integer.parseInt(line));
							break;
						case 5:
							controller.addToOperationQueue("setDay", oneHour * Integer.parseInt(line));
							break;
						case 7:
							controller.addToOperationQueue("setTillNum", Integer.parseInt(line));
							break;
						case 9:
							controller.addToOperationQueue("setQueueLength", Integer.parseInt(line));
							break;
						case 11:
							controller.addToOperationQueue("setShoppersPerMinute", Integer.parseInt(line));
							break;
						case 13:
							controller.addToOperationQueue("setBasketSize", Integer.parseInt(line));
							break;
						case 15:
							if(Boolean.parseBoolean(line)) {controller.addToOperationQueue("setST", 1);}
							else {controller.addToOperationQueue("setST", 0);}
							break;
						case 17:
							if(Boolean.parseBoolean(line)) {controller.addToOperationQueue("setEQT", 1);}
							else {controller.addToOperationQueue("setEQT", 0);}
							break;
						case 19:
							if(Boolean.parseBoolean(line)) {controller.addToOperationQueue("setECT", 1);}
							else {controller.addToOperationQueue("setECT", 0);}
							break;
						case 21:
							if(Boolean.parseBoolean(line)) {controller.addToOperationQueue("setLCT", 1);}
							else {controller.addToOperationQueue("setLCT", 0);}
							break;
						case 23:
							if(Boolean.parseBoolean(line)) {controller.addToOperationQueue("setQT", 1);}
							else {controller.addToOperationQueue("setQT", 0);}
							break;
						case 25:
							if(Boolean.parseBoolean(line)) {controller.addToOperationQueue("setCT", 1);}
							else {controller.addToOperationQueue("setCT", 0);}
							break;
					}
				}
				customReader.close();
			}
			catch (IOException e1) {e1.printStackTrace();}
		}
		
		//Load Products From CSV
		try 
		{
			String line;
			String[] data;
			boolean skipOneLine = true;
			BufferedReader productsCSVReader = new BufferedReader(new FileReader("products.csv"));
			while ((line = productsCSVReader.readLine()) != null) 
			{
				//Skip the first line, it is just the Column Names
				if(!skipOneLine) 
				{
					data = line.split(",");
					products.add(new Products(data));
					//Make sure to only save one of each Type and SubType
					//This is used by the Shopper class to select what items the shoppers are looking for
					if(!types.contains(data[4])) {types.add(data[4]);}
					if(!subTypes.contains(data[5])) {subTypes.add(data[5]);}
				}
				skipOneLine = false;
			}
			productsCSVReader.close();
		}
		catch (IOException e1) {e1.printStackTrace();}
		
		//Load Cashier Schedules From CSV
		LinkedList<Boolean> schedule = new LinkedList<Boolean>();
		try 
		{
			String line;
			String[] data;
			boolean skipOneLine = true;
			BufferedReader cashierCSVReader = new BufferedReader(new FileReader("cashierSchedule.csv"));
			while ((line = cashierCSVReader.readLine()) != null) 
			{
				//Skip the first line, it is just the Column Names
				if(!skipOneLine) 
				{
					data = line.split(",");
					schedule.add(Boolean.valueOf(data[1]));
				}
				skipOneLine = false;
			}
			cashierCSVReader.close();
		}
		catch (IOException e1) {e1.printStackTrace();}
		
		//Timers and Tasks
		TimerTask timeSlice = new TimeSlice();
		TimerTask arrivingShoppers = new ArrivingShoppers(basketSize, shoppersPerMinute);
		TimerTask cashierSchedule = new CashierSchedule(tillNum, schedule);
		Timer timeSliceTimer = new Timer(true);
		Timer arrivingShoppersTimer = new Timer(true);
		Timer cashierScheduleTimer = new Timer(true);
		
		//Create Tills, Cashiers and Schedule
		for(int i = 0; i < tillNum; i++) 
		{
			tills.add(new Till(i, queueLength));
			Model.cashiers.add(new Cashier(i));
			
			//Allocate cashier schedule.
			for(int x = 0; x < 23; x++) 
			{
				Model.cashiers.get(i).setSchedule(schedule.get(x));
			}
		}
		
		//Start Simulation
		viewer.simulationStarted();
        timeSliceTimer.scheduleAtFixedRate(timeSlice, 0, oneMinute);
        cashierScheduleTimer.scheduleAtFixedRate(cashierSchedule, 0, oneHour);
        arrivingShoppersTimer.scheduleAtFixedRate(arrivingShoppers, 0, oneMinute);
        
        //Start Till Threads
        for(int i = 0; i < tillNum; i++) {tills.get(i).start();}
        
		//Sleep Model Thread (How long the program runs for)
        try {Thread.sleep(oneDay);}
    	catch (InterruptedException e) {e.printStackTrace();}
        
        //End Simulation
        timeSliceTimer.cancel();
        arrivingShoppersTimer.cancel();
        cashierScheduleTimer.cancel();
        
        //Sleep Model Thread to let lingering threads close themselves
        try {Thread.sleep(20);}
        catch (InterruptedException e) {e.printStackTrace();}
        
        viewer.endOutput();
        
        //Debug variables
        int times = 0; int times2 = 0; int shLeft = 0;
        
        //Counts how many shoppers went through the checkout
        //how many went through more than once (should be 0)
        //and how many shoppers left the store because they waited too long
        for(int i = 0; i < shoppers.size(); i++) 
        {
        	if(shoppers.get(i).getTimesRun() == 1) {times++;}
        	if(shoppers.get(i).getTimesRun() > 1) {times2++;}
        	if(shoppers.get(i).getShopperLeft() > 0) {shLeft++;}
        }
        
        viewer.debugOutput(times, times2, shLeft, totalCustomers);
        
        //Write all output to `output.txt`
        try
        {
        	FileWriter outputWriter = new FileWriter("output.txt", false);
        	for(int i = 0; i < output.size(); i++) 
        	{
        		outputWriter.write(output.get(i) + "\n");
        	}
        	outputWriter.close();
        }
        catch (IOException e) {e.printStackTrace();}
        viewer.pressAnyKey();
	}
}