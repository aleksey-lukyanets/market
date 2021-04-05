package market.dto.assembler;

import market.domain.Product;
import market.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import java.util.List;

public class ProductDtoAssembler implements RepresentationModelAssembler<Product, ProductDTO> {

	@Override
	public ProductDTO toModel(Product product) {
		ProductDTO dto = new ProductDTO();
		dto.setProductId(product.getId());
		dto.setDistillery(product.getDistillery() == null ? null : product.getDistillery().getTitle());
		dto.setName(product.getName());
		dto.setAge(product.getAge());
		dto.setAlcohol(product.getAlcohol());
		dto.setPrice(product.getPrice());
		dto.setVolume(product.getVolume());
		dto.setDescription(product.getDescription());
		dto.setAvailable(product.isAvailable());
		return dto;
	}

	public PageImpl<ProductDTO> toModel(Page<Product> page) {
		List<ProductDTO> dtoList = page.map(this::toModel).toList();
		return new PageImpl<>(dtoList, page.getPageable(), page.getTotalElements());
	}

	public Product dtoDomain(ProductDTO dto) {
		return new Product.Builder()
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
