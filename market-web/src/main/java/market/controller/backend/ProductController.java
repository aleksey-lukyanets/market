package market.controller.backend;

import market.domain.Distillery;
import market.domain.Product;
import market.dto.DistilleryDTO;
import market.dto.ProductDTO;
import market.dto.assembler.DistilleryDtoAssembler;
import market.dto.assembler.ProductDtoAssembler;
import market.exception.UnknownEntityException;
import market.properties.PaginationProperties;
import market.service.DistilleryService;
import market.service.ProductService;
import market.sorting.ISorter;
import market.sorting.ProductBackendSorting;
import market.sorting.SortingValuesDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Controller
@RequestMapping("/admin/products")
@Secured({"ROLE_STAFF", "ROLE_ADMIN"})
public class ProductController {
	private static final Logger log = LogManager.getLogger(ProductController.class);

	private static final String PRODUCTS_BASE = "admin/products";
	private static final String PRODUCTS_EDIT = PRODUCTS_BASE + "/edit";
	private static final String PRODUCTS_NEW = PRODUCTS_BASE + "/new";

	private final ProductService productService;
	private final DistilleryService distilleryService;
	private final ISorter<ProductDTO> productBackendSorting;
	private final ProductDtoAssembler productDtoAssembler = new ProductDtoAssembler();
	private final DistilleryDtoAssembler distilleryDtoAssembler = new DistilleryDtoAssembler();

	public ProductController(ProductService productService, DistilleryService distilleryService,
		PaginationProperties paginationProperties)
	{
		this.productService = productService;
		this.distilleryService = distilleryService;
		productBackendSorting = new ProductBackendSorting(paginationProperties.getBackendProduct());
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getProducts(
		SortingValuesDTO sortingValues,
		@RequestParam(value = "dist", required = false, defaultValue = "0") long distilleryId,
		Model model
	) {
		PageRequest request = productBackendSorting.updateSorting(sortingValues);
		Page<Product> pagedProducts;
		if (distilleryId == 0) {
			pagedProducts = productService.findAll(request);
		} else {
			Distillery distillery = distilleryService.findById(distilleryId);
			pagedProducts = productService.findByDistillery(distillery, request);
			model.addAttribute("currentDistilleryTitle", distillery.getTitle());
		}
		productBackendSorting.prepareModel(model, pagedProducts.map(productDtoAssembler::toModel));

		List<Distillery> distilleries = distilleryService.findAll();
		List<DistilleryDTO> distilleriesDto = distilleries.stream()
			.map(distilleryDtoAssembler::toModel)
			.collect(toList());
		model.addAttribute("distilleries", distilleriesDto);

		Map<String, String> regionByDistillery = distilleries.stream()
			.collect(toMap(Distillery::getTitle, d -> d.getRegion().getName()));
		model.addAttribute("regionByDistillery", regionByDistillery);

		return PRODUCTS_BASE;
	}

	//------------------------------------------------------- Creating new product

	@RequestMapping(method = RequestMethod.GET, value = "/new")
	public String newProduct(Model model) {
		List<DistilleryDTO> distilleriesDto = distilleryService.findAll().stream()
			.map(distilleryDtoAssembler::toModel)
			.collect(toList());
		model.addAttribute("distilleries", distilleriesDto);
		model.addAttribute("product", productDtoAssembler.toModel(new Product()));
		return PRODUCTS_NEW;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/new")
	public String postProduct(
		@Valid ProductDTO product, BindingResult bindingResult
	) {
		if (bindingResult.hasErrors())
			return "redirect:/" + PRODUCTS_NEW;

		Product newProduct = productDtoAssembler.dtoDomain(product);
		productService.create(newProduct, product.getDistillery());
		return "redirect:/" + PRODUCTS_BASE;
	}

	//--------------------------------------------------- Updating product

	@RequestMapping(method = RequestMethod.GET, value = "/{productId}/edit")
	public String editProduct(
		@PathVariable long productId, Model model
	) {
		Optional<Product> productOptional = productService.findById(productId);
		if (!productOptional.isPresent())
			return "redirect:/" + PRODUCTS_BASE;

		List<DistilleryDTO> distilleriesDto = distilleryService.findAll().stream()
			.map(distilleryDtoAssembler::toModel)
			.collect(toList());
		model.addAttribute("distilleries", distilleriesDto);
		model.addAttribute("product", productDtoAssembler.toModel(productOptional.get()));
		return PRODUCTS_EDIT;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/{productId}/edit")
	public String putProduct(
		@PathVariable long productId,
		@Valid ProductDTO product, BindingResult bindingResult
	) {
		if (bindingResult.hasErrors())
			return "redirect:/" + PRODUCTS_EDIT;

		Product changedProduct = productDtoAssembler.dtoDomain(product);
		try {
			productService.update(productId, changedProduct, product.getDistillery());
			return "redirect:/" + PRODUCTS_BASE;
		} catch (UnknownEntityException e) {
			log.warn(e.getMessage());
			return "redirect:/" + PRODUCTS_EDIT;
		}
	}

	//--------------------------------------------------------- Removing product

	@RequestMapping(method = RequestMethod.POST, value = "/{productId}/delete")
	public String deleteProduct(@PathVariable long productId) {
		productService.delete(productId);
		return "redirect:/" + PRODUCTS_BASE;
	}
}
