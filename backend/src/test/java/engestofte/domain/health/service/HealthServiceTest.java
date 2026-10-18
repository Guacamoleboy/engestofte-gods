package engestofte.domain.health.service;

import engestofte.ATest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HealthServiceTest extends ATest {

    // Attributes

    // _________________________________________________________________________________________________________________

	@Test
	void reportsDatabaseConnectivity() {
		assertEquals("Health OK", new HealthService(em).checkHealth());
	}

}