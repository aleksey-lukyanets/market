package market.dao;

import market.domain.Contacts;
import market.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ContactsDAO extends CrudRepository<Contacts, Long>, JpaRepository<Contacts, Long> {

	Contacts findByUserAccount(UserAccount userAccount);
}
