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
		
		orderAmountLimit = ServiceConfig.readMoneyServiceConfigFile();
		ServiceConfig.readProjectConfigFile();
		ServiceConfig.readCurrencyConfigFile();
	}
	
	/**
	 * This method supports user interaction via CLI
	 */
	private static void CLIApplication(MoneyService aExchangeOffice) {
		
//		MoneyService aExchangeOffice = new ExchangeOffice();
		
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
				aOrder = CLIHelper.orderRequest();
				if (aOrder.getMode() == TransactionMode.SELL)
					aExchangeOffice.sellMoney(aOrder);
				if (aOrder.getMode() == TransactionMode.BUY)
					aExchangeOffice.buyMoney(aOrder);
				break;
//			case 3:
//				aBuyOrder = CLIHelper.orderRequest();
//				aExchangeOffice.buyMoney(aBuyOrder);
//				break;
			case 0:
				done = true;
			}
		} while(!done);
	}

}
