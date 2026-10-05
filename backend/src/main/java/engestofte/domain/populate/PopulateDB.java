package engestofte.domain.populate;

import engestofte.domain.role.dao.RoleDAO;
import engestofte.domain.role.entity.Role;
import engestofte.domain.role.enums.RoleName;
import engestofte.domain.populate.dao.PopulateDAO;
import engestofte.domain.useraccount.dao.UserAccountDAO;
import engestofte.domain.useraccount.entity.UserAccount;
import engestofte.util.BCryptHash;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.function.Function;

public class PopulateDB {

	// Attributes
	private final EntityManagerFactory entityManagerFactory;
	private static final String TEST_ACCOUNT_PASSWORD = "Password12345!";
	private static final List<OwnerAccount> OWNER_ACCOUNTS = List.of(
			new OwnerAccount("Johan", "johan@johan.dk"),
			new OwnerAccount("Lise", "lise@lise.dk"),
			new OwnerAccount("Mette", "mette@mette.dk")
	);
	private static final List<StaffAccount> STAFF_ACCOUNTS = List.of(
			new StaffAccount("Kok", "kok@kok.dk"),
			new StaffAccount("Clean", "clean@clean.dk")
	);

	// _________________________________________________________________________________________________________________

	public PopulateDB(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	// _________________________________________________________________________________________________________________

	public void populateRoles() {
		withEntityManager(entityManager -> {
			populateRoles(entityManager);
			return null;
		});
	}

	private void populateRoles(EntityManager entityManager) {
		RoleDAO roleDAO = new RoleDAO(entityManager);
		for (RoleName roleName : RoleName.values()) {
			if (roleDAO.getByName(roleName) != null) continue;
			Role role = new Role();
			role.setName(roleName);
			roleDAO.create(role);
		}
	}

	// _________________________________________________________________________________________________________________

	public void createOwners() {
		withEntityManager(entityManager -> {
			createOwners(entityManager);
			return null;
		});
	}

	// _________________________________________________________________________________________________________________

	public void staff() {
		withEntityManager(entityManager -> {
			populateRoles(entityManager);
			Role staffRole = new RoleDAO(entityManager).getByName(RoleName.STAFF);
			UserAccountDAO userAccountDAO = new UserAccountDAO(entityManager);

			for (StaffAccount staffDetails : STAFF_ACCOUNTS) {
				UserAccount account = userAccountDAO.findByEmail(staffDetails.email());
				if (account == null) {
					account = new UserAccount();
					account.setFullName(staffDetails.fullName());
					account.setEmail(staffDetails.email());
					account.setPasswordHash(BCryptHash.hash(TEST_ACCOUNT_PASSWORD));
					account.setRole(staffRole);
					userAccountDAO.create(account);
					continue;
				}
				account.setFullName(staffDetails.fullName());
				account.setRole(staffRole);
				userAccountDAO.update(account);
			}
		});
	}

	private void createOwners(EntityManager entityManager) {
		populateRoles(entityManager);
		Role ownerRole = new RoleDAO(entityManager).getByName(RoleName.OWNER);
		UserAccountDAO userAccountDAO = new UserAccountDAO(entityManager);

		for (OwnerAccount ownerDetails : OWNER_ACCOUNTS) {
			UserAccount account = userAccountDAO.findByEmail(ownerDetails.email());
			if (account == null) {
				account = new UserAccount();
				account.setFullName(ownerDetails.fullName());
				account.setEmail(ownerDetails.email());
				account.setPasswordHash(BCryptHash.hash(TEST_ACCOUNT_PASSWORD));
				account.setRole(ownerRole);
				userAccountDAO.create(account);
				continue;
			}
			account.setRole(ownerRole);
			userAccountDAO.update(account);
		}
	}

	public String restart() {
		return withEntityManager(entityManager -> new PopulateDAO(entityManager).restart());
	}

	private <T> T withEntityManager(Function<EntityManager, T> action) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		try {
			return action.apply(entityManager);
		} finally {
			entityManager.close();
		}
	}

	private record OwnerAccount(String fullName, String email) { }
	private record StaffAccount(String fullName, String email) { }
}
