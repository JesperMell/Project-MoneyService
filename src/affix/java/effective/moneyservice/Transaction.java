package affix.java.effective.moneyservice;

import java.time.LocalDateTime;
import java.time.LocalTime;


public class Transaction implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final int id;
	private final String currencyCode;
	private final int amount;
	private final TransactionMode mode;
	private LocalDateTime timeStamp;
	
	private static int uniqueId = 0;
	
	public Transaction(String currencyCode, int amount, TransactionMode mode) {
		this(currencyCode, amount, mode, ++uniqueId);
	}

	public Transaction(String currencyCode, int amount, TransactionMode mode, int id) {
		if(currencyCode == null || currencyCode.isEmpty()) {
			throw new IllegalArgumentException("currencyCode missing!");
		}
		else{
			if(amount < 50) {
				throw new IllegalArgumentException("Amount too low!");
			}
		}

		this.currencyCode = currencyCode;
		this.amount = amount;

		this.mode = mode;
		if(MoneyServiceApp.dummyDate != null) {
			timeStamp = LocalDateTime.parse(MoneyServiceApp.dummyDate.toString() + "T" + LocalTime.now().toString());
		} else {
			timeStamp = LocalDateTime.now();
		}
		this.id = id;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", currencyCode=" + currencyCode + ", amount=" + amount + ", mode=" + mode
				+ ", timeStamp=" + timeStamp + "]";
	}

	public LocalDateTime getCreatedAt() {
		return timeStamp;
	}
	/**
	 * @return the timeStamp
	 */
	public LocalDateTime getTimeStamp() {
		return timeStamp;

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

	public TransactionMode getMode() {
		return mode;
	}
	
	
}
