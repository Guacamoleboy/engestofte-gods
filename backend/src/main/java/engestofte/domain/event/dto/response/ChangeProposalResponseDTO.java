package engestofte.domain.event.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import engestofte.domain.event.changeproposal.ChangeApprovalParty;
import engestofte.domain.event.changeproposal.ChangeProposalStatus;
import lombok.Data;

import java.time.Instant;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeProposalResponseDTO {

	// Expected JSON Output
	// ____________________
	//
	//		{ "id": 4, "field_name": "customer_name", "old_value": "Camilla", "new_value": "Camilla Jensen",
	//		  "proposer_name": "Camilla", "proposer_party": "CUSTOMER", "status": "PENDING",
	//		  "customer_approved": true, "owner_approved": false, "rejection_explanation": null,
	//		  "created_at": "2026-10-05T12:30:00Z" }
	//
	// ____________________
	// Tested: NO
	// Last Tested: N/A

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("field_name")
	private String fieldName;

	@JsonProperty("old_value")
	private String oldValue;

	@JsonProperty("new_value")
	private String newValue;

	@JsonProperty("proposer_name")
	private String proposerName;

	@JsonProperty("proposer_party")
	private ChangeApprovalParty proposerParty;

	@JsonProperty("status")
	private ChangeProposalStatus status;

	@JsonProperty("customer_approved")
	private boolean customerApproved;

	@JsonProperty("owner_approved")
	private boolean ownerApproved;

	@JsonProperty("rejection_explanation")
	private String rejectionExplanation;

	@JsonProperty("created_at")
	private Instant createdAt;
}
