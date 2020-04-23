package affix.java.effective.moneyservice;

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
		Order testOrder = new Order(TransactionMode.BUY, 3400, "USD"); 

		assertEquals(true, testOffice.buyMoney(testOrder));
	}
	
	@Test
	public void testBuyMoney2() {
		ServiceConfig.readProjectConfigFile();
		ServiceConfig.readCurrencyConfigFile();

		ExchangeOffice testOffice = new ExchangeOffice("TestOffice", MoneyServiceApp.inventoryMap);
		testOffice.getCurrencyMap();
		Order testOrder = new Order(TransactionMode.BUY, 150000, "RUB"); 

		assertEquals(true, testOffice.buyMoney(testOrder));
	}
	
	
	@Test
	public void testSellMoney1() {
		ServiceConfig.readProjectConfigFile();
		ServiceConfig.readCurrencyConfigFile();

		ExchangeOffice testOffice = new ExchangeOffice("TestOffice", MoneyServiceApp.inventoryMap);
		testOffice.getCurrencyMap();
		Order testOrder = new Order(TransactionMode.SELL, 2000, "USD"); 

		assertEquals(true, testOffice.sellMoney(testOrder));
	}
	
	@Test
	public void testSellMoney2() {
		ServiceConfig.readProjectConfigFile();
		ServiceConfig.readCurrencyConfigFile();

		ExchangeOffice testOffice = new ExchangeOffice("TestOffice", MoneyServiceApp.inventoryMap);
		testOffice.getCurrencyMap();
	//	System.out.println(MoneyServiceApp.inventoryMap.get("RUB"));
		Order testOrder = new Order(TransactionMode.SELL, 8000, "RUB"); 

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
	
	@Test
	public void testBuyMoneyCheckModulus1() {
		ServiceConfig.readProjectConfigFile();
		ServiceConfig.readCurrencyConfigFile();

		ExchangeOffice testOffice = new ExchangeOffice("TestOffice", MoneyServiceApp.inventoryMap);
		testOffice.getCurrencyMap();
		Order testOrder = new Order(TransactionMode.BUY, 50, "RUB"); 

		assertEquals(true, testOffice.buyMoney(testOrder));
	}
	
	@Test
	public void testBuyMoneyCheckModulus2() {
		ServiceConfig.readProjectConfigFile();
		ServiceConfig.readCurrencyConfigFile();

		ExchangeOffice testOffice = new ExchangeOffice("TestOffice", MoneyServiceApp.inventoryMap);
		testOffice.getCurrencyMap();
		Order testOrder = new Order(TransactionMode.BUY, 250, "RUB"); 

		assertEquals(true, testOffice.buyMoney(testOrder));
	}
	
	@Test
	public void testBuyMoneyCheckModulus3() {
		ServiceConfig.readProjectConfigFile();
		ServiceConfig.readCurrencyConfigFile();

		ExchangeOffice testOffice = new ExchangeOffice("TestOffice", MoneyServiceApp.inventoryMap);
		testOffice.getCurrencyMap();
		Order testOrder = new Order(TransactionMode.BUY, 65, "RUB"); 

		assertEquals(false, testOffice.buyMoney(testOrder));
	}
	
	@Test
	public void testBuyMoneyCheckModulus4() {
		ServiceConfig.readProjectConfigFile();
		ServiceConfig.readCurrencyConfigFile();

		ExchangeOffice testOffice = new ExchangeOffice("TestOffice", MoneyServiceApp.inventoryMap);
		testOffice.getCurrencyMap();
		Order testOrder = new Order(TransactionMode.BUY, 93, "RUB"); 

		assertEquals(false, testOffice.buyMoney(testOrder));
	}
	
	@Test
	public void testSellMoneyCheckModulus1() {
		ServiceConfig.readProjectConfigFile();
		ServiceConfig.readCurrencyConfigFile();

		ExchangeOffice testOffice = new ExchangeOffice("TestOffice", MoneyServiceApp.inventoryMap);
		testOffice.getCurrencyMap();
		Order testOrder = new Order(TransactionMode.SELL, 50, "RUB");

		assertEquals(true, testOffice.sellMoney(testOrder));
	}
	
	@Test
	public void testSellMoneyCheckModulus2() {
		ServiceConfig.readProjectConfigFile();
		ServiceConfig.readCurrencyConfigFile();

		ExchangeOffice testOffice = new ExchangeOffice("TestOffice", MoneyServiceApp.inventoryMap);
		testOffice.getCurrencyMap();
		Order testOrder = new Order(TransactionMode.SELL, 200, "RUB"); 

		assertEquals(true, testOffice.sellMoney(testOrder));
	}
	
	@Test
	public void testSellMoneyCheckModulus3() {
		ServiceConfig.readProjectConfigFile();
		ServiceConfig.readCurrencyConfigFile();

		ExchangeOffice testOffice = new ExchangeOffice("TestOffice", MoneyServiceApp.inventoryMap);
		testOffice.getCurrencyMap();
		Order testOrder = new Order(TransactionMode.SELL, 52, "RUB"); 

		assertEquals(false, testOffice.sellMoney(testOrder));
	}
	
	@Test
	public void testSellMoneyCheckModulus4() {
		ServiceConfig.readProjectConfigFile();
		ServiceConfig.readCurrencyConfigFile();

		ExchangeOffice testOffice = new ExchangeOffice("TestOffice", MoneyServiceApp.inventoryMap);
		testOffice.getCurrencyMap();
		Order testOrder = new Order(TransactionMode.SELL, 175, "RUB"); 

		assertEquals(false, testOffice.sellMoney(testOrder));
	}
}
