package market.sorting;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Опции сортировки и фильтрации списка единиц хранения.
 */
@Component
public class StorageSorting extends AbstractSorter {

    private final Map<String, String> availableOptions = new LinkedHashMap<>();

    {
        sortFieldOptions.put("product.price", "по цене");
        sortFieldOptions.put("product.distillery.title", "по винокурне");
        sortFieldOptions.put("product.age", "по возрасту");
        
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
