package market.dto.assembler;

import market.domain.Product;
import market.dto.ProductDTO;
import market.rest.CartRestController;
import market.rest.ProductsRestController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 *
 */
public class ProductDtoAssembler extends RepresentationModelAssemblerSupport<Product, ProductDTO> {

	public ProductDtoAssembler() {
		super(ProductsRestController.class, ProductDTO.class);
	}

	@Override
	public ProductDTO toModel(Product product) {
		ProductDTO dto = createModelWithId(product.getId(), product);
		dto.setProductId(product.getId());
		dto.setDistillery(product.getDistillery().getTitle());
		dto.setName(product.getName());
		dto.setAge(product.getAge());
		dto.setAlcohol(product.getAlcohol());
		dto.setPrice(product.getPrice());
		dto.setVolume(product.getVolume());
		dto.setDescription(product.getDescription());
		dto.setInStock(product.getStorage().isAvailable());
		dto.add(linkTo(ProductsRestController.class).withRel("products"));
		dto.add(linkTo(CartRestController.class).withRel("cart"));
		return dto;
	}
}
