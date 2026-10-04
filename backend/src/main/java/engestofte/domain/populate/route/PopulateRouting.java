package engestofte.domain.populate.route;

import engestofte.domain.populate.controller.PopulateController;
import engestofte.domain.populate.PopulateDB;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class PopulateRouting {

	// Attributes
	private final PopulateController populateController;

	// _________________________________________________________________________________________________________________

	public PopulateRouting(EntityManagerFactory entityManagerFactory) {
		this.populateController = new PopulateController(new PopulateDB(entityManagerFactory));
	}

	// _________________________________________________________________________________________________________________

	public EndpointGroup routes() {
		return () -> path("/populate", () -> {
			post("/reset", populateController::restart);
			post("/roles", populateController::populateRoles);
			post("/owners", populateController::createOwners);
		});
	}
}
