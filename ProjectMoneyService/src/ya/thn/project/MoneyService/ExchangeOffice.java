package ya.thn.project.MoneyService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ExchangeOffice {

	private String name;
	//private Double amount; 
	private String currencyCode;

	private Map<DateTime, List<Transaction>> completedTransactions
	= new HashMap<>();

	private Map<String, Double> inventory
	= new HashMap<>();

	public ExchangeOffice(String name, Map<String, Double> inv){
		this.name = name;
		this.inventory = inv;
	}

	public boolean buyMoney(Order orderData){
		// idea of implementation before Order class available.

	}

	public boolean sellMoney(Order orderData) {

	}

	public void printSiteReport(String destination) {

	}

	public void shutDownService(String destination) {

	}

	public Map<String, Currency> getCurrencyMap() {

	}

	public Optional<Double> getAvailableAmount(String currencyCode){
		// Get the value of the specified currency.
		Double AvailableAmount = inventory.get(currencyCode);
		// If the value is not null, return Optional value. Otherwise, return empty Optional.
		Optional<Double> opt = Optional.ofNullable(AvailableAmount);
		// Returning the amount of specified currency.
		return opt;
	}


	private boolean validateOrder(Order orderData) {
		// Get the available amount at site.
		Optional<Double> availableAmount = getAvailableAmount(orderData.getCurrencyCode);
		// Return true if amount requested is available. Otherwise, return false.
		if(availableAmount >= orderData.getAmount) {
			return true;
		}
		else {
			return false;
		}
	}
	// Generating an Transaction object and put it in completedTransactions map.
	// Call this method only if validate order returned true.
//	private void generateTransaction(Order orderData) {
//		Transaction theTransaction = new Transaction(orderData);
//		completedTransactions.putIfAbsent(theTransaction.getCurrencyCode, theTransaction);	
//	}
}