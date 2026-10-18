package engestofte;

import engestofte.config.HibernateConfig;
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class ATest {

	// Attributes
	protected static EntityManagerFactory emf;
	protected EntityManager em;
	private static Javalin restApp;

	// _________________________________________________________________________________________________________________

	@BeforeAll
	protected static void setupTestDatabase() {
		System.setProperty("set.env", "test");
		emf = HibernateConfig.getEntityManagerFactoryForTest();
	}

	// _________________________________________________________________________________________________________________

	@BeforeEach
	protected void setupEntityManager() {
		em = emf.createEntityManager();
	}

	// _________________________________________________________________________________________________________________

	@AfterEach
	protected void closeEntityManager() {
		if (em != null && em.isOpen()) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}
	}

	// _________________________________________________________________________________________________________________

	@AfterAll
	protected static void stopTestServer() {
		if (restApp != null) {
			restApp.stop();
			restApp = null;
		}
		RestAssured.reset();
	}

	// _________________________________________________________________________________________________________________

	protected void startServer(EndpointGroup routes) {
		if (restApp == null) {
			restApp = Javalin.create(config -> {
				config.router.contextPath = "/v1";
				config.routes.apiBuilder(routes);
			}).start(0);
		}
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = restApp.port();
		RestAssured.basePath = "/v1";
	}

}