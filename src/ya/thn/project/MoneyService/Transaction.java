package ya.thn.project.MoneyService;
import java.time.LocalDateTime;

public class Transaction {
	
	private Order order;
	private LocalDateTime createdAt;
	
	public Transaction(Order order) {
		this.order = order;
		// Creating a timestamp.
		createdAt = LocalDateTime.now();
	}

	/**
	 * @return the order
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * @return the createdAt
	 */
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
