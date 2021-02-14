package market.dto.assembler;

import market.domain.Contacts;
import market.dto.ContactsDTO;
import market.rest.CartRestController;
import market.rest.ContactsRestController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ContactsDtoAssembler extends RepresentationModelAssemblerSupport<Contacts, ContactsDTO> {

	public ContactsDtoAssembler() {
		super(ContactsRestController.class, ContactsDTO.class);
	}

	@Override
	public ContactsDTO toModel(Contacts contacts) {
		ContactsDTO dto = instantiateModel(contacts);
		dto.setPhone(contacts.getPhone());
		dto.setAddress(contacts.getAddress());
		dto.add(linkTo(CartRestController.class).withRel("Shopping cart"));
		return dto;
	}

	public Contacts toDomain(ContactsDTO dto) {
		Contacts contacts = new Contacts();
		contacts.setAddress(dto.getAddress());
		contacts.setPhone(dto.getPhone());
		return contacts;
	}
}
