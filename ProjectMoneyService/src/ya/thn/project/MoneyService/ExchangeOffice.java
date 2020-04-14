package ya.thn.project.MoneyService;

/**
 * This is an implementation of the generic interface MoneyService
 * plus some own implemented methods
 * @author group Center
 */
public class ExchangeOffice implements MoneyService {
	
	public static void showSupportedCurrencies() {
		
		System.out.println("Supported Currencies");
		System.out.println("--------------------");
		System.out.println("1 - SEK");
		System.out.println("2 - GBP");
		System.out.println("3 - USD");
	}

}
