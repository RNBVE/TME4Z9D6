/**
 * 
 */
package tme4.gui;

import tme4.GreenhouseControls;
import tme4.PauseResume;
import tme4.events.Terminate;
import tme4.events.Event;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Chantal del Carmen
 * @date Mar 6, 2022
 *
 */


public class MainWindow extends PauseResume {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame = null;
	
	public MainWindow() {		
	}
	
	// Constructor
	public MainWindow(int x, int y) {
		createWindow(x, y);
	}
	
	//------------------------------- LAYOUT -----------------------------------
	
	private JLabel eventsFileLabel;
	private JTextArea fileNameTextArea;
	private JTextArea textArea;
	
	public void createWindow(int x, int y) {
		frame = new JFrame();		
		eventsFileLabel = new JLabel("Events File:");
		fileNameTextArea = new JTextArea(1, 20);
		textArea = new JTextArea(15, 50);
		
		// Prep GUI
		frame.setTitle("COMP308 TME4");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Exit on shutdown
		frame.setSize(x, y); // Size of window panels
		frame.setVisible(true); // Make frame visible
		
		makeButtons();
		makePopupMenu();
		makePulldownMenu();
		makeLayout();
	}
	
	public JTextArea getTextArea() {
		return textArea;
	}
	
	private void makeLayout() {		
		frame.setJMenuBar(menuBar); // Add menu bar to the frame		
		frame.setLayout(new FlowLayout()); // Set layout
		
		fileNameTextArea.setEditable(false);
		
		frame.add(popupMenu);
		frame.add(eventsFileLabel);	
		frame.add(fileNameTextArea);
		frame.add(new JScrollPane(textArea));
		
		frame.add(startButton);
		frame.add(restartButton);
		frame.add(terminateButton);
		frame.add(suspendButton);
		frame.add(resumeButton);
	}

	
	//--------------------------- MAKE BUTTONS ----------------------------------
	
	private JButton
		startButton = null,
		restartButton = null,
		terminateButton = null,
		suspendButton = null,
		resumeButton = null;
	
	private void makeButtons() {		
		startButton = new JButton("Start");
		restartButton = new JButton("Restart");
		terminateButton = new JButton("Terminate");
		suspendButton = new JButton("Suspend");
		resumeButton =  new JButton("Resume");		
		
		addButtonListeners();			
	}
	
	
	//------------------------ BUTTON/POPUP MENU ACTION LISTENERS ----------------------
	
	private ExecutorService guiExecutor = Executors.newSingleThreadExecutor();
	private String fileNameString = "";
	
