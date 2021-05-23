package market.sorting;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Опции сортировки и фильтрации списка заказов.
 */
@Component
public class OrderSorting extends AbstractSorter {
	private final int defaultPageSize;

	private final Map<String, String> executedOptions = new LinkedHashMap<>();
	private final Map<String, String> createdOptions = new LinkedHashMap<>();

	public OrderSorting(int defaultPageSize) {
		this.defaultPageSize = defaultPageSize;

		sortFieldOptions.put("dateCreated", "по дате оформления");
		sortFieldOptions.put("bill.totalCost", "по сумме");
		sortFieldOptions.put("userAccount.name", "по имени покупателя");

		executedOptions.put("all", "все заказы");
		executedOptions.put("true", "только исполненные");
		executedOptions.put("false", "только в исполнении");
		createdOptions.put("all", "за всё время");
		createdOptions.put("1", "за сутки");
		createdOptions.put("7", "за 7 дней");
		createdOptions.put("30", "за 30 дней");
	}

	@Override
	public int getDefaultPageSize() {
		return defaultPageSize;
	}

	@Override
	public Model prepareFilteredModel(Model model) {
		model.addAttribute("executedOptions", executedOptions);
		model.addAttribute("createdOptions", createdOptions);
		return model;
	}
}
