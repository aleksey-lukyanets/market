package market.dto.assembler;

import market.domain.Contacts;
import market.domain.UserAccount;
import market.dto.UserDTO;
import market.rest.CartRestController;
import market.rest.ContactsRestController;
import market.rest.CustomerRestController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class UserAccountDtoAssembler extends RepresentationModelAssemblerSupport<UserAccount, UserDTO> {

	public UserAccountDtoAssembler() {
		super(CustomerRestController.class, UserDTO.class);
	}

	@Override
	public UserDTO toModel(UserAccount userAccount) {
		UserDTO dto = instantiateModel(userAccount);
		dto.setEmail(userAccount.getEmail());
		dto.setPassword(UserDTO.HIDDEN_PASSWORD);
		dto.setName(userAccount.getName());
		dto.setPhone(userAccount.getContacts().getPhone());
		dto.setAddress(userAccount.getContacts().getAddress());
		dto.add(linkTo(CartRestController.class).withRel("Shopping cart"));
		dto.add(linkTo(ContactsRestController.class).withRel("Manage contacts"));
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
