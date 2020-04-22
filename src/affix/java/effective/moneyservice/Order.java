package affix.java.effective.moneyservice;

public class Order {
	
	
	private static int orderCounter = 0;
	
	private final int orderNr;
	private final TransactionMode mode;
	private final String currencyCode;
	private final int amount;
	

	/**
  * @param orderType holds what kind of orderType, either SELL or BUY.
  * @param amount the amount what the order should BUY or SELL.
  * @param currencyCode the currency the order should deal with.
  */
	public Order(TransactionMode mode, int amount, String currencyCode) {
		
		if(amount <= 0) {
			throw new IllegalArgumentException("Amount not valid");
		}
		else{
			if(currencyCode.isEmpty() || currencyCode == null) {
				throw new IllegalArgumentException("currencyCode missing!");
			}
		}
		this.orderNr = ++orderCounter;
		this.mode = mode;
		this.amount = amount;
		this.currencyCode = currencyCode;
	}


	/**
	 * @return the orderNr
	 */
	public int getOrderNr() {
		return orderNr;
	}

	
  
	/**
	 * @return the mode
	 */
	public TransactionMode getMode() {
		return mode;
	}
	
	/**
	 * @return the currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}
	

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}
	
	
	@Override
	public String toString() {
		return "Order [orderNr=" + orderNr + ", mode=" + mode + ", currencyCode=" + currencyCode + ", amount=" + amount
				+ "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount;
		result = prime * result + ((currencyCode == null) ? 0 : currencyCode.hashCode());
		result = prime * result + ((mode == null) ? 0 : mode.hashCode());
		result = prime * result + orderNr;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (amount != other.amount)
			return false;
		if (currencyCode == null) {
			if (other.currencyCode != null)
				return false;
		} else if (!currencyCode.equals(other.currencyCode))
			return false;
		if (mode != other.mode)
			return false;
		if (orderNr != other.orderNr)
			return false;
		return true;
	}
}
