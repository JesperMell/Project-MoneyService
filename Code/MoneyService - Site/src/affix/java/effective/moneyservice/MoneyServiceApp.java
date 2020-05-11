package affix.java.effective.moneyservice;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
	public static final String OFFICE_NAME = "CENTER";
	
	/**
	 * String holding the configured reference currency code
	 */
	static String referenceCurrencyCode;
	
	
	/**
	 * A Logger object
	 */
	// create logger
	private static Logger logger;
	
	static {
		logger = Logger.getLogger("affix.java.effective.moneyservice");
	}
	
	/**
	 * Storage for Currency objects using CurrencyCode as key and Currency as value
	 */
	static Map<String, Currency> currencyMap = new HashMap<>();
	
	/**
	 * Map holding configured values defining an inventory of money, key is a currency code
	 * and value is a double defining the amount
	 */
	static Map<String, Double> inventoryMap = new HashMap<>();
	
	/**
	 * int holding the configured value for min order amount
	 */
	static int orderAmountLimit;
	
	/**
	 * Start method of the program, arguments are unused
	 * @param args - A string argument
	 */
	public static void main(String[] args) {
		configure();
		logger.log(Level.INFO, "-------Configuration_Ends-------\n");
		MoneyService aExchangeOffice = new ExchangeOffice("THN", inventoryMap);
		CLIApplication(aExchangeOffice);
	}
	
	/**
	 * Method for setting up and configuring logging
	 */
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
		
	/**
	 * Method for running configuration methods
	 */
	private static void configure() {
		setupLogger();
		ServiceConfig.readMoneyServiceConfigFile();
		ServiceConfig.readProjectConfigFile();
		ServiceConfig.readCurrencyConfigFile();
	}
	
	/**
	 * This method supports user interaction via CLI calling methods from MoneyService interface
	 * @param aExchangeOffice - a reference to implementer of interface
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
					
					CLIHelper.showValidatedOrder(aOrder);
					boolean output = false;
					//logging order data.
					logger.log(Level.INFO, "Order: " + aOrder);
					
					if (aOrder != null) {
					
						if (aOrder.getMode() == TransactionMode.SELL)
							try {
								output = aExchangeOffice.sellMoney(aOrder);
								logger.log(Level.INFO, "Completed " + aOrder.getMode() +  " Transaction!\n");
							} catch(IllegalArgumentException iae) {
								logger.log(Level.WARNING, "Order exception! " + iae);
								System.out.println(iae.getMessage());
								System.out.println();
								ok = false;
							}
						
						if (aOrder.getMode() == TransactionMode.BUY)
							try {
								output = aExchangeOffice.buyMoney(aOrder);
								logger.log(Level.INFO, "Completed " + aOrder.getMode() +  " Transaction!\n");
								

							} catch (IllegalArgumentException iae) {
								logger.log(Level.WARNING, "Order exception! " + iae);
								System.out.println(iae.getMessage());
								System.out.println();
								ok = false;
							}
						
						if (ok == false || output == false) {
							
							System.out.println("Your order has been rejected (could not be handeled)");
							System.out.println();
							ok = false;
						}
						
					}
					else {
						logger.log(Level.WARNING, "Order not done properly! " + aOrder);
					}
				} while(!ok);
				

				break;
			case 3:
				aExchangeOffice.printSiteReport("console");
				break;
			case 4:
				aExchangeOffice.printSiteReport("txt");
				break;
			case 5:
				String reportName = String.format("Report_%s_%s.ser", OFFICE_NAME, DateTimeFormatter.ofPattern("YYYY-MM-dd").format(LocalDate.now()));
				aExchangeOffice.shutDownService(reportName);
			case 0:
				System.out.println("Thanks for visiting group center MoneyService. Welcome back!");
				done = true;
			}
			
		} while(!done);
		logger.log(Level.INFO, "Exiting CLIApplication <--");
	}

}
