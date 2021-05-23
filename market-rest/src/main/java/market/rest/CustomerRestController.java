package market.rest;

import market.domain.UserAccount;
import market.dto.UserDTO;
import market.dto.assembler.UserAccountDtoAssembler;
import market.exception.EmailExistsException;
import market.security.AuthenticationService;
import market.service.UserAccountService;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@ExposesResourceFor(UserDTO.class)
public class CustomerRestController {

	private final UserAccountService userAccountService;
	private final AuthenticationService authenticationService;
	private final UserAccountDtoAssembler userAccountDtoAssembler = new UserAccountDtoAssembler();

	public CustomerRestController(UserAccountService userAccountService, AuthenticationService authenticationService) {
		this.userAccountService = userAccountService;
		this.authenticationService = authenticationService;
	}

	@GetMapping("/customer")
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	public UserDTO getCustomer(Principal principal) {
		UserAccount newAccount = userAccountService.findByEmail(principal.getName());
		return toDto(newAccount);
	}

	/**
	 * New customer registration.
	 *
	 * @return newly created account, already authenticated
	 * @throws EmailExistsException if the account with the specified email already exists
	 */
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public UserDTO createCustomer(@RequestBody @Valid UserDTO user) {
		UserAccount userData = userAccountDtoAssembler.toDomain(user);
		UserAccount newAccount = userAccountService.create(userData);
		authenticationService.authenticate(newAccount.getEmail(), user.getPassword());
		return toDto(newAccount);
	}

	private UserDTO toDto(UserAccount newAccount) {
		UserDTO dto = userAccountDtoAssembler.toModel(newAccount);
		dto.add(linkTo(CartRestController.class).withRel("Shopping cart"));
		dto.add(linkTo(getClass()).withRel("Manage contacts"));
		return dto;
	}
}
