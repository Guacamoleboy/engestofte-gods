package engestofte.domain.enquiry.route;

import engestofte.domain.enquiry.controller.EnquiryController;
import engestofte.domain.enquiry.service.EnquiryService;
import engestofte.domain.aiflow.provider.AiFlowProviderFactory;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.function.BiConsumer;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.put;
import static io.javalin.apibuilder.ApiBuilder.post;

public class EnquiryRouting {

	// Attributes
	private final EntityManagerFactory entityManagerFactory;

	// _________________________________________________________________________________________________________________

	public EnquiryRouting(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	// _________________________________________________________________________________________________________________

	public EndpointGroup routes() {
		return () -> path("/enquiries", () -> {
			get("", context -> withController(context, EnquiryController::listForAccount));
			post("/{submissionId}/close", context -> withController(context, EnquiryController::closeByCustomer));
			get("/owner", context -> withController(context, EnquiryController::listForOwnerReview));
			get("/owner/{id}", context -> withController(context, EnquiryController::findOwnerReview));
			put("/owner/{id}", context -> withController(context, EnquiryController::saveOwnerReview));
			post("/owner/{id}/approve", context -> withController(context, EnquiryController::approveOwnerEnquiry));
			get("/owner/{id}/messages", context -> withController(context, EnquiryController::findOwnerMessages));
			post("/owner/{id}/messages", context -> withController(context, EnquiryController::sendOwnerMessage));
			post("/owner/{id}/close", context -> withController(context, EnquiryController::closeByOwner));
			post("", context -> withController(context, EnquiryController::submit));
		});
	}

	// _________________________________________________________________________________________________________________

	private void withController(Context context, BiConsumer<EnquiryController, Context> operation) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		try {
			operation.accept(new EnquiryController(new EnquiryService(entityManager, AiFlowProviderFactory.create())), context);
		} finally {
			entityManager.close();
		}
	}
}
