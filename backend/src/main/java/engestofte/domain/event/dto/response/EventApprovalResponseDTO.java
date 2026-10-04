package engestofte.domain.event.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import engestofte.domain.event.enums.EventStatus;
import lombok.Data;

import java.time.Instant;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventApprovalResponseDTO {

	// Expected JSON Output
	// ____________________
	//
	//		{ "event_id": 18, "status": "APPROVED", "created_at": "2026-10-04T12:30:00Z" }
	//
	// ____________________
	// Tested: NO
	// Last Tested: N/A

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@JsonProperty("event_id")
	private Integer eventId;

	@JsonProperty("status")
	private EventStatus status;

	@JsonProperty("created_at")
	private Instant createdAt;
}
