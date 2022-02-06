package market.controller;

import market.domain.Cart;
import market.dto.CartDTO;
import market.dto.assembler.CartDtoAssembler;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

public class CartModelHelper {
	private final CartDtoAssembler cartDtoAssembler;

	public CartModelHelper(CartDtoAssembler cartDtoAssembler) {
		this.cartDtoAssembler = cartDtoAssembler;
	}

	public CartDTO convertAndUpdateAttributes(Cart cart, Model model, HttpServletRequest request) {
		CartDTO cartDTO = cartDtoAssembler.toModel(cart);
		model.addAttribute("cart", cartDTO);
		request.getSession().setAttribute("cart", cartDTO);
		return cartDTO;
	}
}
