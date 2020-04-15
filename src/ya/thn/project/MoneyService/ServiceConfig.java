package ya.thn.project.MoneyService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ServiceConfig {

	private static final String CONFIG_FILE = "ProjectConfig.txt";
	
	private static String currencyFile;
	static Map<String, Double> box = new HashMap<String, Double>();
	
	
	public static void readProjectConfigFile() {
		boolean insertToBox = false;
		
		try(BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE))){
			while(br.ready()) {
				String row = br.readLine();
				
				// Start inserting cash to the 'box'.
				if(row.equals("BoxOfCash")) {
					insertToBox = true;
					continue;
				}

				// Stop inserting cash to the 'box'.
				if(row.equals("End") && insertToBox) {
					insertToBox = false;
					continue;
				}
				
				// If the row doesn't include ' = ' then continue to next line.
				// Maybe bad format.
				// TODO: perhaps create suitable exception?
				if(!row.contains(" = ")) continue;
				
				// Split the row by key, value.
				String[] columns = row.split(" = ");
				
				// Decide if the key/value should be inserted to
				// box or updating other variables.
				if(insertToBox) {
					box.putIfAbsent(columns[0], Double.parseDouble(columns[1]));
				} else {
					switch(columns[0]) {
						case "CurrencyConfig":
							currencyFile = columns[1];
						case "ReferenceCurrency":
							MoneyServiceApp.referenceCurrencyCode = columns[1];
						default:
							throw new IllegalArgumentException(
										String.format("%s is not a valid setting", columns[0])
									);
					}
				}
			}
		}
		catch(IOException ioe) {
			System.out.println("Sorry, could read config file.");
		}
	}
}
