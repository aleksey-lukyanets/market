package market.sorting;

import org.springframework.stereotype.Component;

/**
 * Опции сортировки и фильтрации списка товаров.
 */
@Component
public class ProductBackendSorting extends AbstractSorter {

    {
        sortFieldOptions.put("price", "по цене");
        sortFieldOptions.put("distillery.title", "по винокурне");
        sortFieldOptions.put("age", "по возрасту");
    }
    
    @Override
    public int getDefaultPageSize() {
        return 10;
    }
}
