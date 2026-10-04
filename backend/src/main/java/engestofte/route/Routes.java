package engestofte.route;

import engestofte.domain.aiflow.route.AiFlowRouting;
import engestofte.domain.auth.route.AuthRouting;
import engestofte.domain.enquiry.route.EnquiryRouting;
import engestofte.domain.event.route.EventRouting;
import engestofte.config.DotEnv;
import engestofte.domain.populate.route.PopulateRouting;
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
		EventRouting eventRouting = new EventRouting(entityManagerFactory);
		PopulateRouting populateRouting = DotEnv.isDevelopment() ? new PopulateRouting(entityManagerFactory) : null;

        // EndpointGroup Return to server
        return () -> {
			aiFlowRouting.routes().addEndpoints();
			authRouting.routes().addEndpoints();
			enquiryRouting.routes().addEndpoints();
			eventRouting.routes().addEndpoints();
			if (populateRouting != null) populateRouting.routes().addEndpoints();
        };

    }

}
