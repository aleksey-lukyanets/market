package market.dto.assembler;

import market.domain.Product;
import market.dto.ProductPreviewDTO;
import market.rest.ProductsRestController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class ProductPreviewAssembler extends RepresentationModelAssemblerSupport<Product, ProductPreviewDTO> {

	public ProductPreviewAssembler() {
		super(ProductsRestController.class, ProductPreviewDTO.class);
	}

	@Override
	public ProductPreviewDTO toModel(Product product) {
		ProductPreviewDTO dto = createModelWithId(product.getId(), product);
		dto.setProductId(product.getId());
		dto.setDistillery(product.getDistillery().getTitle());
		dto.setName(product.getName());
		dto.setPrice(product.getPrice());
		return dto;
	}
}
