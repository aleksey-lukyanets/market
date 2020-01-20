package market.rest;

import market.domain.Contacts;
import market.dto.ContactsDTO;
import market.dto.assembler.ContactsDtoAssembler;
import market.service.ContactsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.security.Principal;

/**
 * REST-контроллер контактных данных покупателя.
 */
@Controller
@RequestMapping(value = "/rest/customer/contacts")
public class ContactsRestController {
	private final ContactsService contactsService;
	private final ContactsDtoAssembler contactsDtoAssembler;

	public ContactsRestController(ContactsService contactsService, ContactsDtoAssembler contactsDtoAssembler) {
		this.contactsService = contactsService;
		this.contactsDtoAssembler = contactsDtoAssembler;
	}

	/**
	 * Просмотр контактов покупателя.
	 *
	 * @return текущие контакты покупателя
	 */
	@RequestMapping(
		method = RequestMethod.GET,
		produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ContactsDTO getContacts(Principal principal) {
		String login = principal.getName();
		Contacts contacts = contactsService.getUserContacts(login);
		return contactsDtoAssembler.toModel(contacts);
	}

	/**
	 * Изменение контактов покупателя.
	 *
	 * @param contactsDto новые контактные данные
	 * @param principal
	 * @return обновлённые контактные данные
	 */
	@RequestMapping(
		method = RequestMethod.PUT,
		consumes = MediaUtf8.APPLICATION_JSON_UTF8_VALUE,
		produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ContactsDTO postContacts(Principal principal, @Valid @RequestBody ContactsDTO contactsDto) {
		String login = principal.getName();
		String newPhone = contactsDto.getPhone();
		String newAddress = contactsDto.getAddress();
		Contacts updatedContacts = contactsService.updateUserContacts(login, newPhone, newAddress);
		return contactsDtoAssembler.toModel(updatedContacts);
	}
}
