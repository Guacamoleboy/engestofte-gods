package engestofte.domain.event.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import engestofte.domain.event.enums.EventStatus;
import lombok.Data;

import java.time.Instant;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventOwnerResponseDTO {

	// Expected JSON Output
	// ____________________
	//
	//		{
	//			"event_id": 18,
	//			"status": "APPROVED",
	//			"approved_at": "2026-10-04T12:30:00Z",
	//			"customer_name": "Alex Morgan",
	//			"event_name": "Bryllupsevent",
	//			"primary_contact_name": "Alex Morgan",
	//			"customer_email_redacted": "ale....@example.com",
	//			"expected_guest_count": 60,
	//			"requested_date": "June 2027",
	//			"has_allergies": true,
	//			"allergy_details": "Peanuts",
	//			"expected_vegan_count": 2,
	//			"wedding_direction": 1,
	//			"created_at": "2026-10-04T12:30:00Z"
	//		}
	//
	// ____________________
	// Tested: NO
	// Last Tested: N/A

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@JsonProperty("event_id")
	private Integer eventId;

	@JsonProperty("status")
	private EventStatus status;

	@JsonProperty("approved_at")
	private Instant approvedAt;

	@JsonProperty("customer_name")
	private String customerName;

	@JsonProperty("event_name")
	private String eventName;

	@JsonProperty("primary_contact_name")
	private String primaryContactName;

	@JsonProperty("customer_email_redacted")
	private String customerEmailRedacted;

	@JsonProperty("expected_guest_count")
	private Integer expectedGuestCount;

	@JsonProperty("requested_date")
	private String requestedDate;

	@JsonProperty("has_allergies")
	private Boolean hasAllergies;

	@JsonProperty("allergy_details")
	private String allergyDetails;

	@JsonProperty("expected_vegan_count")
	private Integer expectedVeganCount;

	@JsonProperty("wedding_direction")
	private Integer weddingDirection;

	@JsonProperty("created_at")
	private Instant createdAt;
}
