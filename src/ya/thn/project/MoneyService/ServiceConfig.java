package ya.thn.project.MoneyService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ServiceConfig {

	public static final String CONFIG_FILE = "ProjectConfig.txt";
	public static final int CONFIG_FILE_LINE_START = 3;
	
	static void readConfig(String file) {
		
	}
	
	public static Map<String, Double> readConfigFile() {
		Map<String, Double> box = new HashMap<String, Double>();
		boolean completed = false;
		int lineNumber = 0;
		
		try(BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE))){
			while(br.ready() && !completed) {
				
				String row = br.readLine();
				if(lineNumber++ <= CONFIG_FILE_LINE_START) continue;

				if(row.equals("End")) {
					completed = true;
					break;
				}
				
				// The column looks like following:
				// column0 = Currency Code.
				// column1 = the 'equal' sign.
				// column2 = the currency amount.
				String[] columns = row.split(" ");
				box.putIfAbsent(columns[0], Double.parseDouble(columns[2]));
			}
		}
		catch(IOException ioe) {
			System.out.println("Sorry, could read config file.");
		}
		
		return box;
	}
}
