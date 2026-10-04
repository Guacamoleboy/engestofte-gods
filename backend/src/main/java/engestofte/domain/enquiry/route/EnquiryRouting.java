package engestofte.domain.enquiry.route;

import engestofte.domain.enquiry.controller.EnquiryController;
import engestofte.domain.enquiry.service.EnquiryService;
import engestofte.domain.aiflow.provider.AiFlowProviderFactory;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.put;

public class EnquiryRouting {

	// Attributes
	private final EnquiryController enquiryController;

	// _________________________________________________________________________________________________________________

	public EnquiryRouting(EntityManagerFactory entityManagerFactory) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		this.enquiryController = new EnquiryController(new EnquiryService(entityManager, AiFlowProviderFactory.create()));
	}

	// _________________________________________________________________________________________________________________

	public EndpointGroup routes() {
		return () -> path("/enquiries", () -> {
			get("", enquiryController::listForAccount);
			get("/owner", enquiryController::listForOwnerReview);
			get("/owner/{id}", enquiryController::findOwnerReview);
			put("/owner/{id}", enquiryController::saveOwnerReview);
			post("", enquiryController::submit);
		});
	}
}
