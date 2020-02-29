package market.rest;

import market.domain.Product;
import market.dto.ProductDTO;
import market.dto.ProductPreviewDTO;
import market.dto.assembler.ProductDtoAssembler;
import market.dto.assembler.ProductPreviewAssembler;
import market.exception.UnknownEntityException;
import market.service.ProductService;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/rest/products")
@ExposesResourceFor(ProductDTO.class)
public class ProductsRestController {

	private final ProductService productService;
	private final ProductPreviewAssembler productPreviewAssembler;
	private final ProductDtoAssembler productAssembler;

	public ProductsRestController(ProductService productService,
		ProductPreviewAssembler productPreviewAssembler, ProductDtoAssembler productAssembler)
	{
		this.productService = productService;
		this.productPreviewAssembler = productPreviewAssembler;
		this.productAssembler = productAssembler;
	}

	/**
	 * All the products.
	 */
	@RequestMapping(
		method = RequestMethod.GET,
		produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Collection<ProductPreviewDTO> getProducts() {
		return productService.findAll().stream()
			.sorted(Comparator.comparing(Product::getId))
			.map(productPreviewAssembler::toModel)
			.collect(Collectors.toList());
	}

	/**
	 * Viewing a single product.
	 *
	 * @return product with the specified id
	 * @throws UnknownEntityException if the product with the specified id doesn't exist
	 */
	@RequestMapping(value = "/{id}",
		method = RequestMethod.GET,
		produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ProductDTO getProduct(@PathVariable long id) throws UnknownEntityException {
		return productService.findOne(id)
			.map(productAssembler::toModel)
			.orElseThrow(() -> new UnknownEntityException(Product.class, id));
	}
}
