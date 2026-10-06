package engestofte.domain.event.route;

import engestofte.domain.event.controller.EventController;
import engestofte.domain.event.service.EventService;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.function.BiConsumer;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.path;

public class EventRouting {

	// Attributes
	private final EntityManagerFactory entityManagerFactory;

	// _________________________________________________________________________________________________________________

	public EventRouting(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	// _________________________________________________________________________________________________________________

	public EndpointGroup routes() {
		return () -> path("/events", () -> {
			get("/owner/important-messages", context -> withController(context, EventController::findImportantMessagesForOwner));
			get("/important-messages", context -> withController(context, EventController::findImportantMessagesForAccount));
			get("/owner/{id}", context -> withController(context, EventController::findForOwner));
			get("/owner/{id}/change-proposals", context -> withController(context, EventController::findChangeProposalsForOwner));
			post("/owner/{id}/change-proposals", context -> withController(context, EventController::proposeChangeForOwner));
			post("/owner/{id}/change-proposals/{proposalId}/decision", context -> withController(context, EventController::decideChangeForOwner));
			post("/owner/{id}/contacts", context -> withController(context, EventController::addContactForOwner));
			post("/owner/{id}/close", context -> withController(context, EventController::closeByOwner));
			get("/staff", context -> withController(context, EventController::findAllForStaff));
			get("/staff/important-messages", context -> withController(context, EventController::findImportantMessagesForStaff));
			get("/staff/{id}", context -> withController(context, EventController::findForStaff));
			post("/staff/{id}/messages", context -> withController(context, EventController::sendStaffMessage));
			get("/owner/{id}/messages", context -> withController(context, EventController::findMessagesForOwner));
			post("/owner/{id}/messages", context -> withController(context, EventController::sendOwnerMessage));
			get("/{id}", context -> withController(context, EventController::findForAccount));
			get("/{id}/change-proposals", context -> withController(context, EventController::findChangeProposalsForAccount));
			post("/{id}/change-proposals", context -> withController(context, EventController::proposeChangeForAccount));
			post("/{id}/change-proposals/{proposalId}/decision", context -> withController(context, EventController::decideChangeForAccount));
			post("/{id}/contacts", context -> withController(context, EventController::addContactForCustomer));
			get("/{id}/messages", context -> withController(context, EventController::findMessagesForAccount));
			post("/{id}/messages", context -> withController(context, EventController::sendCustomerMessage));
			post("/{id}/close", context -> withController(context, EventController::closeByCustomer));
		});
	}

	// _________________________________________________________________________________________________________________

	private void withController(Context context, BiConsumer<EventController, Context> operation) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		try {
			operation.accept(new EventController(new EventService(entityManager)), context);
		} finally {
			entityManager.close();
		}
	}
}
