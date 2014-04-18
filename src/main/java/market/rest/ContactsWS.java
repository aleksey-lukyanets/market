package market.rest;

import java.security.Principal;
import javax.validation.Valid;
import market.domain.dto.ContactsDTO;
import market.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 */
@Controller
@RequestMapping(value = "/rest/customer/contacts")
public class ContactsWS {
    
    @Autowired
    private ContactsService contactsService;

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ContactsDTO getContacts(Principal principal) {
        String login = principal.getName();
        return contactsService.getUserContacts(login);
    }

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaUtf8.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ContactsDTO postContacts(Principal principal, @Valid @RequestBody ContactsDTO dto) {
        String login = principal.getName();
        return contactsService.updateUserContacts(login, dto);
    }
}
