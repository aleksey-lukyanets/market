package market.dto.assembler;

import market.domain.Contacts;
import market.domain.UserAccount;
import market.dto.UserDTO;
import market.rest.CartRestController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 *
 */
@Component
public class UserAccountDtoAssembler extends RepresentationModelAssemblerSupport<UserAccount, UserDTO> {

	public UserAccountDtoAssembler() {
		super(CartRestController.class, UserDTO.class);
	}

	@Override
	public UserDTO toModel(UserAccount userAccount) {
		UserDTO dto = createModelWithId(userAccount.getId(), userAccount);
		dto.setEmail(userAccount.getEmail());
		dto.setPassword("hidden");
		dto.setName(userAccount.getName());
		dto.setPhone(userAccount.getContacts().getPhone());
		dto.setAddress(userAccount.getContacts().getAddress());
		dto.add(linkTo(CartRestController.class).withRel("Shopping cart"));
		return dto;
	}

	public UserAccount toDomain(UserDTO user) {
		UserAccount userAccount = new UserAccount.Builder()
			.setEmail(user.getEmail())
			.setPassword(user.getPassword())
			.setName(user.getName())
			.setActive(true)
			.build();
		Contacts contacts = new Contacts.Builder()
			.setUserAccount(userAccount)
			.setPhone(user.getPhone())
			.setAddress(user.getAddress())
			.build();
		userAccount.setContacts(contacts);
		return userAccount;
	}
}
