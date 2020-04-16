package ya.thn.project.MoneyService;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TestJUnitExchangeOffice {
	static Map<String, Double> testInv = new HashMap<>();

	@Test
	public void testBuyMoney() {
		testInv.putIfAbsent("USD", 55.7);
		ExchangeOffice testOffice = new ExchangeOffice("TestOffice", testInv);
		Order testOrder = new Order(TransactionMode.BUY, 10, "USD");
		testOffice.buyMoney(testOrder);
		
		assertEquals(true, testOffice.buyMoney(testOrder));
	}
}
