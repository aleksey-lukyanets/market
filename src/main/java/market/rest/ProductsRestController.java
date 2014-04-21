package market.rest;

import java.util.ArrayList;
import java.util.List;
import market.domain.Product;
import market.domain.dto.ProductDTO;
import market.domain.dto.ProductPreviewDTO;
import market.exception.ProductNotFoundException;
import market.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * REST-контроллер товаров магазина.
 */
@Controller
@RequestMapping(value = "/rest/products")
@ExposesResourceFor(ProductDTO.class)
public class ProductsRestController {

    @Autowired
    private ProductService productService;
    
    /**
     * Просмотр всех товаров магазина.
     * 
     * @return список всех товаров
     */
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<ProductPreviewDTO> getProducts() {
        return createProductDtoList(productService.findAllOrderById());
    }
    
    private List<ProductPreviewDTO> createProductDtoList(List<Product> products) {
        List<ProductPreviewDTO> dtos = new ArrayList<>();
        for (Product product : products) {
            ProductPreviewDTO dto = product.createPerviewDTO();
            try {
                dto.add(linkTo(methodOn(ProductsRestController.class).getProduct(product.getId())).withSelfRel());
            } catch (ProductNotFoundException ex) {}
            dtos.add(dto);
        }
        return dtos;
    }
    
    /**
     * Просмотр одного товара.
     * 
     * @param id идентификатор товара
     * @return запрошенный товар
     * @throws ProductNotFoundException если товар с запрошенным id не существует
     */
    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ProductDTO getProduct(@PathVariable long id) throws ProductNotFoundException {
        Product product = productService.findOne(id);
        ProductDTO dto = product.createDTO();
        return dto;
    }
}
