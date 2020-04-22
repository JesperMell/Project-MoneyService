package affix.java.effective.moneyservice;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServiceConfig {

	private static final String CONFIG_FILE = "ProjectConfig.txt";
	private static final int CURRENCY_CONFIG_FILE_LINE_START = 2;
	private static final String MONEYSERVICE_CONFIG_FILE = "MoneyServiceConfig.txt";
	
	private static String currencyFile;
	
	static final double BUY_RATE = 0.995;
	static final double SELL_RATE = 1.005;
      
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
					MoneyServiceApp.inventoryMap.putIfAbsent(columns[0], Double.parseDouble(columns[1]));
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
			System.out.println("Sorry, could not read config file.");
		}
	}
	
	public static void readCurrencyConfigFile() {
			
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
	
	public static int readMoneyServiceConfigFile() {
		
		int orderAmountLimit = 0;
		
		try(BufferedReader br = new BufferedReader(new FileReader(MONEYSERVICE_CONFIG_FILE))) {
			while (br.ready()) {
				
				String row = br.readLine();
				
				String[] parts = row.split("=");
				String orderAmountLimitString = parts[1].strip();
				orderAmountLimit = Integer.parseInt(orderAmountLimitString);
			}
		}
		catch (IOException ioe) {
			System.out.println("Could not read " + MONEYSERVICE_CONFIG_FILE);
		}
		
		return orderAmountLimit;
	}
}
