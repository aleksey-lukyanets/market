package market.rest;

import market.domain.Contacts;
import market.dto.ContactsDTO;
import market.dto.assembler.ContactsDtoAssembler;
import market.service.ContactsService;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Customer contacts controller.
 */
@RestController
@RequestMapping(value = "rest/customer/contacts")
@ExposesResourceFor(ContactsDTO.class)
@Secured({"ROLE_USER"})
public class ContactsRestController {
	private final ContactsService contactsService;
	private final ContactsDtoAssembler contactsDtoAssembler = new ContactsDtoAssembler();

	public ContactsRestController(ContactsService contactsService) {
		this.contactsService = contactsService;
	}

	/**
	 * Viewing contacts.
	 */
	@GetMapping
	public ContactsDTO getContacts(Principal principal) {
		Contacts contacts = contactsService.getContacts(principal.getName());
		return contactsDtoAssembler.toModel(contacts);
	}

	/**
	 * Updating contacts.
	 *
	 * @return updated contacts
	 */
	@PutMapping
	public ContactsDTO updateContacts(Principal principal, @RequestBody @Valid ContactsDTO contactsDto) {
		String login = principal.getName();
		Contacts changedContacts = contactsDtoAssembler.toDomain(contactsDto);
		contactsService.updateUserContacts(changedContacts, login);
		return contactsDtoAssembler.toModel(changedContacts);
	}
}
