package market.sorting;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Опции сортировки и фильтрации списка заказов.
 */
@Component
public class OrderSorting extends AbstractSorter {

    private final Map<String, String> executedOptions = new LinkedHashMap<>();
    private final Map<String, String> createdOptions = new LinkedHashMap<>();

    {
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
        return 10;
    }

    @Override
    public Model prepareFilteredModel(Model model) {
        model.addAttribute("executedOptions", executedOptions);
        model.addAttribute("createdOptions", createdOptions);
        return model;
    }
}
