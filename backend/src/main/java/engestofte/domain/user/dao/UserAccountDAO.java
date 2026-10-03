package engestofte.domain.user.dao;

import engestofte.dao.EntityManagerDAO;
import engestofte.domain.user.entity.UserAccount;
import jakarta.persistence.EntityManager;

public class UserAccountDAO extends EntityManagerDAO<UserAccount> {

	// Attributes

	// _________________________________________________________________________________________________________________

	public UserAccountDAO(EntityManager em) {
		super(em, UserAccount.class);
	}

	// _________________________________________________________________________________________________________________

	public UserAccount findByEmail(String email) {
		return findEntityByColumn(email, UserAccount.Fields.EMAIL);
	}
}
