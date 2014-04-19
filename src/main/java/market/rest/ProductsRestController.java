package market.rest;

import java.util.ArrayList;
import java.util.List;
import market.domain.Product;
import market.domain.dto.ProductDTO;
import market.exception.ProductNotFoundException;
import market.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
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
    public List<ProductDTO> getProducts() {
        return createProductDtoList(productService.findAll());
    }
    
    private List<ProductDTO> createProductDtoList(List<Product> products) {
        List<ProductDTO> dtos = new ArrayList<>();
        for (Product product : products) {
            dtos.add(product.createDTO());
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
