package ya.thn.project.MoneyService;

public class Order {
	
	enum OrderType {BUY, SELL};
	
	private static int orderCounter = 0;
	
	private final int orderNr;
	private final OrderType orderType;
	private final String currencyCode;
	private final int amount;
	

	/**
  * @param orderType holds what kind of orderType, either SELL or BUY.
  * @param amount the amount what the order should BUY or SELL.
  * @param currencyCode the currency the order should deal with.
  */
	public Order(OrderType orderType, double amount, String currencyCode) {
		this.orderNr = ++orderCounter;
		
		this.orderType = orderType;
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
	 * @return the orderType
	 */
	public OrderType getOrderType() {
		return orderType;
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
		return "Order [orderNr=" + orderNr + ", orderType=" + orderType + ", currencyCode=" + currencyCode + ", amount="
				+ amount + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((currencyCode == null) ? 0 : currencyCode.hashCode());
		result = prime * result + orderNr;
		result = prime * result + ((orderType == null) ? 0 : orderType.hashCode());
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
		if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
			return false;
		if (currencyCode == null) {
			if (other.currencyCode != null)
				return false;
		} else if (!currencyCode.equals(other.currencyCode))
			return false;
		if (orderNr != other.orderNr)
			return false;
		if (orderType != other.orderType)
			return false;
		return true;
	}

}