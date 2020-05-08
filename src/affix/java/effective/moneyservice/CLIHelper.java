package affix.java.effective.moneyservice;

import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is a support class for user interaction using CLI
 * 
 * @author Group Center
 */
public class CLIHelper {
	
	/**
	 * A Logger object
	 */
	private final static Logger logger = Logger.getLogger("affix.java.effective.moneyservice");
	
	/**
	 * Reads input from the user
	 */
	static Scanner input = new Scanner(System.in);
	
	/**
	 * This method display the menu and prompt a user input for choice
	 * @return an int holding current user menu choice
	 */
	static int menuInput() {
		
		int choice = 0;
		boolean ok;
		do {
			logger.log(Level.INFO, "Entering Main menu loop -->");
			ok = true;
			System.out.println("----- Main menu -----");
			System.out.println("1 - Show supported currencies and their exchange rate");
			System.out.println("2 - Exchange currency");
			System.out.println("3 - Show Site report on the console");
			System.out.println("4 - Print Site report to a textfile");
			System.out.println("5 - Exit and save transactions to a file");
			System.out.println("0 - Exit");
			
			System.out.print("Enter your choice: ");
			String userChoice = input.next();
			
			try {
				choice = Integer.parseInt(userChoice);
			} catch(NumberFormatException e) {
				logger.log(Level.WARNING, "Choice Exception: " + e);
				System.out.format("Your choice %s is not accepted!%n", userChoice);
				ok = false;
			}
			
			if (choice < 0 || choice > 5)
				ok = false;
			
			System.out.println();
			
		} while (!ok);
		logger.log(Level.INFO, "Exiting Main menu loop <--");
		
		return choice;
	}
	
	
	/**
	 * Method that show the currencies the system accept
	 * @param hm - a map holding the currency codes as String and Currency as value
	 */
	static void showSupportedCurrencies(Map<String, Currency> hm) {
		
		// To get the map in alphabetical order
		Map<String, Currency> tm = new TreeMap<String, Currency>(hm);
		
		// Get a set of the entries
		Set<Map.Entry<String, Currency>> set = tm.entrySet();
		
		// Display the keys
		System.out.println("Supported currencies");
		System.out.println("--------------------");
		
		for (Map.Entry<String, Currency> me : set)
			System.out.format(Locale.US, "%s ", me.getKey());
		
		System.out.println();
		System.out.println();
	}
	
	
	/**
	 * Helper method for creating a new Order object
	 * @return a Order object based on user input
	 */
	static Order orderRequest() {
		
		logger.log(Level.INFO, "Entering orderRequest method -->");
		int sellBuyChoice = 0; // Default "Back to main menu"
		String currencyCode = MoneyServiceApp.referenceCurrencyCode;
		int amount = 0;
		Order aOrder = null;
		boolean ok;
		do {
			ok = true;
			
			do {
				ok = true;
				
				System.out.println("----- Exchange currency -----");
				System.out.println("Do you want to sell or buy currency (from the company view)? ");
				System.out.println("1 - Sell currency");
				System.out.println("2 - Buy currency");
				System.out.println("0 - Back to main menu");
				System.out.print("Enter your choice: ");
				String userSellBuyChoice = input.next();
			
				try {
					sellBuyChoice = Integer.parseInt(userSellBuyChoice);
				} catch(NumberFormatException e) {
					logger.log(Level.WARNING, "Choice exception! " + e);
					System.out.format("Your choice %s is not accepted!%n", userSellBuyChoice);
					ok = false;
				}
				
				if (sellBuyChoice < 0 || sellBuyChoice > 2)
					ok = false;
				
				System.out.println();
			
			} while(!ok);
			
			if (sellBuyChoice == 1 || sellBuyChoice == 2) {
				
				System.out.print("Enter currency code (3 capital letters): ");
				String userCurrencyCode = input.next();
			
				System.out.print("Enter amount (minimum "+MoneyServiceApp.orderAmountLimit+" of the choosen currency and multiples of "+MoneyServiceApp.orderAmountLimit+"): ");
				String userAmount = input.next();

				try {
					currencyCode = userCurrencyCode.toUpperCase();
				} catch(NumberFormatException e) {
					logger.log(Level.WARNING, "Currency code exception! " + e);
					System.out.format("Your choice %s is not accepted!", userCurrencyCode);
					ok = false;
				}

				try {
					amount = Integer.parseInt(userAmount);
				} catch(NumberFormatException e) {
					logger.log(Level.WARNING, "Amount exception! " + e);
					System.out.format("Your choice %s is not accepted!%n", userAmount);
					ok = false;
				}
			}
			
			if (ok) {
				switch(sellBuyChoice) {
				case 1:
					try {
						aOrder = new Order(TransactionMode.SELL, amount, currencyCode);
					} catch(IllegalArgumentException iae) {
						System.out.println(iae.getMessage());
						System.out.println();
						ok = false;
					}
					break;
					
				case 2:
					try {
						aOrder = new Order(TransactionMode.BUY, amount, currencyCode);
					} catch(IllegalArgumentException iae) {
						System.out.println(iae.getMessage());
						System.out.println();
						ok = false;
					}
					break;
					
				case 0:
					break;
				}
			}
			
			System.out.println();
			
		} while (!ok);
		logger.log(Level.INFO, "Exiting orderRequest method <--");
				
		return aOrder;
		
	}
	
	/**
	 * Method for displaying a requested order
	 * @param aOrder - an Order object
	 */
	static void showValidatedOrder(Order aOrder) {
		
		if (aOrder != null) {
			System.out.println("Your requested order!");
			System.out.println("----------------------");
			System.out.println();
			System.out.println("Order type: " + aOrder.getMode());
			System.out.println("Currency code: " + aOrder.getCurrencyCode());
			System.out.println("Order amount in requested currency: " + aOrder.getAmount());
			System.out.println();
		}
		
	}

}
