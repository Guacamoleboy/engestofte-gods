package engestofte.domain.enquiry.route;

import engestofte.domain.enquiry.controller.EnquiryController;
import engestofte.domain.enquiry.service.EnquiryService;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class EnquiryRouting {

	// Attributes
	private final EnquiryController enquiryController;

	// _________________________________________________________________________________________________________________

	public EnquiryRouting(EntityManagerFactory entityManagerFactory) {
		this.enquiryController = new EnquiryController(new EnquiryService(entityManagerFactory));
	}

	// _________________________________________________________________________________________________________________

	public EndpointGroup routes() {
		return () -> path("/enquiries", () -> post("", enquiryController::submit));
	}
}
