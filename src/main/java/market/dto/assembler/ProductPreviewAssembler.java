package market.dto.assembler;

import market.domain.Product;
import market.dto.ProductPreviewDTO;
import market.rest.ProductsRestController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

/**
 *
 */
public class ProductPreviewAssembler extends ResourceAssemblerSupport<Product, ProductPreviewDTO> {

    public ProductPreviewAssembler() {
        super(ProductsRestController.class, ProductPreviewDTO.class);
    }

    @Override
    public ProductPreviewDTO toResource(Product product) {
        ProductPreviewDTO dto = createResourceWithId(product.getId(), product);
        dto.setProductId(product.getId());
        dto.setDistillery(product.getDistillery().getTitle());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        return dto;
    }
}
