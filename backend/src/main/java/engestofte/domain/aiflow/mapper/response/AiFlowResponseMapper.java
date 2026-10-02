package engestofte.domain.aiflow.mapper.response;

import engestofte.domain.aiflow.dto.response.AiFlowResponseDTO;
import engestofte.domain.aiflow.entity.AiFlow;

public class AiFlowResponseMapper {

	// _________________________________________________________________________________________________________________

	// Maps AiFlow entity to AiFlowResponseDTO
	// ______________________________________
	//
	//		AiFlow
	//			↓
	//		AiFlowResponseMapper
	//			↓
	//		AiFlowResponseDTO
	//
	// ____________________
	// Tested: NO
	// Last Tested: N/A

	// _________________________________________________________________________________________________________________

	public static AiFlowResponseDTO toDTO(AiFlow aiFlow) {
		AiFlowResponseDTO dto = new AiFlowResponseDTO();
		dto.setId(aiFlow.getId());
		dto.setAcknowledgement(aiFlow.getAcknowledgement());
		dto.setNextQuestion(aiFlow.getNextQuestion());
		dto.setCustomerName(aiFlow.getCustomerName());
		dto.setStep(aiFlow.getStep());
		dto.setStatus(aiFlow.getStatus());
		return dto;
	}
	
}
