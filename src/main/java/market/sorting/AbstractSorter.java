package market.sorting;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * Управляющий сортировкой и разбивкой на страницы.
 * <p>
 * Инкапсулирует операции с опциями сортировки и разбивки на страницы: хранение
 * и обновление значений, а также дополнение модели необходимыми объектами в
 * соответствии с текущими значениями.
 * <p>
 * Добавление перечня опций сортировки (по умолчанию пустой) и другого
 * дополнительного функционала (e.g. фильтрации) осуществляется в классах-потомках.
 *
 * @param <T> класс элементов обрабатываемого списка
 */
public abstract class AbstractSorter<T> implements ISorter<T> {

	public static Integer FIRST_PAGE = 1;
	public static Integer PAGE_SIZE_DEFAULT = 2;
	public static Sort.Direction DIRECTION_DEFAULT = Sort.Direction.ASC;

	protected final Map<String, String> sortFieldOptions = new LinkedHashMap<>();
	private final Map<Integer, String> pageSizeOptions = new LinkedHashMap<>();
	private final Map<String, String> directionOptions = new LinkedHashMap<>();
	private Integer pageNumber;
	private Integer pageSize;
	private String sortBy;
	private Sort.Direction sortDirection;

	public AbstractSorter() {
		directionOptions.put(DIRECTION_DEFAULT.toString(), "по возрастанию");
		directionOptions.put(Sort.Direction.DESC.toString(), "по убыванию");

		pageSizeOptions.put(2, "2");
		pageSizeOptions.put(5, "5");
		pageSizeOptions.put(10, "10");
		pageSizeOptions.put(20, "20");
	}

	//-------------------------------------------------------- Обновление опций

	private static Sort.Direction parseSortDirection(String direction) {
		if (direction == null)
			return DIRECTION_DEFAULT;
		return Sort.Direction.fromOptionalString(direction).orElse(DIRECTION_DEFAULT);
	}

	@Override
	public PageRequest updateSorting(SortingValuesDTO values) {
		this.sortBy = (values.getSort() == null) ? getSortFieldDefault() : values.getSort();
		this.pageSize = (values.getSize() == null) ? getDefaultPageSize() : values.getSize();
		this.pageNumber = (values.getPage() == null) ? FIRST_PAGE : values.getPage();
		this.sortDirection = parseSortDirection(values.getDirect());
		return createPageRequest();
	}

	/**
	 * @return кол-во объектов на странице
	 */
	protected int getDefaultPageSize() {
		return PAGE_SIZE_DEFAULT;
	}

	private String getSortFieldDefault() {
		return sortFieldOptions.keySet().iterator().next();
	}

	private PageRequest createPageRequest() {
		return PageRequest.of(
			getPageNumber() - 1,
			getPageSize(),
			getSortDirection(),
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

	/**
	 * Дополнение модели объектами разбивки на страницы.
	 *
	 * @param model изменяемая модель
	 * @return изменённая модель
	 */
	protected Model preparePagedModel(Model model, Page<T> page) {
		int current = page.getNumber() + 1;
		int begin = Math.max(1, current - 5);
		int end = Math.min(begin + 10, page.getTotalPages());
		model.addAttribute("page", page);
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("indexesList", IntStream.rangeClosed(begin, end).boxed().collect(toList()));
		return model;
	}

	/**
	 * Дополнение модели объектами сортировки.
	 *
	 * @param model изменяемая модель
	 * @return изменённая модель
	 */
	protected Model prepareSortedModel(Model model) {
		model.addAttribute("pageSizeOptions", getPageSizeOptions());
		model.addAttribute("sortOptions", getSortFieldOptions());
		model.addAttribute("directOptions", getDirectionOptions());
		model.addAttribute("currentPageSize", getPageSize());
		model.addAttribute("currentSort", getSortBy());
		model.addAttribute("currentDirection", getSortDirection().toString());
		return model;
	}

	/**
	 * Дополнение модели объектами фильтрации.
	 *
	 * @param model изменяемая модель
	 * @return изменённая модель
	 */
	protected Model prepareFilteredModel(Model model) {
		return model;
	}

	//---------------------------------------------------- Аксессоры и мутаторы

	public Integer getPageNumber() {
		return pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public String getSortBy() {
		return sortBy;
	}

	public Sort.Direction getSortDirection() {
		return sortDirection;
	}

	public Map<Integer, String> getPageSizeOptions() {
		return pageSizeOptions;
	}

	public Map<String, String> getSortFieldOptions() {
		return sortFieldOptions;
	}

	public Map<String, String> getDirectionOptions() {
		return directionOptions;
	}
}
