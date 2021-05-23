package market.dto.assembler;

import market.domain.Product;
import market.dto.ProductPreviewDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public class ProductPreviewAssembler implements RepresentationModelAssembler<Product, ProductPreviewDTO> {

	@Override
	public ProductPreviewDTO toModel(Product product) {
		ProductPreviewDTO dto = new ProductPreviewDTO();
		dto.setProductId(product.getId());
		dto.setRegion(product.getDistillery().getRegion().getName());
		dto.setDistillery(product.getDistillery().getTitle());
		dto.setName(product.getName());
		dto.setPrice(product.getPrice());
		return dto;
	}
}
