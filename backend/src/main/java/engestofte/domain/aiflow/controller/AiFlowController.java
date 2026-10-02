package engestofte.domain.aiflow.controller;

import engestofte.crud.CRUDController;
import engestofte.domain.aiflow.dto.request.AiFlowRequestDTO;
import engestofte.domain.aiflow.entity.AiFlow;
import engestofte.domain.aiflow.mapper.response.AiFlowResponseMapper;
import engestofte.domain.aiflow.service.AiFlowService;
import engestofte.service.EntityManagerService;
import engestofte.util.TryCatchHelper;
import io.javalin.http.Context;

public class AiFlowController extends CRUDController<AiFlow> {

	// Attributes

	// _________________________________________________________________________________________________________________

	public AiFlowController(EntityManagerService<AiFlow> service) {
		super(service, AiFlow.class, AiFlowResponseMapper::toDTO);
	}

	// _________________________________________________________________________________________________________________

	@Override
	public void create(Context context) {
		interact(context);
	}

	// _________________________________________________________________________________________________________________

	public void interact(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			AiFlowRequestDTO request = context.bodyAsClass(AiFlowRequestDTO.class);
			AiFlow created = ((AiFlowService) classService).interact(request);
			return AiFlowResponseMapper.toDTO(created);
		}, "AI flow interaction saved");
	}

}