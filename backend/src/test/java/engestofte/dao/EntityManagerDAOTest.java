package engestofte.dao;

import engestofte.ATest;
import engestofte.domain.role.entity.Role;
import engestofte.domain.role.enums.RoleName;
import engestofte.testdata.TestJsonData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class EntityManagerDAOTest extends ATest {

    // Attributes
	private EntityManagerDAO<Role> dao;

	// _________________________________________________________________________________________________________________

	@BeforeEach
	void setupDAO() {
		dao = new EntityManagerDAO<>(em, Role.class);
		dao.deleteAll();
	}

	// _________________________________________________________________________________________________________________

	@Test
	void createAndReadById() {
		Role created = dao.create(role("roleCustomer"));

		assertNotNull(created.getId());
		assertEquals(RoleName.CUSTOMER, dao.getById(created.getId()).getName());
	}

	// _________________________________________________________________________________________________________________

	@Test
	void createReturnsAnEntityAlreadyManagedByTheEntityManager() {
		Role managed = role("roleCustomer");
		em.getTransaction().begin();
		em.persist(managed);
		em.flush();

		assertSame(managed, dao.create(managed));
		assertTrue(em.getTransaction().isActive());
		em.getTransaction().commit();
	}

	// _________________________________________________________________________________________________________________

	@Test
	void updateAndDeleteById() {
		Role created = dao.create(role("roleCustomer"));
		created.setName(RoleName.STAFF);
		Role updated = dao.update(created);

		assertEquals(RoleName.STAFF, updated.getName());
		assertEquals(RoleName.STAFF, dao.deleteById(created.getId()).getName());
		assertNull(dao.getById(created.getId()));
	}

	// _________________________________________________________________________________________________________________

	@Test
	void deleteRemovesTheSuppliedEntity() {
		Role created = dao.create(role("roleCustomer"));

		assertEquals(created.getId(), dao.delete(created).getId());
		assertNull(dao.getById(created.getId()));
	}

	// _________________________________________________________________________________________________________________

	@Test
	void readsAndDeletesReturnNullWhenTheIdDoesNotExist() {
		assertNull(dao.getById(-1));
		assertNull(dao.deleteById(-1));
	}

	// _________________________________________________________________________________________________________________

	@Test
	void queryMethodsFindAndUpdateByColumn() {
		Role customer = dao.create(role("roleCustomer"));
		dao.create(role("roleOwner"));

		assertEquals(2, dao.getAll().size());
		assertEquals(customer.getId(), dao.findEntityByColumn(RoleName.CUSTOMER, Role.Fields.NAME).getId());
		assertTrue(dao.existByColumn(RoleName.CUSTOMER, Role.Fields.NAME));
		assertFalse(dao.existByColumn(RoleName.STAFF, Role.Fields.NAME));
		assertEquals(RoleName.CUSTOMER, dao.getColumnById(customer.getId(), Role.Fields.NAME));
		assertEquals(1, dao.updateColumnById(customer.getId(), Role.Fields.NAME, RoleName.STAFF));
		em.clear();
		assertEquals(RoleName.STAFF, dao.getById(customer.getId()).getName());
		assertEquals(0, dao.updateColumnById(-1, Role.Fields.NAME, RoleName.CUSTOMER));
		dao.deleteById(customer.getId());
		assertNull(dao.findEntityByColumn(RoleName.CUSTOMER, Role.Fields.NAME));
	}

	// _________________________________________________________________________________________________________________

	@Test
	void deleteAllSupportsBulkAndEntityRemoval() {
		dao.create(role("roleCustomer"));
		dao.create(role("roleStaff"));

		assertEquals(2, dao.deleteAll());
		assertTrue(dao.getAll().isEmpty());

		dao.create(role("roleCustomer"));
		dao.create(role("roleStaff"));
		assertEquals(2, dao.deleteAllSafe());
		assertTrue(dao.getAll().isEmpty());
	}

	// _________________________________________________________________________________________________________________

	@Test
	void executeQueryManagesTransactionAndReturnsQueryResult() {
		Role role = role("roleCustomer");
		Role created = dao.executeQuery(() -> dao.create(role));

		assertNotNull(created.getId());
		assertEquals(RoleName.CUSTOMER, dao.getById(created.getId()).getName());
	}

	// _________________________________________________________________________________________________________________

	@Test
	void executeQuerySupportsRunnableAndLeavesAnExistingTransactionOpen() {
		Role role = role("roleCustomer");
		em.getTransaction().begin();
		dao.executeQuery((Runnable) () -> dao.create(role));

		assertTrue(em.getTransaction().isActive());
		assertEquals(RoleName.CUSTOMER, dao.getById(role.getId()).getName());
		em.getTransaction().commit();
	}

	// _________________________________________________________________________________________________________________

	@Test
	void executeQueryRollsBackItsOwnTransactionWhenTheOperationFails() {
		assertThrows(IllegalStateException.class, () -> dao.executeQuery(() -> {
			dao.create(role("roleCustomer"));
			throw new IllegalStateException("simulated failure");
		}));

		assertFalse(em.getTransaction().isActive());
		assertTrue(dao.getAll().isEmpty());
	}

	// _________________________________________________________________________________________________________________

	@Test
	void getByIdRejectsUnsupportedIdentifierTypes() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> dao.getById(1L));
		assertTrue(exception.getMessage().contains("Unsupported ID type"));
		assertFalse(em.getTransaction().isActive());
	}

	// _________________________________________________________________________________________________________________

	@Test
	void getByIdPassesStringAndUuidIdentifiersToEntityManager() {
		List<Object> identifiers = new ArrayList<>();
		EntityTransaction transaction = (EntityTransaction) Proxy.newProxyInstance(
				EntityTransaction.class.getClassLoader(), new Class<?>[] { EntityTransaction.class },
				new TransactionHandler());
		EntityManager entityManager = (EntityManager) Proxy.newProxyInstance(
				EntityManager.class.getClassLoader(), new Class<?>[] { EntityManager.class },
				(proxy, method, args) -> {
					if (method.getName().equals("getTransaction")) return transaction;
					if (method.getName().equals("find")) {
						identifiers.add(args[1]);
						return null;
					}
					return null;
				});
		EntityManagerDAO<Role> identifierDAO = new EntityManagerDAO<>(entityManager, Role.class);
		UUID uuid = UUID.fromString("a76e2e30-12df-4d94-9f6b-8ac4f3f4dd48");

		assertNull(identifierDAO.getById("role-id"));
		assertNull(identifierDAO.getById(uuid));
		assertEquals(List.of("role-id", uuid), identifiers);
	}

	// _________________________________________________________________________________________________________________

	private Role role(String fixtureName) {
		return TestJsonData.read("testdata/data/core.json", fixtureName, Role.class);
	}

    // _________________________________________________________________________________________________________________

	private static class TransactionHandler implements InvocationHandler {
		private boolean active;
		@Override
		public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) {
			return switch (method.getName()) {
				case "isActive" -> active;
				case "begin" -> { active = true; yield null; }
				case "commit", "rollback" -> { active = false; yield null; }
				default -> null;
			};
		}
	}

}