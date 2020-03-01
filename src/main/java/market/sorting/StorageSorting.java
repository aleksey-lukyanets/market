package market.sorting;

import org.springframework.stereotype.*;
import org.springframework.ui.*;

import java.util.*;

/**
 * Опции сортировки и фильтрации списка единиц хранения.
 */
@Component
public class StorageSorting extends AbstractSorter {

	private final Map<String, String> availableOptions = new LinkedHashMap<>();

	public StorageSorting() {
		sortFieldOptions.put("price", "по цене");
		sortFieldOptions.put("distillery.title", "по винокурне");
		sortFieldOptions.put("age", "по возрасту");

		availableOptions.put("all", "все товары");
		availableOptions.put("true", "только в наличии");
		availableOptions.put("false", "только отсутствующие");
	}

	@Override
	public int getDefaultPageSize() {
		return 10;
	}

	@Override
	public Model prepareFilteredModel(Model model) {
		model.addAttribute("availableOptions", availableOptions);
		return model;
	}
}
