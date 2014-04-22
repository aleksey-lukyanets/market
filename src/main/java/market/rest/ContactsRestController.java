package market.rest;

import java.security.Principal;
import javax.validation.Valid;
import market.dto.ContactsDTO;
import market.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * REST-контроллер контактных данных покупателя.
 */
@Controller
@RequestMapping(value = "/rest/customer/contacts")
public class ContactsRestController {
    
    @Autowired
    private ContactsService contactsService;

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
        return contactsService.getUserContacts(login);
    }

    /**
     * Изменение контактов покупателя.
     *
     * @param contacts новые контактные данные
     * @param principal
     * @return обновлённые контактные данные
     */
    @RequestMapping(
            method = RequestMethod.PUT,
            consumes = MediaUtf8.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ContactsDTO postContacts(Principal principal, @Valid @RequestBody ContactsDTO contacts) {
        String login = principal.getName();
        return contactsService.updateUserContacts(login, contacts);
    }
}
