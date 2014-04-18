package market.sorting;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

/**
 * Абстрактный класс опций сортировки.
 * 
 * @param <T> класс элементов обрабатываемого списка.
 */
public abstract class AbstractSortingOptions<T> implements ISortingOptions<T> {
    
    public static Integer FIRST_PAGE = 1;
    public static Integer PAGE_SIZE_DEFAULT = 3;
    public static String DIRECTION_DEFAULT = "asc";

    protected final Map<String, String> sortFieldOptions = new LinkedHashMap<>();
    
    private Integer pageNumber;
    private Integer pageSize;
    private String sortBy;
    private String sortDirection;
    private final Map<Integer, String> pageSizeOptions = new LinkedHashMap<>();
    private final Map<String, String> directionOptions = new LinkedHashMap<>();
    
    {
        directionOptions.put(DIRECTION_DEFAULT, "по возрастанию");
        directionOptions.put("desc", "по убыванию");
        
        pageSizeOptions.put(3, "3");
        pageSizeOptions.put(5, "5");
        pageSizeOptions.put(10, "10");
        pageSizeOptions.put(20, "20");
    }

    //-------------------------------------------------------- Обновление опций
    
    @Override
    public PageRequest updateSorting(SortingValuesDTO values) {
        this.sortBy = (values.getSort() == null) ? getSortFieldDefault() : values.getSort();
        this.pageSize = (values.getSize() == null) ? getDefaultPageSize() : values.getSize();
        this.pageNumber = (values.getPage() == null) ? FIRST_PAGE : values.getPage();
        this.sortDirection = (values.getDirect() == null) ? DIRECTION_DEFAULT : values.getDirect();
        return createPageRequest();
    }
    
    protected int getDefaultPageSize() {
        return PAGE_SIZE_DEFAULT;
    }
    
    private String getSortFieldDefault() {
        return sortFieldOptions.keySet().iterator().next();
    }

    private PageRequest createPageRequest() {
        return new PageRequest(
                getPageNumber() - 1,
                getPageSize(),
                Sort.Direction.fromStringOrNull(getSortDirection()),
                getSortBy());
    }
    
    //------------------------------------------------------- Подготовка модели
    
    @Override
    public Model prepareModel(Model model, Page<T> page) {
        preparePagedModel(model, page);
        prepareSortedModel(model);
        prepareFilteredModel(model);
        return model;
    }
    
    protected Model prepareFilteredModel(Model model) {
        return model;
    }
    
    protected Model prepareSortedModel(Model model) {
        model.addAttribute("pageSizeOptions", getPageSizeOptions());
        model.addAttribute("sortOptions", getSortFieldOptions());
        model.addAttribute("directOptions", getDirectionOptions());
        model.addAttribute("currentPageSize", getPageSize());
        model.addAttribute("currentSort", getSortBy());
        model.addAttribute("currentDirection", getSortDirection());
        return model;
    }

    protected Model preparePagedModel(Model model, Page<T> page) {
        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());
        model.addAttribute("page", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        return model;
    }
    
    //---------------------------------------------------- Аксессоры и мутаторы
    
    @Override
    public Integer getPageNumber() {
        return pageNumber;
    }

    @Override
    public Integer getPageSize() {
        return pageSize;
    }

    @Override
    public String getSortBy() {
        return sortBy;
    }

    @Override
    public String getSortDirection() {
        return sortDirection;
    }

    @Override
    public Map<Integer, String> getPageSizeOptions() {
        return pageSizeOptions;
    }

    @Override
    public Map<String, String> getSortFieldOptions() {
        return sortFieldOptions;
    }

    @Override
    public Map<String, String> getDirectionOptions() {
        return directionOptions;
    }
}
