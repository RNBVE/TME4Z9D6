/**
 * 
 */
package tme4;

import java.io.Serializable;

/**
 * Title: TwoTuple.java
 * @author Chantal del Carmen
 * @date Feb 21, 2022
 * Student ID: 3514814
 */

/** Based on the TwoTuple.java program on page 621 of Thinking in Java, 
 *  4th Edition by Bruce Eckel 
 */

/**
 * CODE...
 */

public class TwoTuple implements Serializable {
	// Key: variable the Event is modding
	// Value: value the Event is setting
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String key;
	private String value;
	
	public TwoTuple(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public String toString() {
		return key + " - " + value;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}

	/**
	 * @param eventValue
	 */
	public void setValue(String value) {
		this.value = value;
		
	}
}

