package market.domain;

import javax.persistence.*;
import java.io.*;
import java.util.*;

/**
 * Primary key of a {@link Product} inside the {@link Order}.
 */
@Embeddable
public class OrderedProductId implements Serializable {
	private static final long serialVersionUID = 5368453186150127449L;

	private Long orderId;
	private Long productId;

	public Long getCustomerOrder() {
		return orderId;
	}

	public void setCustomerOrder(Long orderId) {
		this.orderId = orderId;
	}

	public Long getProduct() {
		return productId;
	}

	public void setProduct(Long productId) {
		this.productId = productId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		OrderedProductId that = (OrderedProductId) o;

		if (!Objects.equals(orderId, that.orderId)) return false;
		return Objects.equals(productId, that.productId);
	}

	@Override
	public int hashCode() {
		int result;
		result = (orderId != null ? orderId.hashCode() : 0);
		result = 31 * result + (productId != null ? productId.hashCode() : 0);
		return result;
	}
}