	private ActionListener startAL = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			guiExecutor.execute(new Runnable() {
				@Override
				public void run() {
					start();
				}
				
			});
		}
	};
	
	private void start() {

		// Get filename input 
		fileNameString = fileNameTextArea.getText();
		
		// If text file valid, start system run
		if(!(fileNameString.isEmpty())) {
			gc.run(fileNameString);	
			
		} else { // Invalid text file, or file not chosen
			
			// Disable Start and Restart button if no file is read
			startButton.setEnabled(false);
			restartButton.setEnabled(false);
			
			// Show error msg
			textArea.setText("Please choose a file, before clicking Start/Restart");
		}		
	}
	
	
	private ActionListener restartAL = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			guiExecutor.execute(new Runnable() {
				@Override
				public void run() {
					// Clear the text area
					textArea.selectAll();
					textArea.replaceSelection("");
					
					start();
				}
				
			});
		}
	};
	
	private ActionListener terminateAL = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
			guiExecutor.execute(new Runnable() {
				@Override
				public void run() {
					// Open a dialog window to accept user input for delay time of the Terminate Event
					String input = JOptionPane.showInputDialog("Please enter delay time in milliseconds");
					
					// Parse the string input to a long
					long inputLong = Long.parseLong(input);
					
					// Create Terminate event, add it to the event list, and execute it
					Terminate terminate = new Terminate(inputLong);
			    	gc.addEvent(terminate);
			    	executor.execute(terminate);
			    	
			    	// Shutdown the event executor and gui executor
			    	executor.shutdown();
					guiExecutor.shutdown();				
				}				
			});		
		}
	};
	
	private ActionListener suspendAL = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			guiExecutor.execute(new Runnable() {
				@Override
				public void run() {
					gc.pause();
				}
				
			});
		}
	};
	
	private ActionListener resumeAL = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			guiExecutor.execute(new Runnable() {
				@Override
				public void run() {
					gc.resume();
				}
				
			});			
		}
	};
	
	private void addButtonListeners() {
		startButton.addActionListener(startAL);
		restartButton.addActionListener(restartAL);
		terminateButton.addActionListener(terminateAL);
		suspendButton.addActionListener(suspendAL);
		resumeButton.addActionListener(resumeAL);
	}
	
	//------------------------------ POPUP MENU -----------------------------

	private JPopupMenu popupMenu = null;
	
	private void makePopupMenu() {
		startMenuItem = new JMenuItem("Start");
		restartMenuItem = new JMenuItem("Restart");
		terminateMenuItem = new JMenuItem("Terminate");
		suspendMenuItem = new JMenuItem("Suspend");
		resumeMenuItem =  new JMenuItem("Resume");

		popupMenu = new JPopupMenu();
		popupMenu.add(startMenuItem);
		popupMenu.add(restartMenuItem);
		popupMenu.add(terminateMenuItem);
		popupMenu.add(suspendMenuItem);
		popupMenu.add(resumeMenuItem);
		
		addPopupMenuListeners();			
	}
	
	private void addPopupMenuListeners() {
		
		// Create a listener to show pop-up menu when the mouse is clicked
		MouseAdapter popupListener = new MouseAdapter() {  
            public void mouseClicked(MouseEvent e) {              
                popupMenu.show(e.getComponent(), e.getX(), e.getY());  
            }                 
         };
		
         // Add the pop-up menu listener to the frame and text area
        frame.addMouseListener(popupListener);        
		textArea.addMouseListener(popupListener);		
		
		// Add listeners to the pop-up menu items
		startMenuItem.addActionListener(startAL);
		restartMenuItem.addActionListener(restartAL);
		terminateMenuItem.addActionListener(terminateAL);
		suspendMenuItem.addActionListener(suspendAL);
		resumeMenuItem.addActionListener(resumeAL);
	}
	
	//------------------------------ PULLDOWN MENU --------------------------
	
	private JMenuBar menuBar = null;
	private JMenuItem 
		newMenuItem = null,
		closeMenuItem = null,
		openMenuItem = null,
		restoreMenuItem = null,
		exitMenuItem = null,
		startMenuItem = null,
		restartMenuItem = null,
		terminateMenuItem = null,
		suspendMenuItem = null,
		resumeMenuItem = null;
	
	private void makePulldownMenu() {
		// Create menu bar
		menuBar = new JMenuBar();
		
		// Create pulldown menu
		JMenu pulldownMenu = new JMenu("File");
		
		// Create menu items
		newMenuItem = new JMenuItem("New Window");
		closeMenuItem = new JMenuItem("Close Window");
		openMenuItem = new JMenuItem("Open Events");
		restoreMenuItem = new JMenuItem("Restore");
		exitMenuItem = new JMenuItem("Exit");
			
		pulldownMenu.add(newMenuItem);
		pulldownMenu.add(closeMenuItem);
		pulldownMenu.add(openMenuItem);
		pulldownMenu.add(restoreMenuItem);
		pulldownMenu.add(exitMenuItem);
				
		// Add pulldown menu to menu bar
		menuBar.add(pulldownMenu);		
		
		// Add ActionListeners to the pulldown menu items
		addPulldownMenuListeners();

	}

	private void addPulldownMenuListeners() {
	
		// Add ActionListeners to menu items
		
		newMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				guiExecutor.execute(new Runnable() {
					@Override
					public void run() {
						// Clear the text area and filename area to
						// simulate a new window
						textArea.selectAll();
						textArea.replaceSelection("");						
						fileNameTextArea.selectAll();
						fileNameTextArea.replaceSelection("");
					}					
				});				
			}			
		});
		
		closeMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				guiExecutor.execute(new Runnable() {
					@Override
					public void run() {
						frame.dispose();				
					}
					
				});
			}			
		});
		
		openMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				guiExecutor.execute(new Runnable() {
					@Override
					public void run() {
						JFileChooser fileChooser = new JFileChooser();
						int option = fileChooser.showOpenDialog(frame);
						if(option == JFileChooser.APPROVE_OPTION) {
							fileNameTextArea.setText(fileChooser.getSelectedFile().toString());
						}
						if(option == JFileChooser.CANCEL_OPTION) {
							fileNameTextArea.setText("You pressed cancel");
						}					
					}
					
				});
			}	
			
		});
		
		restoreMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				guiExecutor.execute(new Runnable() {
//					@Override
//					public void run() {
//						JFileChooser fileChooser = new JFileChooser();
//						int option = fileChooser.showOpenDialog(GUI.this);
//						if(option == JFileChooser.APPROVE_OPTION) {
//							fileNameTextArea.setText(fileChooser.getSelectedFile().toString());
//							String restoreFile = fileNameTextArea.getText();
//							Restore restore = new Restore(restoreFile);
//							
//							// Deserialize GreenhouseControls object
//							GreenhouseControls gc = restore.deserialize();			
//							
//							// Get Fixable to fix the malfunction + log the fix
//							Fixable fixable = gc.getFixable(gc.getErrorCode());
//							fixable.fix(gc);
//							fixable.log(gc);				
//							
//							// Get the time that the program shutdown
//							long shutdownTime = gc.getShutdownTime();
//							
//							// Call start() method on remaining Events in the eventList to reset
//							// current time and calculate the event time for each Event, minus shutdown time
//							for(Event event : gc.getEventList()) {
//								event.start(shutdownTime);
//							}
//							
//							appendToTextArea("\nRestarting system from event after malfunction...");
//							appendToTextArea("Please wait...");
//							
//							gc.run();
//						}
//						
//						if(option == JFileChooser.CANCEL_OPTION) {
//							fileNameTextArea.setText("You pressed cancel");
//						}	
//					}
//					
//				});			
			}
			
		});
		
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

	// Append given String to main text area
	public void appendToTextArea(String s) {
		textArea.append(s + "\n");		
	}
	
} // End MainWindow class



