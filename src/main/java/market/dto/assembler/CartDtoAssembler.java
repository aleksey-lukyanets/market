package market.dto.assembler;

import market.domain.Cart;
import market.domain.CartItem;
import market.domain.Product;
import market.dto.CartDTO;
import market.dto.CartItemDTO;
import market.rest.CartRestController;
import market.rest.ContactsRestController;
import market.service.ProductService;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 *
 */
@Component
public class CartDtoAssembler extends RepresentationModelAssemblerSupport<Cart, CartDTO> {

	public CartDtoAssembler() {
		super(CartRestController.class, CartDTO.class);
	}

	@Override
	public CartDTO toModel(Cart cart) {
		CartDTO dto = toAnonymousResource(cart);
		dto.setUser(cart.getUserAccount().getEmail());
		dto.add(linkTo(ContactsRestController.class).withRel("Customer contacts"));
		dto.add(linkTo(CartRestController.class).slash("payment").withRel("Payment"));
		return dto;
	}

	public CartDTO toAnonymousResource(Cart cart) {
		CartDTO dto = instantiateModel(cart);
		dto.setDeliveryIncluded(cart.isDeliveryIncluded());
		dto.setProductsCost(cart.getItemsCost());
		dto.setTotalCost(cart.getItemsCost());
		dto.setItemsCount(cart.getItemsCount());

		List<CartItemDTO> cartItemsDto = cart.getCartItems().stream()
			.map(this::toCartItemDto)
			.collect(Collectors.toList());
		dto.setCartItems(cartItemsDto);

		return dto;
	}

	private CartItemDTO toCartItemDto(CartItem cartItem) {
		CartItemDTO dto = new CartItemDTO();
		dto.setProductId(cartItem.getProduct().getId());
		dto.setQuantity(cartItem.getQuantity());
		return dto;
	}

	/**
	 * @return domain cart created from DTO
	 */
	public Cart toDomain(CartDTO cartDTO, ProductService productService) {
		Cart cart = new Cart();
		cart.setDeliveryIncluded(cartDTO.isDeliveryIncluded());
		for (CartItemDTO cartItemDto : cartDTO.getCartItems()) {
			Optional<Product> productOptional = productService.findOne(cartItemDto.getProductId());
			if (productOptional.isPresent()) {
				Product product = productOptional.get();
				if (product.isAvailable())
					cart.update(product, cartItemDto.getQuantity());
			}
		}
		return cart;
	}
}
