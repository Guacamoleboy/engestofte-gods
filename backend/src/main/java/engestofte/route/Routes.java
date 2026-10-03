package engestofte.route;

import engestofte.domain.aiflow.route.AiFlowRouting;
import engestofte.domain.auth.route.AuthRouting;
import engestofte.domain.enquiry.route.EnquiryRouting;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

public class Routes {

    // Attributes

    // _______________________________________________________________________

    public static EndpointGroup registerRoutes(EntityManagerFactory entityManagerFactory) {

        // Routings
		AiFlowRouting aiFlowRouting = new AiFlowRouting(entityManagerFactory);
		AuthRouting authRouting = new AuthRouting(entityManagerFactory);
		EnquiryRouting enquiryRouting = new EnquiryRouting(entityManagerFactory);

        // EndpointGroup Return to server
        return () -> {
			aiFlowRouting.routes().addEndpoints();
			authRouting.routes().addEndpoints();
			enquiryRouting.routes().addEndpoints();
        };

    }

}
