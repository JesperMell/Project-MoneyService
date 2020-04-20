package ya.thn.project.MoneyService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestJUnitOrder {
	
	private static Order o1 = null;
	
	@BeforeClass
	public static void setUp() {
		o1 = new Order(TransactionMode.BUY, 50, "USD");
	}
	
	@Test
	public void testAA1GetOrderNr_1() {
		Order o2 = new Order(TransactionMode.SELL, 50, "USD");
		assertEquals(2, o2.getOrderNr());
	}
	
	@Test
	public void testToString() {
		assertEquals("Order [orderNr=1, mode=BUY, currencyCode=USD, amount=50]", o1.toString());
	}
		
	@Test
	public void testGetTransactionMode_1() {
		assertEquals(TransactionMode.BUY, o1.getMode());
	}
	
	@Test
	public void testGetTransactionMode_2() {
		Order testOrder = new Order(TransactionMode.SELL, 50, "USD");
		assertEquals(TransactionMode.SELL, testOrder.getMode());
	}
	
	@Test
	public void testGetCurrencyCode_1() {
		assertEquals("USD", o1.getCurrencyCode());
	}
	
	@Test
	public void testGetAmount() {
		assertEquals(50, o1.getAmount());
	}
	
	@Test
	public void testConstructorOK() {
		Order o3 = new Order(TransactionMode.BUY, 100, "JPY");
		assertNotNull(o3);
	}
	
	@SuppressWarnings("unused")
	@Test(expected = IllegalArgumentException.class)
	public void testMissingCurrencyCode_1() {
		Order o3 = new Order(TransactionMode.BUY, 100, "");
	}
	
	@SuppressWarnings("unused")
	@Test(expected = IllegalArgumentException.class)
	public void testMissingCurrencyCode_2() {
		Order o3 = new Order(TransactionMode.BUY, 100, null);
	}
	
	@SuppressWarnings("unused")
	@Test(expected = IllegalArgumentException.class)
	public void testNegativeAmount() {
		Order o3 = new Order(TransactionMode.SELL, -100, "USD");
	}
	
	@SuppressWarnings("unused")
	@Test(expected = IllegalArgumentException.class)
	public void testZeroAmount() {
		Order o3 = new Order(TransactionMode.SELL, 0, "USD");
	}
	
	@Test
	public void testEqual_1() {
		Order o4 = new Order(TransactionMode.BUY, 200, "USD");
		Order o5 = new Order(TransactionMode.BUY, 200, "USD");
		
		assertFalse(o4.equals(o5));
	}
	
	
	@Test
	public void testHascode_1() {
		Order o4 = new Order(TransactionMode.BUY, 200, "USD");
		Order o5 = new Order(TransactionMode.BUY, 200, "USD");
		
		assertFalse(o4.hashCode() == o5.hashCode());
	}
	@Test
	public void testHascode_2() {
		Order o4 = new Order(TransactionMode.BUY, 200, "USD");
		Order o5 = new Order(TransactionMode.BUY, 200, "USD");
		
		assertFalse(o4.hashCode() == o5.hashCode() && o4.equals(o5));
	}
	
	
}
