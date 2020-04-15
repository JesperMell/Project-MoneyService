package ya.thn.project.MoneyService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServiceConfig {

	private static final String CONFIG_FILE = "ProjectConfig.txt";
	public static final int CURRENCY_CONFIG_FILE_LINE_START = 2;
	
	private static String currencyFile;
	
	static final double BUY_RATE = 1.005;
	static final double SELL_RATE = 0.995;
      
	public static void readProjectConfigFile() {
		boolean insertToBox = false;
		
		Map <String, Double> box = new HashMap<>();
		
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
							break;
						case "ReferenceCurrency":
							MoneyServiceApp.referenceCurrencyCode = columns[1];
							break;
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
		MoneyServiceApp.inventoryMap = box;
	}
	
	public static void readCurrencyConfigFile() {
			Map <String, Currency> currencyMap = new HashMap<>();
			
			int lineNumber = 1;
			
			try(BufferedReader br = new BufferedReader(new FileReader(currencyFile))) {
				while (br.ready()) {
					
					String row = br.readLine();
					if (lineNumber++ < CURRENCY_CONFIG_FILE_LINE_START) continue;
					
					Currency currency = parseInput(row);
					currencyMap.putIfAbsent(currency.getCurrencyCode(), currency);
				}
			}
			catch (IOException ioe) {
				System.out.println("An IOException occurred for file " + currencyFile);
			}
	}
	
	private static Currency parseInput(String input) {
		
		// The column looks like following:
		// column 0 = Period
		// column 1 = Group
		// column 2 = "Serie" (Currency code)
		// column 3 = Exchange rate
		String[] parts = input.split(";");
		
		String[] currencyCodeParts = parts[2].split(" ");
		String currencyCode = currencyCodeParts[1].strip();
		
		String exchangeRateString = parts[3].strip();
		double exchangeRate = Double.parseDouble(exchangeRateString);
		
		if (currencyCodeParts[0].strip().length() > 1)
			return new Currency(currencyCode, exchangeRate/100);
		else
			return new Currency(currencyCode, exchangeRate);
	}
}
