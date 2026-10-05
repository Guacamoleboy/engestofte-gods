package engestofte.domain.event.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import engestofte.domain.event.changeproposal.ChangeApprovalDecision;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeProposalDecisionRequestDTO {

	// Expected JSON Input
	// ___________________
	//
	//		{ "decision": "APPROVED", "explanation": null }
	//
	// ____________________
	// Tested: NO
	// Last Tested: N/A

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@JsonProperty("decision")
	private ChangeApprovalDecision decision;

	@JsonProperty("explanation")
	private String explanation;
}
