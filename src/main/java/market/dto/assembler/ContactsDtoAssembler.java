package market.dto.assembler;

import market.domain.Contacts;
import market.dto.ContactsDTO;
import market.rest.CartRestController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 *
 */
@Component
public class ContactsDtoAssembler extends RepresentationModelAssemblerSupport<Contacts, ContactsDTO> {

	public ContactsDtoAssembler() {
		super(CartRestController.class, ContactsDTO.class);
	}

	@Override
	public ContactsDTO toModel(Contacts contacts) {
		ContactsDTO dto = instantiateModel(contacts);
		dto.setPhone(contacts.getPhone());
		dto.setAddress(contacts.getAddress());
		dto.add(linkTo(CartRestController.class).withRel("Shopping cart"));
		return dto;
	}
}
