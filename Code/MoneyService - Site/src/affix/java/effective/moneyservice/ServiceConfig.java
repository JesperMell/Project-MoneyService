package affix.java.effective.moneyservice;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Helper class for IO, reading configuration files and
 * exchange rate files
 * 
 * @author Group Center
 *
 */

public class ServiceConfig {

	/**
	 * CONFIG_FILE - A String defining name of configuration file
	 */
	private static final String CONFIG_FILE = "ProjectConfig.txt";
	
	/**
	 * String for setting which line in the configuration file to start reading from
	 */
	private static final int CURRENCY_CONFIG_FILE_LINE_START = 2;
	
	/**
	 * A String defining a configuration file
	 */
	private static final String MONEYSERVICE_CONFIG_FILE = "MoneyServiceConfig.txt";
	
	/**
	 * A String for storing a configured filepath for currency exchange rates
	 */
	private static String currencyFile;
	
	
	/**
	 * BUY_RATE - double defining the profit margin which exchange rate
	 *  for buying should be adjusted with.
	 */
	static final double BUY_RATE = 0.995;
	
	/**
	 * SELL_RATE - double defining the profit margin which exchange rate
	 * for selling should be adjusted with.
	 */
	static final double SELL_RATE = 1.005;
	
	//logger
	/**
	 * logger - a Logger object
	 */
	private final static Logger logger = Logger.getLogger("affix.java.effective.moneyservice");
    
	
	/**
	 * Method for reading and parsing ProjectConfig.txt
	 * setting configurable values for the inventory in each currency
	 * and set the exchange rate file to be read.
	 */
	
	public static void readProjectConfigFile() {
		logger.log(Level.INFO, "Entering readProjectConfigFile method..");
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
							currencyFile = String.format("ExchangeRates/%s", columns[1]);
							break;
						case "ReferenceCurrency":
							MoneyServiceApp.referenceCurrencyCode = columns[1];
							break;
						default:
							logger.log(Level.WARNING, "setting: " + columns[0] + " not valid!");
							throw new IllegalArgumentException(
										String.format("%s is not a valid setting", columns[0])
									);
					}
				}
			}
		}
		catch(IOException ioe) {
			// add log.info
			logger.log(Level.SEVERE, "Sorry, could not read config file." + ioe);
			System.out.println("Sorry, could not read config file.");
		}
	}
	
	/**
	 * Method for reading the currency configuration file and set accepted currencies 
	 * and their exchange rates
	 */
	
	public static void readCurrencyConfigFile() {
		logger.log(Level.INFO, "Entering readCurrencyConfigFile method..");
			
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
				logger.log(Level.SEVERE, "Read currency file exception! " + ioe);
				System.out.println("An IOException occurred for file " + currencyFile);
			}
	}
	
	/**
	 * Method for parsing currency configuration file
	 * @param input - A String with data to be parsed
	 * @return Currency - Data holding information about exchange rate
	 * for each currency
	 */
	
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
	
	/**
	 * Method for reading MoneyService configuration file setting
	 * the order amount restrictions
	 */
	
	public static void readMoneyServiceConfigFile() {
		logger.log(Level.INFO, "Entering readMoneyServiceConfigFile method..");

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
			logger.log(Level.SEVERE, "Read config file exception! " + ioe);
			System.out.println("Could not read " + MONEYSERVICE_CONFIG_FILE);
		}
		
		MoneyServiceApp.orderAmountLimit = orderAmountLimit;
	}
}
