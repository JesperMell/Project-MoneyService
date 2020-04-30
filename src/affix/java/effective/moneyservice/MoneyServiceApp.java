package affix.java.effective.moneyservice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * This class triggers an application defining an ExchangeOffice for Order objects.
 * @author Group Center
 */
public class MoneyServiceApp {
	
	static String referenceCurrencyCode;
	
	// create logger
	private static Logger logger;
	
	static {
		logger = Logger.getLogger("affix.java.effective.moneyservice");
	}
	
	/**
	 * Storage for Currency objects using CurrencyCode as key
	 */
	static Map<String, Currency> currencyMap = new HashMap<>();
	static Map<String, Double> inventoryMap = new HashMap<>();
	static int orderAmountLimit;
	
	private static void setupLogger() {
		LogManager.getLogManager().reset();
		// set the level of logging.
		logger.setLevel(Level.ALL);
		// Create a new Handler for console.
		ConsoleHandler consHandler = new ConsoleHandler();
		consHandler.setLevel(Level.WARNING);
		logger.addHandler(consHandler);
		
		try {
			// Create a new Handler for file.
		FileHandler fHandler = new FileHandler("logger.log");
		fHandler.setFormatter(new SimpleFormatter());
		// set level of logging
		fHandler.setLevel(Level.FINEST);
		logger.addHandler(fHandler);
		}catch(IOException e) {
			logger.log(Level.SEVERE, "File logger not working! ", e);
		}
	}
	
	public static void main(String[] args) {
		
		configure();
		logger.log(Level.INFO, "-------Configuration_Ends-------\n");
		MoneyService aExchangeOffice = new ExchangeOffice("THN", inventoryMap);
		CLIApplication(aExchangeOffice);
	}
	
	private static void configure() {
		setupLogger();
		ServiceConfig.readMoneyServiceConfigFile();
		ServiceConfig.readProjectConfigFile();
		ServiceConfig.readCurrencyConfigFile();
	}
	
	/**
	 * This method supports user interaction via CLI
	 */
	private static void CLIApplication(MoneyService aExchangeOffice) {
		
		logger.log(Level.INFO, "Entering CLIApplication -->");
		System.out.println("Welcome to group Center MoneyService");
		System.out.println("------------------------------------");
		System.out.println();
		
		boolean done = false;
	
		do {
			
			int choice = CLIHelper.menuInput(); 
			Order aOrder = null;
			
			switch(choice) {
			
			case 1:
				CLIHelper.showSupportedCurrencies(aExchangeOffice.getCurrencyMap());
				break;
			case 2:
				CLIHelper.showSupportedCurrencies(aExchangeOffice.getCurrencyMap());
				boolean ok;
				do {
					ok = true;
					aOrder = null;
					aOrder = CLIHelper.orderRequest();
					boolean output = false;
					//logging order data.
					logger.log(Level.INFO, "Order: " + aOrder);
					
					if (aOrder != null) {
					
						if (aOrder.getMode() == TransactionMode.SELL)
							try {
								output = aExchangeOffice.sellMoney(aOrder);
								logger.log(Level.INFO, "Completed " + aOrder.getMode() +  " Transaction!\n");
							} catch(IllegalArgumentException iae) {
								logger.log(Level.SEVERE, "Order exception! " + iae);
								System.out.println(iae.getMessage());
								System.out.println();
								ok = false;
							}
						
						if (aOrder.getMode() == TransactionMode.BUY)
							try {
								output = aExchangeOffice.buyMoney(aOrder);
								logger.log(Level.INFO, "Completed " + aOrder.getMode() +  " Transaction!\n");
								

							} catch (IllegalArgumentException iae) {
								logger.log(Level.SEVERE, "Order exception! " + iae);
								System.out.println(iae.getMessage());
								System.out.println();
								ok = false;
							}
						
						if (ok && output == false || output == false) {
							
							System.out.println("The amount does not meet the requirements (min/multiples) or is a too high amount for us to handle");
							System.out.println();
							ok = false;
						}
						
					}
					else {
						logger.log(Level.WARNING, "Order not done properly! " + aOrder);
					}
				} while(!ok);
				
				CLIHelper.showValidatedOrder(aOrder);
				break;
			case 3:
				aExchangeOffice.printSiteReport("console");
				break;
			case 4:
				aExchangeOffice.printSiteReport("txt");
				break;
			case 5:
				aExchangeOffice.shutDownService("Transactions.ser");
			case 0:
				System.out.println("Thanks for visiting group center MoneyService. Welcome back!");
				done = true;
			}
			
		} while(!done);
		logger.log(Level.INFO, "Exiting CLIApplication <--");
	}

}
