/**
 * 
 */
package tme4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingUtilities;

import tme4.restore.*;
import tme4.events.*;
import tme4.gui.MainWindow;

/**
 * @author Chantal del Carmen
 * @date Feb 19, 2022
 *
 */

public class GreenhouseControls extends PauseResume implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Event> eventList = new ArrayList<Event>();
	private List<TwoTuple> twoTupleList = new ArrayList<TwoTuple>();
	private int errorCode = 0;
	private String errorMsg = "";
	private transient String fixMsg = "";
	private long shutdownTime = 0;
	private static boolean isRunning = false;
	private static PauseResume pauseResume = new PauseResume();
	
	// CONSTRUCTOR
	public GreenhouseControls() {
	}
	
	public void addTwoTuple(String eventKey, String eventValue) {
				
		TwoTuple twoTuple = new TwoTuple(eventKey, eventValue);
		
		synchronized(twoTupleList) {
			twoTupleList.add(twoTuple);
		}
		System.out.println("Adding to twotuple list: " + twoTuple);
	}

	
	public void setVariable(String eventKey, String eventValue) {
		// Handles updating to collection of Event TwoTuples

		String key = "";
		
		for(TwoTuple tt : twoTupleList) {
			key = tt.getKey();
			
			if(key.equals(eventKey)) {
				tt.setValue(eventValue);
			}
		}		
	}

	public void run(String fileNameString) {
		restart(fileNameString);
	}
	
	public void restart(String fileNameString) {
		appendToTextArea("Restarting system");
				
	  	// Regex to find pattern
	    String eventRegex = "\\b(Event)=([^ ]*)\\b";
	    String timeRegex = "\\b(time)=([^ ]*)\\b";
	    String ringsRegex = "\\b(rings)=([^ ]*)\\b";
	      
	    Pattern pattern = null;
	    Matcher matcher = null;
	      
		String eventName = "";
		long time = 0;
		int rings = 1;
		String[] strArray;
		Event event = null;		
		File file = new File(fileNameString);
		Scanner sc = null;
		
		try {
			sc = new Scanner(file);
			
			while(sc.hasNext()) { // While there are more elements in file  
				
				// Split string by ","
				strArray = sc.next().split(",");
				
				for (int i = 0; i < strArray.length; i++) {
					
			        if(i == 0) { // 1st pattern is the event element
			        	 pattern = Pattern.compile(eventRegex);
			        }			        
			        if(i == 1) { // 2nd element is the time element
			        	 pattern = Pattern.compile(timeRegex);
			        }			        
			        if(i == 2) { // 3rd element is the rings element
			        	pattern = Pattern.compile(ringsRegex);
			        }
			        
			        // Match pattern against String element
			        matcher = pattern.matcher(strArray[i]);
			        
			        // If match is found, take the 2nd part of the element to 
			        // extract the data needed (matcher.group(2))
			        while (matcher.find()) {				        
				        if(i == 0) {
				        	eventName = matcher.group(2);
				        }				        
				        if(i == 1) {
				        	time = Long.parseLong(matcher.group(2));
				        }	
				        if(i == 2) {
				        	rings = Integer.parseInt(matcher.group(2));	
				        }
			        }				        		  
			    }
				
				// Call function to create event
				event = createEvent(eventName, time);
				executor.execute(event);
				addEvent(event);
				
//		    		if(eventName.equals("Bell")) {
//						long bellEventTime = time;
//						long eventTime = 0;
//						
//						for(int i = 0; i < rings; i++) {
//							eventTime = bellEventTime + (i * 2000);
//							gc.addEvent(new Bell(eventTime));
//						}
//		    		} else {
//		    			gc.addEvent(event);
//		    		}  		
			}
			// Close scanner
			sc.close();	
				
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public <T extends Event> T createEvent(String eventName, long time) {
    	Constructor<T> cons = null;
    	T event = null;
    	Class clazz = null;
		try {
    		clazz = Class.forName("tme4.events." + eventName);
			cons = clazz.getConstructor(long.class);
			event = cons.newInstance(time);
			return event;
		} catch (NoSuchMethodException | SecurityException | InstantiationException | 
				IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void appendToTextArea(String s) {
		window.appendToTextArea(s);
	}
	
	public void pause() {
		pauseResume.setPaused(true);
        
        synchronized(lock) {
            lock.notifyAll();
            System.out.println("PAUSED");
        }
	}
	
	public void resume() {
		pauseResume.setPaused(false);

        synchronized(lock) {
            lock.notifyAll();
            System.out.println("RESUME");
        }
	}
	
//---------------------------- CONTROLLER ------------------------
	  
	public void serialize(GreenhouseControls gc) {
		// Saves state of the system to file by serializing the object
		FileOutputStream fileOut;
		ObjectOutputStream objOut;
		try {
			fileOut = new FileOutputStream("dump.out");
			objOut = new ObjectOutputStream(fileOut);
			objOut.writeObject(gc);
			objOut.close();
			fileOut.close();
			System.out.println("Serialized data is saved in dump.out.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
		
	public void shutdown() {	  
		  // Call serialize() method, then emergency shutdown system
//		  GreenhouseControls greenhouseControls = this;
		  serialize(this);
		  
		  System.out.println("\nSYSTEM INITIATING EMERGENCY SHUTDOWN...");		  
		  System.out.println("Program terminated.");
		  
		  setShutdownTime(System.currentTimeMillis());
		  System.exit(0);
	}
		
	public Fixable getFixable(int errorCode) {
		  Fixable fixable = null;
		  
		  if(errorCode == 1) { // Window malfunction
			  fixable = new FixWindow();
		  } else if(errorCode == 2) { // Power outage
			  fixable = new PowerOn();
		  } else if(errorCode == 0) { // System is normal
			  System.out.println("No system malfunctions.");
		  } else { // Invalid error code
			  System.out.println("Error code not a known value.");
		  }	
		return fixable;
	}

	
	  public void addEvent(Event event) { 
		  	// Add Event to list of Events
		  	eventList.add(event); 
		  	System.out.println("Adding to event list: " + event.getClass().getSimpleName());
	  }
	  
	  public void removeEvent(Event event) { 
		  // Remove Event from event list
		  eventList.remove(event); 
		  System.out.println("Removing from event list: " + event.getClass().getSimpleName());
	
	  }

	//------------------- GETTERS / SETTERS -------------------------
  
	public List<Event> getEventList() {
		return eventList;
	}

	public void setEventList(List<Event> eventList) {
		this.eventList = eventList;
	}

	public List<TwoTuple> getTwoTupleList() {
		return twoTupleList;
	}

	public void setTwoTupleList(List<TwoTuple> twoTupleList) {
		this.twoTupleList = twoTupleList;
	}
	
	public int getErrorCode() {
		return errorCode;
	}


	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}


	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}


	public String getFixMsg() {
		return fixMsg;
	}


	public void setFixMsg(String fixMsg) {
		this.fixMsg = fixMsg;
	}
	
	public long getShutdownTime() {
		return shutdownTime ;
	}

	public void setShutdownTime(long shutdownTime) {
		this.shutdownTime = shutdownTime;
	}
	
	/**
	 * @return
	 */
	public static boolean isRunning() {
		return isRunning;
	}

	public static void setRunning(boolean isRunning) {
		GreenhouseControls.isRunning = isRunning;
	}
	
	//---------------------------- MAIN --------------------------------
	private static MainWindow window = null;	
	
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, 
	InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InterruptedException {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() { window = new MainWindow(550, 400); }
		});		
		
	}
	
}
