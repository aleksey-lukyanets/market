package market.rest;

import market.domain.Product;
import market.dto.ProductDTO;
import market.dto.assembler.ProductDtoAssembler;
import market.exception.UnknownEntityException;
import market.service.ProductService;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "products")
@ExposesResourceFor(ProductDTO.class)
public class ProductsRestController {

	private final ProductService productService;
	private final ProductDtoAssembler productAssembler = new ProductDtoAssembler();

	public ProductsRestController(ProductService productService) {
		this.productService = productService;
	}

	/**
	 * All the existing products, sorted by id.
	 */
	@GetMapping
	public Collection<ProductDTO> getProducts() {
		return productService.findAll().stream()
			.sorted(Comparator.comparing(Product::getId))
			.map(productAssembler::toModel)
			.peek(this::addSelfLink)
			.collect(Collectors.toList());
	}

	private void addSelfLink(ProductDTO dto) {
		dto.add(linkTo(methodOn(getClass()).getProduct(dto.getProductId())).withRel("self"));
	}

	/**
	 * Viewing a single product.
	 *
	 * @return product with the specified id
	 * @throws UnknownEntityException if the product with the specified id doesn't exist
	 */
	@GetMapping(value = "/{productId}")
	public ProductDTO getProduct(@PathVariable long productId) {
		return productService.findById(productId)
			.map(productAssembler::toModel)
			.map(this::addListLink)
			.orElseThrow(() -> new UnknownEntityException(ProductDTO.class, productId));
	}

	private ProductDTO addListLink(ProductDTO dto) {
		dto.add(linkTo(methodOn(getClass()).getProducts()).withRel("All products"));
		return dto;
	}
}
