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
		dto.setCityAndRegion(contacts.getCityAndRegion());
		dto.add(linkTo(CartRestController.class).withRel("Shopping cart"));
		return dto;
	}

	public Contacts toDomain(ContactsDTO dto) {
		Contacts contacts = new Contacts();
		contacts.setAddress(dto.getAddress());
		contacts.setPhone(dto.getPhone());
		contacts.setCityAndRegion(dto.getCityAndRegion());
		return contacts;
	}
}
