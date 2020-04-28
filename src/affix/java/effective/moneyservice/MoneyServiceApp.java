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
		consHandler.setLevel(Level.SEVERE);
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
		
		setupLogger();
	
		configure();
		MoneyService aExchangeOffice = new ExchangeOffice("THN", inventoryMap);
		CLIApplication(aExchangeOffice);
	}
	
	private static void configure() {
		
		orderAmountLimit = ServiceConfig.readMoneyServiceConfigFile();
		ServiceConfig.readProjectConfigFile();
		ServiceConfig.readCurrencyConfigFile();
	}
	
	/**
	 * This method supports user interaction via CLI
	 */
	private static void CLIApplication(MoneyService aExchangeOffice) {
		
//		MoneyService aExchangeOffice = new ExchangeOffice();
		
		System.out.println("Welcome to group Center MoneyService");
		System.out.println("------------------------------------");
		System.out.println();
		
		boolean done = false;
	
		do {
			logger.log(Level.INFO, "Entering Menu input loop..");
			int choice = CLIHelper.menuInput();
			Order aOrder = null;
//			Order aBuyOrder = null;
			
			switch(choice) {
			case 1:
				//aExchangeOffice.getCurrencyMap().keySet();
				CLIHelper.showSupportedCurrencies(aExchangeOffice.getCurrencyMap());
				break;
			case 2:
				boolean ok;
				do {
					ok = true;
					aOrder = null;
					aOrder = CLIHelper.orderRequest();
					//logging order data.
					logger.log(Level.INFO, "Order: " + aOrder);
					
					if (aOrder != null) {
					
						if (aOrder.getMode() == TransactionMode.SELL)
							try {
								aExchangeOffice.sellMoney(aOrder);
								logger.log(Level.INFO, "Completed " + aOrder.getMode() +  " order!");
							} catch(IllegalArgumentException iae) {
								logger.log(Level.SEVERE, "Order exception! " + iae);
								System.out.println(iae.getMessage());
								System.out.println();
								ok = false;
								//aOrder = null;
							}
						
						if (aOrder.getMode() == TransactionMode.BUY)
							try {
								aExchangeOffice.buyMoney(aOrder);
								logger.log(Level.INFO, "Completed " + aOrder.getMode() +  " order!");
							} catch (IllegalArgumentException iae) {
								logger.log(Level.SEVERE, "Order exception! " + iae);
								System.out.println(iae.getMessage());
								System.out.println();
								ok = false;
								//aOrder = null;
							}
						
					}
					else {
						logger.log(Level.WARNING, "Order not done properly! " + aOrder);
					}
				} while(!ok);
				
				CLIHelper.showValidatedOrder(aOrder);
				break;
//			case 3:
//				aBuyOrder = CLIHelper.orderRequest();
//				aExchangeOffice.buyMoney(aBuyOrder);
//				break;
			case 0:
				System.out.println("Thanks for visiting group center MoneyService. Welcome back!");
				done = true;
			}
			
		} while(!done);
		logger.log(Level.INFO, "Exiting Menu input loop..");
	}

}
