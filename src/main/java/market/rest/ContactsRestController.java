package market.rest;

import market.domain.Contacts;
import market.dto.ContactsDTO;
import market.dto.assembler.ContactsDtoAssembler;
import market.service.ContactsService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Customer contacts controller.
 */
@Controller
@RequestMapping(value = "/rest/customer/contacts")
@Secured({"ROLE_USER"})
public class ContactsRestController {
	private final ContactsService contactsService;
	private final ContactsDtoAssembler contactsDtoAssembler;

	public ContactsRestController(ContactsService contactsService, ContactsDtoAssembler contactsDtoAssembler) {
		this.contactsService = contactsService;
		this.contactsDtoAssembler = contactsDtoAssembler;
	}

	/**
	 * Viewing contacts.
	 */
	@RequestMapping(
		method = RequestMethod.GET,
		produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ContactsDTO getContacts(Principal principal) {
		Contacts contacts = contactsService.getContacts(principal.getName());
		return contactsDtoAssembler.toModel(contacts);
	}

	/**
	 * Updating contacts.
	 *
	 * @return updated contacts
	 */
	@RequestMapping(
		method = RequestMethod.PUT,
		consumes = MediaUtf8.APPLICATION_JSON_UTF8_VALUE,
		produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ContactsDTO postContacts(Principal principal, @Valid @RequestBody ContactsDTO contactsDto) {
		String login = principal.getName();
		Contacts changedContacts = contactsDtoAssembler.toDomain(contactsDto);
		contactsService.updateUserContacts(changedContacts, login);
		return contactsDtoAssembler.toModel(changedContacts);
	}
}
