package market.dto.assembler;

import market.domain.Contacts;
import market.dto.ContactsDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public class ContactsDtoAssembler implements RepresentationModelAssembler<Contacts, ContactsDTO> {

	@Override
	public ContactsDTO toModel(Contacts contacts) {
		ContactsDTO dto = new ContactsDTO();
		dto.setPhone(contacts.getPhone());
		dto.setAddress(contacts.getAddress());
		return dto;
	}

	public Contacts toDomain(ContactsDTO dto) {
		Contacts contacts = new Contacts();
		contacts.setAddress(dto.getAddress());
		contacts.setPhone(dto.getPhone());
		return contacts;
	}
}
