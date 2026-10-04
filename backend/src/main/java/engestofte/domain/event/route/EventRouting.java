package engestofte.domain.event.route;

import engestofte.domain.event.controller.EventController;
import engestofte.domain.event.service.EventService;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.path;

public class EventRouting {

	// Attributes
	private final EventController eventController;

	// _________________________________________________________________________________________________________________

	public EventRouting(EntityManagerFactory entityManagerFactory) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		this.eventController = new EventController(new EventService(entityManager));
	}

	// _________________________________________________________________________________________________________________

	public EndpointGroup routes() {
		return () -> path("/events", () -> {
			get("/{id}", eventController::findForAccount);
			get("/{id}/messages", eventController::findMessagesForAccount);
			post("/{id}/messages", eventController::sendCustomerMessage);
			post("/{id}/close", eventController::closeByCustomer);
		});
	}
}
