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
	//			"expected_guest_count": 60,
	//			"requested_date": "June 2027",
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

	@JsonProperty("expected_guest_count")
	private Integer expectedGuestCount;

	@JsonProperty("requested_date")
	private String requestedDate;

	@JsonProperty("created_at")
	private Instant createdAt;
}
