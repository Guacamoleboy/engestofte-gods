package engestofte.domain.event.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import engestofte.domain.event.enums.EventCategory;
import engestofte.domain.event.enums.EventStatus;
import lombok.Data;

import java.time.Instant;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventOperationalResponseDTO {

	// Expected JSON Output
	// ____________________
	//
	//		{
	//			"event_id": 18,
	//			"category": "WEDDING",
	//			"status": "APPROVED",
	//			"approved_at": "2026-10-04T12:30:00Z",
	//			"event_name": "Bryllupsevent",
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

	@JsonProperty("category")
	private EventCategory category;

	@JsonProperty("status")
	private EventStatus status;

	@JsonProperty("approved_at")
	private Instant approvedAt;

	@JsonProperty("customer_name")
	private String customerName;

	@JsonProperty("event_name")
	private String eventName;

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
