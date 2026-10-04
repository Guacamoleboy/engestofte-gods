package engestofte.domain.aiflow.provider;

import engestofte.domain.aiflow.dto.request.AiFlowTurnDTO;
import engestofte.domain.aiflow.dto.response.AiEnquiryAssessmentDTO;

import java.util.List;

public interface EnquiryAssessmentProvider {
	AiEnquiryAssessmentDTO assess(List<AiFlowTurnDTO> conversation, String language);
}
