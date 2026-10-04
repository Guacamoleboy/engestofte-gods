package engestofte.domain.enquiry.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import engestofte.domain.enquiry.enums.EnquiryStatus;
import lombok.Data;

import java.time.Instant;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnquiryOwnerReviewResponseDTO {

	// Expected JSON Output
	// ____________________
	//
	//		{
	//			"id": 42,
	//			"submission_id": "c02b8f4d-1c7f-4c15-9ee2-b26a8c998fc1",
	//			"language": "da",
	//			"status": "UNDER_REVIEW",
	//			"event_id": null,
	//			"submitted_at": "2026-10-04T12:30:00Z",
	//			"draft": { "conversation": [] },
	//			"ai_assessment": { "summary": "..." },
	//			"internal_note": null,
	//			"customer_question": null
	//		}
	//
	// ____________________
	// Tested: NO
	// Last Tested: N/A

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("submission_id")
	private String submissionId;

	@JsonProperty("language")
	private String language;

	@JsonProperty("status")
	private EnquiryStatus status;

	@JsonProperty("event_id")
	private Integer eventId;

	@JsonProperty("event_approved_at")
	private Instant eventApprovedAt;

	@JsonProperty("submitted_at")
	private Instant submittedAt;

	@JsonProperty("draft")
	private JsonNode draft;

	@JsonProperty("ai_assessment")
	private JsonNode aiAssessment;

	@JsonProperty("internal_note")
	private String internalNote;

	@JsonProperty("customer_question")
	private String customerQuestion;
}
