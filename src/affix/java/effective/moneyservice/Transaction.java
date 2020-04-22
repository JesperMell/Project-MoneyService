package affix.java.effective.moneyservice;

import java.time.LocalDateTime;

public class Transaction implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final int id;
	private final String currencyCode;
	private final int amount;
	private final TransactionMode transactionMode;
	private LocalDateTime createdAt;
	
	private static int uniqueId = 0;
	
	public Transaction(String currencyCode, int amount, TransactionMode mode) {
		this(currencyCode, amount, mode, ++uniqueId);
	}
	
	public Transaction(String currencyCode, int amount, TransactionMode mode, int id) {
		this.currencyCode = currencyCode;
		this.amount = amount;
		this.transactionMode = mode;
		createdAt = LocalDateTime.now();
	
		this.id = id;
	}
	/**
	 * @return the createdAt
	 */
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public int getId() {
		return id;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public int getAmount() {
		return amount;
	}

	public TransactionMode getTransactionMode() {
		return transactionMode;
	}
	
	
}
