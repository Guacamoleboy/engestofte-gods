package engestofte.route;

import engestofte.domain.aiflow.route.AiFlowRouting;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

public class Routes {

    // Attributes

    // _______________________________________________________________________

    public static EndpointGroup registerRoutes(EntityManagerFactory entityManagerFactory) {

        // Routings
        AiFlowRouting aiFlowRouting = new AiFlowRouting(entityManagerFactory);

        // EndpointGroup Return to server
        return () -> {
            aiFlowRouting.routes().addEndpoints();
        };

    }

}