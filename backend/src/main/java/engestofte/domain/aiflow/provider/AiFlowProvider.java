package engestofte.domain.aiflow.provider;

import engestofte.domain.aiflow.dto.request.AiFlowRequestDTO;
import engestofte.domain.aiflow.dto.response.AiFlowResponseDTO;

// Purpose of this class:
// ----------------------
//
// - N/A

public interface AiFlowProvider {
	AiFlowResponseDTO respond(AiFlowRequestDTO request);
}