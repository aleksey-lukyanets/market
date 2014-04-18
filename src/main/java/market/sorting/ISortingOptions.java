package market.sorting;

import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;

/**
 * Интерфейс опций сортировки и фильтрации.
 * 
 * @param <T> класс элементов обрабатываемого списка.
 */
public interface ISortingOptions<T> {

    PageRequest updateSorting(SortingValuesDTO sortingValues);

    Model prepareModel(Model model, Page<T> page);

    Integer getPageNumber();

    Integer getPageSize();

    String getSortBy();

    String getSortDirection();

    Map<String, String> getDirectionOptions();

    Map<Integer, String> getPageSizeOptions();

    Map<String, String> getSortFieldOptions();

}
