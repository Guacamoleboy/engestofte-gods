package engestofte.domain.aiflow.mapper.request;

import engestofte.domain.aiflow.dto.request.AiFlowRequestDTO;
import engestofte.domain.aiflow.entity.AiFlow;

public class AiFlowRequestMapper {

	// _________________________________________________________________________________________________________________

	// Maps AiFlowRequestDTO to AiFlow entity
	// _____________________________________
	//
	//		AiFlowRequestDTO
	//				↓
	//		AiFlowRequestMapper
	//				↓
	//		AiFlow
	//
	// ____________________
	// Tested: NO
	// Last Tested: N/A

	// _________________________________________________________________________________________________________________

	public static AiFlow toEntity(AiFlowRequestDTO dto) {
		return AiFlow.builder()
				.answer(dto.getAnswer())
				.currentQuestion(dto.getCurrentQuestion())
				.customerName(dto.getCustomerName())
				.language(dto.getLanguage())
				.build();
	}
	
}
