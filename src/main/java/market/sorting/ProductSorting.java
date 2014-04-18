package market.sorting;

import org.springframework.stereotype.Component;

/**
 * Опции сортировки и фильтрации списка товаров.
 */
@Component
public class ProductSorting extends AbstractSortingOptions {

    {
        sortFieldOptions.put("price", "по цене");
        sortFieldOptions.put("distillery.title", "по винокурне");
        sortFieldOptions.put("age", "по возрасту");
    }
}
