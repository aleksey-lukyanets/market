package market.controller.backend;

import market.domain.Product;
import market.dto.ProductDTO;
import market.dto.assembler.ProductDtoAssembler;
import market.service.ProductService;
import market.sorting.ISorter;
import market.sorting.SortingValuesDTO;
import market.sorting.StorageSorting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;

@Controller
@RequestMapping("/admin/storage")
@Secured({"ROLE_STAFF", "ROLE_ADMIN"})
public class StorageController {
	private static final String STORAGE_BASE = "admin/storage";

	private final ProductService productService;
	private final ISorter<ProductDTO> storageSorting = new StorageSorting();
	private final ProductDtoAssembler productAssembler = new ProductDtoAssembler();

	public StorageController(ProductService productService) {
		this.productService = productService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getStorageUnits(
		SortingValuesDTO sortingValues,
		@RequestParam(value = "available", required = false, defaultValue = "all") String available,
		Model model
	) {
		PageRequest request = storageSorting.updateSorting(sortingValues);
		Page<Product> pagedProducts = productService.findByAvailability(available, request);
		storageSorting.prepareModel(model, pagedProducts.map(productAssembler::toModel));

		model.addAttribute("currentlyAvailable", available);
		return STORAGE_BASE;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String postStorage(
		@RequestParam(value = "productsIds", required = false) Long[] productsIds,
		@RequestParam(value = "availableProductsIds", required = false) Long[] availableProductsIds
	) {
		if (availableProductsIds != null && productsIds != null) {
			Set<Long> available = new HashSet<>(Arrays.asList(availableProductsIds));
			Map<Boolean, List<Long>> productIdsByAvailability = Arrays.stream(productsIds)
				.filter(Objects::nonNull)
				.collect(groupingBy(available::contains));
			productService.updateAvailability(productIdsByAvailability);
		}
		return "redirect:/" + STORAGE_BASE;
	}
}
