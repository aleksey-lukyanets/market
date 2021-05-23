package market.dto.assembler;

import market.domain.Contacts;
import market.domain.UserAccount;
import market.dto.UserDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public class UserAccountDtoAssembler implements RepresentationModelAssembler<UserAccount, UserDTO> {

	@Override
	public UserDTO toModel(UserAccount userAccount) {
		UserDTO dto = new UserDTO();
		dto.setEmail(userAccount.getEmail());
		dto.setPassword(UserDTO.HIDDEN_PASSWORD);
		dto.setName(userAccount.getName());
		dto.setPhone(userAccount.getContacts().getPhone());
		dto.setAddress(userAccount.getContacts().getAddress());
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
