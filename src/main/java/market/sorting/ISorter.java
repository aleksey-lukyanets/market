package market.sorting;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;

/**
 * Интерфейс опций сортировки и разбивки на страницы.
 *
 * @param <T> класс элементов обрабатываемого списка
 */
public interface ISorter<T> {

	/**
	 * Обновление значений опций сортировки.
	 *
	 * @param sortingValues новые значения опций
	 * @return поисковый запрос для обращения к ДАО
	 */
	PageRequest updateSorting(SortingValuesDTO sortingValues);

	/**
	 * Добавление данных в модель.
	 * <p>
	 * Добавляет в модель данные и все служебные объекты, связанные
	 * с постраничным отображаением и сортировкой.
	 *
	 * @param model модель, которая будет обновлена
	 * @param page  результаты постраничной выборки из БД
	 * @return дополненная модель
	 */
	Model prepareModel(Model model, Page<T> page);
}
