package ya.thn.project.MoneyService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ServiceConfig {
	
	//public static final String CURRENCY_CONFIG_FILE = "CurrencyConfig_2020-04-01.txt";
	public static final int CURRENCY_CONFIG_FILE_LINE_START = 2;
	
	static void readConfig(String file) {
		
	}
	
	public static void readCurrencyConfigFile() {
		
		// check if storage is a text file
		if (currencyFile.endsWith(".txt")) {
			
//			Map<String, Currency> data = new HashMap<String, Currency>();
			int lineNumber = 1;
			
			try(BufferedReader br = new BufferedReader(new FileReader(currencyFile))) {
				while (br.ready()) {
					
					String row = br.readLine();
					if (lineNumber++ < CURRENCY_CONFIG_FILE_LINE_START) continue;
					
					Currency currency = parseInput(row);
					MoneyServiceApp.currencyMap.putIfAbsent(currency.getCurrencyCode(), currency);
				}
			}
			catch (IOException ioe) {
				System.out.println("An IOException occurred for file " + currencyFile);
			}
			
		}
		// Not a textfile
		else
			System.out.println("The input file is not a text file");
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
		double exchangeRate = Integer.parseInt(exchangeRateString);
		
		if (currencyCodeParts[0].strip().length() > 1)
			return new Currency(currencyCode, exchangeRate/100);
		else
			return new Currency(currencyCode, exchangeRate);
	}
}
