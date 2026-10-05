package engestofte.domain.useraccount.dao;

import engestofte.dao.EntityManagerDAO;
import engestofte.domain.useraccount.entity.UserAccount;
import engestofte.domain.role.enums.RoleName;
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

	// _________________________________________________________________________________________________________________

	public UserAccount findByEmailIgnoreCase(String email) {
		return executeQuery(() -> em.createQuery("SELECT account FROM UserAccount account WHERE LOWER(account.email) = :email AND account.role.name = :role", UserAccount.class)
			.setParameter("email", email.toLowerCase(java.util.Locale.ROOT))
			.setParameter("role", RoleName.CUSTOMER)
			.getResultStream().findFirst().orElse(null));
	}
}
