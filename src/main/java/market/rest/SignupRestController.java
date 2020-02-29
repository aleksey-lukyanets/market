package market.rest;

import market.domain.UserAccount;
import market.dto.UserDTO;
import market.dto.assembler.UserAccountDtoAssembler;
import market.exception.EmailExistsException;
import market.security.AuthenticationService;
import market.service.UserAccountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/rest/signup")
public class SignupRestController {

	private final UserAccountService userAccountService;
	private final UserAccountDtoAssembler userAccountDtoAssembler;
	private final AuthenticationService authenticationService;

	public SignupRestController(UserAccountService userAccountService, UserAccountDtoAssembler userAccountDtoAssembler,
		AuthenticationService authenticationService) {
		this.userAccountService = userAccountService;
		this.userAccountDtoAssembler = userAccountDtoAssembler;
		this.authenticationService = authenticationService;
	}

	/**
	 * New account registration.
	 *
	 * @return newly created account
	 * @throws EmailExistsException if the account with the specified email already exists
	 */
	@RequestMapping(
		method = RequestMethod.POST,
		consumes = MediaUtf8.APPLICATION_JSON_UTF8_VALUE,
		produces = MediaUtf8.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public UserDTO postNewUser(@Valid @RequestBody UserDTO user) throws EmailExistsException {
		UserAccount userData = userAccountDtoAssembler.toDomain(user);
		UserAccount newAccount = userAccountService.create(userData);
		authenticationService.authenticate(newAccount);
		return userAccountDtoAssembler.toModel(newAccount);
	}
}
