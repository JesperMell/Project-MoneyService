package ya.thn.project.MoneyService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrderGenerator {
	
	private static int MAX_RAND = 500;
	private static int MIN_RAND = 10;
	
	private static Set<String> currencies = new HashSet<String>();
	
	/**
	 * generateOrders
	 * 
	 * Create orders with random values.
	 * Read generateOrder for random values description
	 * 
	 * int size for the amount of random orders.
	 * 
	 * @return List<Order>()
	 *  
	 */
	public static List<Order> generateOrders(int size) {
		List<Order> l = new ArrayList<>();
		for(int i = 0; i < size; i++) {
			l.add(generateOrder());
		}
		return l;
	}
	
	/**
	 * generateOrder
	 * 
	 * Create a order with random values.
	 * Random values is following:
	 * BUY|SELL, [MIN_RAND-MAX_RAND], [Random CurrencyCode]
	 * 
	 * @return Order
	 *  
	 */
	public static Order generateOrder() {
		if(MoneyServiceApp.currencyMap.isEmpty())
			throw new IllegalArgumentException("You need to register currencies!");
		
		// Create a random amount.
		int randAmount = (int) ((Math.random() * ((MAX_RAND - MIN_RAND) + 1)) + MIN_RAND);
		
		// Determine BUY or SELL mode.
		TransactionMode randMode = (Math.random() > 0.5) ? TransactionMode.BUY : TransactionMode.SELL;

		// Fetch all currencies.
		fillCurrencies();
	
		// Create a random index, between 0 and currencies.size().
		int randCurrencyIndex = (int) ((Math.random() * ((currencies.size() - 1) + 1)));
		
		// Return the new order. 
		return new Order(randMode, randAmount, (String) currencies.toArray()[randCurrencyIndex]);
	}

	
	/**
	 * fillCurrencies
	 *
	 * Get the currencies from the MoneyServiceApp
	 * and merge them to single.
	 *
	 */
	private static void fillCurrencies() {
		if(!currencies.isEmpty())
			return;
		
		currencies.addAll(MoneyServiceApp.currencyMap.keySet());
		currencies.addAll(MoneyServiceApp.inventoryMap.keySet());
	}
}
