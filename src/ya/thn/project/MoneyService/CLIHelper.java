package ya.thn.project.MoneyService;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

/**
 * This is a support class for user interaction using CLI
 * @author Group Center
 */
public class CLIHelper {
	
	static Scanner input = new Scanner(System.in);
	
	
	/**
	 * This is the overall helper method for user choice of operation
	 * @return an "int" holding current menu choice
	 */
	static int menuInput() {
		
		int choice = 0;
		boolean ok;
		do {
			ok = true;
			System.out.println("Welcome to group Center MoneyService");
			System.out.println("------------------------------------");
			System.out.println("1 - Show supported currencies and their exchange rate");
			System.out.println("2 - Exchange currency");
			System.out.println("3 - Show order");
			System.out.println("0 - Exit");
			
			System.out.print("Enter your choice: ");
			String userChoice = input.next();
			
			try {
				choice = Integer.parseInt(userChoice);
			} catch(NumberFormatException e) {
				System.out.format("Your choice %s is not accepted!", userChoice);
				ok = false;
			}
			
			if (choice > 3)
				ok = false;
			
			
		} while (!ok);
		
		return choice;
	}
	
	
	/**
	 * Helper method that show supported currencies,
	 * original taken from the "CurrencyConfig.txt"
	 * @param a map with currencyCode as key and Currency as value
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
			System.out.println(me.getKey());
		
		System.out.println();
	}
	
	
	/**
	 * Helper method for creating a new Order object
	 * @return a Order object based on user input
	 */
	static Order orderRequest() {
		
		int sellBuyChoice = 0; // Default "Back to main menu"
		String currencyCode = "SEK"; // Default SEK
		int amount = 0;
		Order aOrder = null;
		boolean ok;
		do {
			ok = true;
			
			do {
				System.out.println("----- Exchange currency -----");
				System.out.println("Do you want to sell or buy currency? ");
				System.out.println("1 - Sell currency");
				System.out.println("2 - Buy currency");
				System.out.println("0 - Back to main menu");
				String userSellBuyChoice = input.next();
			
				try {
					sellBuyChoice = Integer.parseInt(userSellBuyChoice);
				} catch(NumberFormatException e) {
					System.out.format("Your choice %s is not accepted!", userSellBuyChoice);
					ok = false;
				}
				
				if (sellBuyChoice < 0 || sellBuyChoice > 2)
					ok = false;
			
			} while(!ok);
			
			//showSupportedCurrencies();
			System.out.print("Enter currency code (3 capital letters): ");
			String userCurrencyCode = input.next();
			
			System.out.print("Enter amount: ");
			String userAmount = input.next();

			try {
				currencyCode = userCurrencyCode.toUpperCase();
			} catch(NumberFormatException e) {
				System.out.format("Your choice %s is not accepted!", userCurrencyCode);
				ok = false;
			}

			try {
				amount = Integer.parseInt(userAmount);
			} catch(NumberFormatException e) {
				System.out.format("Your choice %s is not accepted!", userAmount);
				ok = false;
			}
			
			
			switch(sellBuyChoice) {
			case 1:
				aOrder = new Order(TransactionMode.SELL, amount, currencyCode);
				break;
			case 2:
				aOrder = new Order(TransactionMode.BUY, amount, currencyCode);
				break;
			case 0:
				menuInput();
				break;
			}
			
		} while (!ok);
		
		return aOrder;
		
	}

}
