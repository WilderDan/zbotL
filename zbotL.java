import java.awt.event.InputEvent;
import java.awt.MouseInfo;
import java.io.*;

public class zbotL extends MouseRobot{

    private static final int START_WAIT = 2000;
    private static final int RECORD_WAIT = 2000;
    private static final int HOVER_WAIT = 1000;
    private static final int EXTRA_CLICK_WAIT = 100; // If needed

    /* TO DO: Command line argument parsing */
    public static void main(String[] args) {
	
        int counter = 0, lineNum = 1;
	String action, script;
        zbotL robot = new zbotL();
     
	if (args.length < 2) {
	    showUsage();
	    System.exit(1);
	}

	action = args[0];
	script = args[1];
        

	if (action.equals("record") == false && action.equals("execute") == false
	    && action.equals("resume") == false) {
	    System.out.println("***Invalid action name***\n");
	    showUsage();
	    System.exit(1);
	}

        if (action.equals("record") == true) { 
            robot.record(script);
	}

	else if (action.equals("resume")) {
	    if (args.length < 3) {
		System.out.println("***Missing line number***\n");
	        showUsage();
	        System.exit(1);
	    }
	    lineNum = Integer.parseInt(args[2]); 
	    System.out.println("Resuming script '" + script + "' from line " + lineNum + "..."); 
            robot.sleep(START_WAIT);
            robot.execute(script, lineNum);         
	}
 
        if (action.equals("execute") || action.equals("resume")) { 
            System.out.println("Preparing to run script '" + script + "'...");
            while(true){  
                robot.sleep(START_WAIT);
                robot.execute(script, 1);
                counter++;
                System.out.println("Script executed " + counter + " times.");
            }
	}
    }

    private static void showUsage() {
	System.out.println("USAGE:\n\tjava zbotL record script_name\n\t\tor");
	System.out.println("\tjava zbotL execute script_name\n\t\tor");
	System.out.println("\tjava zbotL resume script_name line_num\n");
    }

    private void record(String script) {
	int mouseX, mouseY;
	long start, end, timed;

	try {
            FileWriter writer = new FileWriter(script, true);

            System.out.println("Script name is '" + script + "'");
	    System.out.println("Move cursor to target and wait. Preparing to record...");
            
	    sleep(RECORD_WAIT);  

            mouseY = MouseInfo.getPointerInfo().getLocation().y;
    	    mouseX = MouseInfo.getPointerInfo().getLocation().x;
            writer.write(mouseX + " " + mouseY + " ");
            
	     
    	    System.out.println("Position recorded!\n");
	    System.out.println("Starting timer...");
	    System.out.println("Now click target"); 	
	    System.out.println("Press <enter> when resulting action finishes.");

	    start = System.currentTimeMillis();    
            System.in.read();
	    end = System.currentTimeMillis();

	    System.out.println("Time recorded!");
	    timed = end - start;

	    writer.write(timed + "\n");
	    writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void execute(String script, int startLine) {
        String line = null;
        String words[];
	int x_coord, y_coord, after_wait, count = 0;
	
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(script);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
		++count;
		System.out.println("\t[LINE #] " + count);
		if (count < startLine)
		    continue;

                words = line.split(" ");
		
		// indicates comment line
		if (words[0].equals("#") == true)
			continue;

		x_coord = Integer.parseInt(words[0]);
		y_coord = Integer.parseInt(words[1]);
		after_wait = Integer.parseInt(words[2]);
		zClick(x_coord, y_coord, after_wait);
            }   

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to execute script '" + script + "'");
	    System.exit(1);                
        }
        catch(IOException ex) {
            System.out.println("Error reading script '" + script + "'");
            System.exit(1);                  
        }
    }

    private void zClick(int x, int y, int time_to_finish_click) {
        int maskL = InputEvent.BUTTON1_MASK;
      
        robot.mouseMove(x, y);

	// Can manually put negative value in time field to just move mouse WITHOUT clicking
        if (time_to_finish_click > 0) {
           click(maskL);
	   sleep(time_to_finish_click + EXTRA_CLICK_WAIT);
	}
	else {
 	   sleep(HOVER_WAIT);
        }
    } 
}
