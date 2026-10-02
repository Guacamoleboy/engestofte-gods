package engestofte.domain.aiflow.route;

import engestofte.crud.CRUDRouting;
import engestofte.domain.aiflow.controller.AiFlowController;
import engestofte.domain.aiflow.entity.AiFlow;
import engestofte.domain.aiflow.service.AiFlowService;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class AiFlowRouting extends CRUDRouting<AiFlow> {

	// Attributes

	// _________________________________________________________________________________________________________________

	public AiFlowRouting(EntityManagerFactory emf) {
		super("/ai-flow", createController(emf));
	}

	// _________________________________________________________________________________________________________________

	@Override
	public EndpointGroup routes() {
		AiFlowController aiFlowController = (AiFlowController) controller;
		return () -> path(basePath, () -> post("/interaction", aiFlowController::interact));
	}

	// _________________________________________________________________________________________________________________

	private static AiFlowController createController(EntityManagerFactory emf) {
		return new AiFlowController(new AiFlowService(emf.createEntityManager()));
	}

}