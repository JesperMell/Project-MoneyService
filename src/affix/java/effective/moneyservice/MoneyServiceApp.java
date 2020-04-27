package affix.java.effective.moneyservice;

import java.util.HashMap;
import java.util.Map;

/**
 * This class triggers an application defining an ExchangeOffice for Order objects.
 * @author Group Center
 */
public class MoneyServiceApp {
	
	static String referenceCurrencyCode;
	
	/**
	 * Storage for Currency objects using CurrencyCode as key
	 */
	static Map<String, Currency> currencyMap = new HashMap<>();
	static Map<String, Double> inventoryMap = new HashMap<>();
	static int orderAmountLimit;
	
	public static void main(String[] args) {
		
		configure();
		MoneyService aExchangeOffice = new ExchangeOffice("THN", inventoryMap);
		CLIApplication(aExchangeOffice);
	}
	
	private static void configure() {
		
		ServiceConfig.readMoneyServiceConfigFile();
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
			int choice = CLIHelper.menuInput();
			Order aOrder = null;
//			Order aBuyOrder = null;
			
			switch(choice) {
			case 1:
				//aExchangeOffice.getCurrencyMap().keySet();
				CLIHelper.showSupportedCurrencies(aExchangeOffice.getCurrencyMap());
				break;
			case 2:
				CLIHelper.showSupportedCurrencies(aExchangeOffice.getCurrencyMap());
				boolean ok;
				do {
					ok = true;
					aOrder = null;
					aOrder = CLIHelper.orderRequest();
					
					if (aOrder != null) {
					
						if (aOrder.getMode() == TransactionMode.SELL)
							try {
								aExchangeOffice.sellMoney(aOrder);
							} catch(IllegalArgumentException iae) {
								System.out.println(iae.getMessage());
								System.out.println();
								ok = false;
								//aOrder = null;
							}
						
						if (aOrder.getMode() == TransactionMode.BUY)
							try {
								aExchangeOffice.buyMoney(aOrder);
								aExchangeOffice.printSiteReport("Console");
							} catch (IllegalArgumentException iae) {
								System.out.println(iae.getMessage());
								System.out.println();
								ok = false;
								//aOrder = null;
							}
						
						if (ok && (aExchangeOffice.sellMoney(aOrder) == false || aExchangeOffice.buyMoney(aOrder) == false)) {
							
							System.out.println("The amount does not meet the requirements (min/multiples) or is a too high amount for us to handle");
							System.out.println();
							ok = false;
						}
						
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
	}

}
