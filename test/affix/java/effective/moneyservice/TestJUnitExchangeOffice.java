package affix.java.effective.moneyservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestJUnitExchangeOffice {
	
	private Map<String, Double> testInventory = new HashMap<>();
	
	@BeforeClass
	public static void setUp() {
		ServiceConfig.readProjectConfigFile();
		ServiceConfig.readCurrencyConfigFile();
		ServiceConfig.readMoneyServiceConfigFile();
		
	}
	
	@Before
	public void setUpInventory() {
		
		testInventory.putIfAbsent("EUR", 3000.0);
		testInventory.putIfAbsent("USD", 2000.0);
		testInventory.putIfAbsent("GBP", 5000.0);
		testInventory.putIfAbsent("NOK", 10000.0);
		testInventory.putIfAbsent("DKK", 10000.0);
		testInventory.putIfAbsent("CHF", 1500.0);
		testInventory.putIfAbsent("RUB", 10000.0);
		testInventory.putIfAbsent("AUD", 1500.0);
		testInventory.putIfAbsent("SEK", 35000.0);
	}

	@Test
	public void testBuyMoney1() {

		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		// When you call buy method, from service side, you "buy" the requested currency
		// and give the customer the amount of SEK that the requested order equals.
		// Example request 100 USD and the office gives back roughly 1000 SEK.
		Order testOrder = new Order(TransactionMode.BUY, 3400, "USD"); 

		assertEquals(true, testOffice.buyMoney(testOrder));
	}
	
	@Test
	public void testBuyMoney2() {

		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		Order testOrder = new Order(TransactionMode.BUY, 150000, "RUB"); 

		assertEquals(true, testOffice.buyMoney(testOrder));
	}
	
	@Test
	public void testBuyMoney3() {
		
		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		Order o1 = new Order(TransactionMode.BUY, 100, "INR");
		
		assertTrue(testOffice.buyMoney(o1));
	}
	
	@Test
	public void testBuyMoney4() {
		
		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		Order o1 = new Order(TransactionMode.BUY, 100, "INR");
		
		testOffice.buyMoney(o1);
		Optional<Double> temp = testOffice.getAvailableAmount("INR");
		assertTrue(testInventory.containsKey("INR"));
		assertTrue(temp.get() == 100.0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testBuyMoney5() {
		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		Order o1 = new Order(TransactionMode.BUY, 100, "ABC");
		
		testOffice.buyMoney(o1);
	}
	
	@Test
	public void testBuyMoney6() {
		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		Order o1 = new Order(TransactionMode.BUY, 10000, "USD");
		
		assertFalse(testOffice.buyMoney(o1));
	}
	
	@Test
	public void testSellMoney1() {

		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		Order testOrder = new Order(TransactionMode.SELL, 2000, "USD"); 

		assertEquals(true, testOffice.sellMoney(testOrder));
	}
	
	@Test
	public void testSellMoney2() {
		
		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		Order testOrder = new Order(TransactionMode.SELL, 8000, "RUB"); 

		assertEquals(true, testOffice.sellMoney(testOrder));
	}
	
	@Test
	public void testSellMoney3() {
		
		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		Order o1 = new Order(TransactionMode.SELL, 200, "INR");
		
		assertFalse(testOffice.sellMoney(o1));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSellMoney4() {
		
		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		Order o1 = new Order(TransactionMode.SELL, 200, "ABC");
		
		testOffice.sellMoney(o1);
	}
	
	@Test
	public void testSellMoney5() {
		
		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		Order o1 = new Order(TransactionMode.SELL, 1501, "AUD");
		
		assertFalse(testOffice.sellMoney(o1));
	}
	
	@Test
	public void testSellMoney6() {
		
		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		Order o1 = new Order(TransactionMode.SELL, 1500, "AUD");
		
		assertTrue(testOffice.sellMoney(o1));
	}
	
	@Test
	public void testSellMoney7() {
		
		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		Order o1 = new Order(TransactionMode.SELL, 1550, "AUD");
		
		assertFalse(testOffice.sellMoney(o1));
	}
	
	@Test
	public void testSellMoney8() {
		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		Order buyOrder = new Order(TransactionMode.BUY, 1000, "INR");
		Order sellOrder = new Order(TransactionMode.SELL, 500, "INR");
		
		testOffice.buyMoney(buyOrder);

		assertTrue(testOffice.sellMoney(sellOrder));
	}
	
	@Test
	public void testSellMoney9() {
		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		Order buyOrder = new Order(TransactionMode.BUY, 1000, "INR");
		Order sellOrder = new Order(TransactionMode.SELL, 500, "INR");
		
		testOffice.buyMoney(buyOrder);
		testOffice.sellMoney(sellOrder);
		
		Optional<Double> temp = testOffice.getAvailableAmount("INR");
		
		assertTrue(temp.get() == 500.0);
	}
	
	@Test
	public void testAvailableAmount1() {

		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		
		assertEquals(Optional.ofNullable(testInventory.get("USD")), testOffice.getAvailableAmount("USD"));
	}
	
	@Test
	public void testAvailableAmount2() {

		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		
		assertEquals(Optional.ofNullable(testInventory.get("JPY")), testOffice.getAvailableAmount("JPY"));
	}
	
	@Test
	public void testBuyMoneyCheckModulus1() {
		
		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		Order testOrder = new Order(TransactionMode.BUY, 50, "RUB"); 

		assertEquals(true, testOffice.buyMoney(testOrder));
	}
	
	@Test
	public void testBuyMoneyCheckModulus2() {

		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		Order testOrder = new Order(TransactionMode.BUY, 250, "RUB"); 

		assertEquals(true, testOffice.buyMoney(testOrder));
	}
	
	@Test
	public void testBuyMoneyCheckModulus3() {

		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		Order testOrder = new Order(TransactionMode.BUY, 65, "RUB"); 

		assertEquals(false, testOffice.buyMoney(testOrder));
	}
	
	@Test
	public void testBuyMoneyCheckModulus4() {

		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		Order testOrder = new Order(TransactionMode.BUY, 93, "RUB"); 

		assertEquals(false, testOffice.buyMoney(testOrder));
	}
	
	@Test
	public void testSellMoneyCheckModulus1() {

		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		Order testOrder = new Order(TransactionMode.SELL, 50, "RUB");

		assertEquals(true, testOffice.sellMoney(testOrder));
	}
	
	@Test
	public void testSellMoneyCheckModulus2() {

		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		Order testOrder = new Order(TransactionMode.SELL, 200, "RUB"); 

		assertEquals(true, testOffice.sellMoney(testOrder));
	}
	
	@Test
	public void testSellMoneyCheckModulus3() {

		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		Order testOrder = new Order(TransactionMode.SELL, 52, "RUB"); 

		assertEquals(false, testOffice.sellMoney(testOrder));
	}
	
	@Test
	public void testSellMoneyCheckModulus4() {
		
		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		Order testOrder = new Order(TransactionMode.SELL, 175, "RUB"); 

		assertEquals(false, testOffice.sellMoney(testOrder));
	}
	
	@Test
	public void testGetCurrencyMap() {
		
		MoneyService testOffice = new ExchangeOffice("TestOffice", testInventory);
		
		Map<String, Currency> testMap = testOffice.getCurrencyMap();
		
		assertTrue(testMap.containsKey("USD"));
	}

}
