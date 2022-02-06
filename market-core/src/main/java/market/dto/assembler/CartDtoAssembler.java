package market.dto.assembler;

import market.domain.Cart;
import market.domain.CartItem;
import market.domain.Product;
import market.dto.CartDTO;
import market.dto.CartItemDTO;
import market.properties.MarketProperties;
import market.service.ProductService;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CartDtoAssembler implements RepresentationModelAssembler<Cart, CartDTO> {

	private final MarketProperties marketProperties;

	public CartDtoAssembler(MarketProperties marketProperties) {
		this.marketProperties = marketProperties;
	}

	@Override
	public CartDTO toModel(Cart cart) {
		CartDTO dto = toAnonymousResource(cart);
		dto.setUser(cart.getUserAccount().getEmail());
		return dto;
	}

	public CartDTO toAnonymousResource(Cart cart) {
		int deliveryCost = marketProperties.getDeliveryCost();

		CartDTO dto = new CartDTO();
		dto.setDeliveryIncluded(cart.isDeliveryIncluded());
		dto.setProductsCost(cart.getItemsCost());
		dto.setDeliveryCost(deliveryCost);
		dto.setTotalCost(cart.getItemsCost() + deliveryCost);
		dto.setTotalItems(cart.getItemsCount());

		List<CartItemDTO> cartItemsDto = cart.getCartItems().stream()
			.map(this::toCartItemDto)
			.collect(Collectors.toList());
		dto.setCartItems(cartItemsDto);

		return dto;
	}

	public CartItemDTO toCartItemDto(CartItem cartItem) {
		Long productId = cartItem.getProduct().getId();

		CartItemDTO dto = new CartItemDTO();
		dto.setProductId(productId);
		dto.setQuantity(cartItem.getQuantity());
		return dto;
	}

	/**
	 * @return domain cart created from DTO
	 */
	public Cart toDomain(CartDTO cartDTO, ProductService productService) { // todo: avoid passing service here, pass a map
		Cart cart = new Cart();
		cart.setDeliveryIncluded(cartDTO.isDeliveryIncluded());
		for (CartItemDTO cartItemDto : cartDTO.getCartItems()) {
			Optional<Product> productOptional = productService.findById(cartItemDto.getProductId());
			if (productOptional.isPresent()) {
				Product product = productOptional.get();
				if (product.isAvailable())
					cart.update(product, cartItemDto.getQuantity());
			}
		}
		return cart;
	}
}
