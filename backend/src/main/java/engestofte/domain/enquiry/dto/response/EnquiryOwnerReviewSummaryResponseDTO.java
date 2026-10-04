package engestofte.domain.enquiry.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import engestofte.domain.enquiry.enums.EnquiryStatus;
import lombok.Data;

import java.time.Instant;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnquiryOwnerReviewSummaryResponseDTO {

	// Expected JSON Output
	// ____________________
	//
	//		{
	//			"id": 42,
	//			"customer_name": "Alex Morgan",
	//			"summary": "A wedding for approximately 60 guests in June.",
	//			"status": "SUBMITTED",
	//			"event_id": null,
	//			"event_approved_at": null,
	//			"submitted_at": "2026-10-04T12:30:00Z"
	//		}
	//
	// ____________________
	// Tested: NO
	// Last Tested: N/A

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("customer_name")
	private String customerName;

	@JsonProperty("summary")
	private String summary;

	@JsonProperty("status")
	private EnquiryStatus status;

	@JsonProperty("event_id")
	private Integer eventId;

	@JsonProperty("event_approved_at")
	private Instant eventApprovedAt;

	@JsonProperty("submitted_at")
	private Instant submittedAt;
}
