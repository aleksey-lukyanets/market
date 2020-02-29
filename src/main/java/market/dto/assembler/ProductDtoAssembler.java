package market.dto.assembler;

import market.domain.Product;
import market.dto.ProductDTO;
import market.rest.CartRestController;
import market.rest.ProductsRestController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 *
 */
@Component
public class ProductDtoAssembler extends RepresentationModelAssemblerSupport<Product, ProductDTO> {

	public ProductDtoAssembler() {
		super(ProductsRestController.class, ProductDTO.class);
	}

	@Override
	public ProductDTO toModel(Product product) {
		ProductDTO dto = createModelWithId(product.getId(), product);
		dto.setProductId(product.getId());
		dto.setDistillery(product.getDistillery() == null ? null : product.getDistillery().getTitle());
		dto.setName(product.getName());
		dto.setAge(product.getAge());
		dto.setAlcohol(product.getAlcohol());
		dto.setPrice(product.getPrice());
		dto.setVolume(product.getVolume());
		dto.setDescription(product.getDescription());
		dto.setAvailable(product.isAvailable());
		dto.add(linkTo(ProductsRestController.class).withRel("products"));
		dto.add(linkTo(CartRestController.class).withRel("cart"));
		return dto;
	}

	public Product dtoDomain(ProductDTO dto, long productId) {
		return new Product.Builder()
			.setId(productId)
			.setName(dto.getName())
			.setAge(dto.getAge())
			.setAlcohol(dto.getAlcohol())
			.setPrice(dto.getPrice())
			.setVolume(dto.getVolume())
			.setDescription(dto.getDescription())
			.setAvailable(dto.isAvailable())
			.build();
	}
}
