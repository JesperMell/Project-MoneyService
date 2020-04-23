package ya.thn.project.MoneyService;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Test;

public class TestJUnitExchangeOffice {

	@Test
	public void testBuyMoney1() {
		ServiceConfig.readProjectConfigFile();
		ServiceConfig.readCurrencyConfigFile();

		ExchangeOffice testOffice = new ExchangeOffice("TestOffice", MoneyServiceApp.inventoryMap);
		testOffice.getCurrencyMap();
		// When you call buy method, from service side, you "buy" the requested currency
		// and give the customer the amount of SEK that the requested order equals.
		// Example request 100 USD and the office gives back roughly 1000 SEK.
		Order testOrder = new Order(TransactionMode.BUY, 3495, "USD"); // the limit(SEK stock is 35k). 
		System.out.println("--------Here starts buyMoney1 method----------");

		assertEquals(true, testOffice.buyMoney(testOrder));
	}
	
	@Test
	public void testBuyMoney2() {
		ServiceConfig.readProjectConfigFile();
		ServiceConfig.readCurrencyConfigFile();

		ExchangeOffice testOffice = new ExchangeOffice("TestOffice", MoneyServiceApp.inventoryMap);
		testOffice.getCurrencyMap();
		Order testOrder = new Order(TransactionMode.BUY, 150000, "RUB"); 
		System.out.println("--------Here starts buyMoney2 method----------");

		assertEquals(true, testOffice.buyMoney(testOrder));
	}
	
	
	@Test
	public void testSellMoney1() {
		ServiceConfig.readProjectConfigFile();
		ServiceConfig.readCurrencyConfigFile();

		ExchangeOffice testOffice = new ExchangeOffice("TestOffice", MoneyServiceApp.inventoryMap);
		testOffice.getCurrencyMap();
		Order testOrder = new Order(TransactionMode.SELL, 2000, "USD"); // Max amount in stock

		assertEquals(true, testOffice.sellMoney(testOrder));
	}
	
	@Test
	public void testSellMoney2() {
		ServiceConfig.readProjectConfigFile();
		ServiceConfig.readCurrencyConfigFile();

		ExchangeOffice testOffice = new ExchangeOffice("TestOffice", MoneyServiceApp.inventoryMap);
		testOffice.getCurrencyMap();
		Order testOrder = new Order(TransactionMode.SELL, 10000, "RUB"); // Max amount in stock

		assertEquals(true, testOffice.sellMoney(testOrder));
	}
	
	@Test
	public void testAvailableAmount1() {
		ServiceConfig.readProjectConfigFile();
		ServiceConfig.readCurrencyConfigFile();

		ExchangeOffice testOffice = new ExchangeOffice("TestOffice", MoneyServiceApp.inventoryMap);
		
		assertEquals(Optional.ofNullable(MoneyServiceApp.inventoryMap.get("USD")), testOffice.getAvailableAmount("USD"));
	}
	
	@Test
	public void testAvailableAmount2() {
		ServiceConfig.readProjectConfigFile();
		ServiceConfig.readCurrencyConfigFile();

		ExchangeOffice testOffice = new ExchangeOffice("TestOffice", MoneyServiceApp.inventoryMap);
		
		assertEquals(Optional.ofNullable(MoneyServiceApp.inventoryMap.get("JPY")), testOffice.getAvailableAmount("JPY"));
	}
}
