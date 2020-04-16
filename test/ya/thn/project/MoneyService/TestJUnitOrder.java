package ya.thn.project.MoneyService;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class TestJUnitOrder {
	
	List<Order> testList = new ArrayList<>();

//	private void generateOrderList() {
//	Order order1 = new Order(TransactionMode.BUY, 10, "USD");
//	Order order2 = new Order(TransactionMode.BUY, 89, "JPY");
//	Order order3 = new Order(TransactionMode.SELL, 37, "NOK");
//	
//	testList.add(order1);
//	testList.add(order2);
//	testList.add(order3);
//	}
	
	
	@Test
	public void testOrderMode1() {
		Order testOrder = new Order(TransactionMode.BUY, 50, "USD");
		TransactionMode mode = testOrder.getMode();

		assertEquals(TransactionMode.BUY, mode);
	}
	
	@Test
	public void testOrderMode2() {
		Order testOrder = new Order(TransactionMode.SELL, 50, "USD");
		TransactionMode mode = testOrder.getMode();

		assertEquals(TransactionMode.SELL, mode);
	}
	
	@Test
	public void testOrderNr1() {
		//generateOrderList();
		Order order1 = new Order(TransactionMode.BUY, 10, "USD");
		Order order2 = new Order(TransactionMode.BUY, 89, "JPY");
		Order order3 = new Order(TransactionMode.SELL, 37, "NOK");
		
		testList.add(order1);
		testList.add(order2);
		testList.add(order3);
		
		Order testOrder = testList.get(0);
		System.out.println(testOrder.getOrderNr());
		
		assertEquals(1, testOrder.getOrderNr());
	}
}
