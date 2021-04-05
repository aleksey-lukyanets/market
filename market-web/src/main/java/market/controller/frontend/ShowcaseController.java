package market.controller.frontend;

import market.domain.Distillery;
import market.domain.Product;
import market.domain.Region;
import market.dto.DistilleryDTO;
import market.dto.ProductDTO;
import market.dto.RegionDTO;
import market.dto.assembler.DistilleryDtoAssembler;
import market.dto.assembler.ProductDtoAssembler;
import market.dto.assembler.RegionDtoAssembler;
import market.service.DistilleryService;
import market.service.ProductService;
import market.service.RegionService;
import market.sorting.ISorter;
import market.sorting.ProductSorting;
import market.sorting.SortingValuesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Region products showcase.
 */
@Controller
@RequestMapping("/regions")
public class ShowcaseController {
	private static final String REGIONS_BASE = "regions";

	private final RegionService regionService;
	private final ProductService productService;
	private final DistilleryService distilleryService;
	private final ISorter<ProductDTO> productSorting = new ProductSorting();
	private final ProductDtoAssembler productAssembler = new ProductDtoAssembler();
	private final RegionDtoAssembler regionDTOAssembler = new RegionDtoAssembler();
	private final DistilleryDtoAssembler distilleryDTOAssembler = new DistilleryDtoAssembler();

	public ShowcaseController(RegionService regionService, ProductService productService, DistilleryService distilleryService) {
		this.regionService = regionService;
		this.productService = productService;
		this.distilleryService = distilleryService;
	}

	/**
	 * Region products page. Filtering by distillery and sorting.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{regionId}")
	public String getRegionProducts(
		@PathVariable long regionId,
		SortingValuesDTO sortingValues,
		@RequestParam(value = "dist", required = false, defaultValue = "0") Long distilleryId,
		Model model
	) {
		Region region = regionService.findOne(regionId);

		PageRequest request = productSorting.updateSorting(sortingValues);
		Page<Product> pagedProducts;
		if (distilleryId == 0) {
			pagedProducts = productService.findByRegion(region, request);
		} else {
			Distillery distillery = distilleryService.findById(distilleryId);
			pagedProducts = productService.findByDistillery(distillery, request);
			model.addAttribute("currentDistilleryTitle", distillery.getTitle());
		}
		productSorting.prepareModel(model, pagedProducts.map(productAssembler::toModel));

		List<DistilleryDTO> distilleriesDto = distilleryService.findByRegion(region).stream()
			.map(distilleryDTOAssembler::toModel)
			.collect(toList());
		model.addAttribute("distilleries", distilleriesDto);

		List<RegionDTO> regionsDto = regionService.findAll().stream()
			.sorted(Comparator.comparing(Region::getId))
			.map(regionDTOAssembler::toModel)
			.collect(toList());
		model.addAttribute("regions", regionsDto);
		model.addAttribute("selectedRegion", regionDTOAssembler.toModel(region));
		return REGIONS_BASE;
	}
}
