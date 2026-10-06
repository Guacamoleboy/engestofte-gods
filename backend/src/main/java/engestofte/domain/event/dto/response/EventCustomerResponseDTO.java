package engestofte.domain.event.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import engestofte.domain.event.enums.EventCategory;
import engestofte.domain.event.enums.EventStatus;
import lombok.Data;

import java.time.Instant;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventCustomerResponseDTO {

	// Expected JSON Output
	// ____________________
	//
	//		{
	//			"event_id": 18,
	//			"category": "WEDDING",
	//			"event_data": { "event_name": "Bryllupsevent", "expected_guest_count": 60, "requested_date": "June 2027" },
	//			"status": "APPROVED",
	//			"approved_at": "2026-10-04T12:30:00Z",
	//			"customer_note": "We look forward to helping with your wedding.",
	//			"created_at": "2026-10-04T12:30:00Z",
	//			"customer_email_redacted": "ale....@example.com",
	//			"is_primary_contact": true,
	//			"primary_contact_name": "Alex Morgan"
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

	@JsonProperty("event_data")
	private JsonNode eventData;

	@JsonProperty("customer_note")
	private String customerNote;

	@JsonProperty("created_at")
	private Instant createdAt;

	@JsonProperty("customer_email_redacted")
	private String customerEmailRedacted;

	@JsonProperty("is_primary_contact")
	private boolean primaryContact;

	@JsonProperty("guest_invitation_created")
	private boolean guestInvitationCreated;

	@JsonProperty("primary_contact_name")
	private String primaryContactName;
}
