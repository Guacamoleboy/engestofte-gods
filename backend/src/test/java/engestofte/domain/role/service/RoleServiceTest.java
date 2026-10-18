package engestofte.domain.role.service;

import engestofte.ATest;
import engestofte.domain.role.entity.Role;
import engestofte.domain.role.enums.RoleName;
import engestofte.testdata.TestJsonData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RoleServiceTest extends ATest {

    // Attributes
	private RoleService service;

	// _________________________________________________________________________________________________________________

	@BeforeEach
	void setupService() {
		service = new RoleService(em);
		service.deleteAll();
	}

	// _________________________________________________________________________________________________________________

	@Test
	void getByNameFindsOnlyTheRequestedRole() {
		Role customer = role("roleCustomer");
		Role owner = role("roleOwner");
		service.create(customer);
		service.create(owner);
		assertEquals(RoleName.CUSTOMER, service.getByName(RoleName.CUSTOMER).getName());
		assertNull(service.getByName(RoleName.STAFF));
	}

	// _________________________________________________________________________________________________________________

	private Role role(String fixtureName) {
		return TestJsonData.read("testdata/data/core.json", fixtureName, Role.class);
	}

}