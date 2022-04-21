package tme4;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 */

/**
 * @author Chantal del Carmen
 * @date Apr 19, 2022
 *
 */
public class PauseResume {
	
//    protected static Object lock = new Object();
	protected static Lock lock = new ReentrantLock();
    protected static volatile boolean paused = false;
	protected static GreenhouseControls gc = new GreenhouseControls();
	protected ExecutorService executor = Executors.newCachedThreadPool();
    
    protected void allowPause() {
        synchronized(lock) {
        	System.out.println("In PauseResume.allowPause()");
        	
            while(paused) {
                try {
                    System.out.println("Waiting for resume...");
                    lock.wait();
                } catch(InterruptedException e) {
                	System.out.println("Locking unsuccessful");
                }
            }
        }
    }

    protected void sleep(long delayTime) {
        try {
            Thread.sleep(delayTime);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void setPaused(boolean paused) {
    	PauseResume.paused = paused;
    }
}
