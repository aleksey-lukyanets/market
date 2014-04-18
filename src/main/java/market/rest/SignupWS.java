package market.rest;

import javax.validation.Valid;
import market.domain.UserAccount;
import market.domain.dto.UserDTO;
import market.exception.EmailExistsException;
import market.service.UserAccountService;
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
@RequestMapping(value = "/rest/signup")
public class SignupWS {

    @Autowired
    private UserAccountService userAccountService;

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaUtf8.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public UserDTO postNewUser(@Valid @RequestBody UserDTO dto) throws EmailExistsException {
        UserAccount user = userAccountService.createUserThenAuthenticate(dto);
        return user.createDTO();
    }
}
