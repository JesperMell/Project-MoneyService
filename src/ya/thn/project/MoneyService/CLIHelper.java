package ya.thn.project.MoneyService;

import java.util.Scanner;

import ya.thn.project.MoneyService.Order.OrderType;

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
	
	
	static void showSupportedCurrencies() {
		
		System.out.println("Supported currencies");
		System.out.println("--------------------");
		System.out.println("1 - SEK");
		System.out.println("2 - GBP");
		System.out.println("3 - USD");
		System.out.println();
	}
	
	
	/**
	 * Helper method for creating a new Order object
	 * @return a Order object based on user input
	 */
	static Order orderRequest() {
		
		int sellBuyChoice = 0; // Default "Back to main menu"
		int currencyChoice = 1; // Default SEK
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
			
			showSupportedCurrencies();
			System.out.print("Enter currency No: ");
			String userCurrencyChoice = input.next();
			
			System.out.print("Enter amount: ");
			String userAmount = input.next();
			
			try {
				currencyChoice = Integer.parseInt(userCurrencyChoice);
				amount = Integer.parseInt(userAmount);
			} catch(NumberFormatException e) {
				System.out.format("Your choice is not accepted!");
				ok = false;
			}
			
			if (currencyChoice < 0 || currencyChoice > 3) // Magic number!!! CHANGE
				ok = false;
			
			switch(sellBuyChoice) {
			case 1:
				aOrder = new Order(OrderType.SELL, amount, String.valueOf(CurrencyCodeType.valueOf(currencyChoice)));
				break;
			case 2:
				aOrder = new Order(OrderType.BUY, amount, String.valueOf(CurrencyCodeType.valueOf(currencyChoice)));
				break;
			case 0:
				menuInput();
				break;
			}
			
		} while (!ok);
		
		return aOrder;
		
	}

}
