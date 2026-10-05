package engestofte.domain.event.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImportantMessageResponseDTO {

	// Expected JSON Output
	// ____________________
	//
	//		{ "event_id": 18, "event_name": "Bryllupsevent", "unread_count": 2, "latest_message": "...", "latest_message_at": "2026-10-04T12:30:00Z" }
	//
	// ____________________
	// Tested: NO
	// Last Tested: N/A

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@JsonProperty("event_id")
	private Integer eventId;

	@JsonProperty("event_name")
	private String eventName;

	@JsonProperty("unread_count")
	private long unreadCount;

	@JsonProperty("latest_message")
	private String latestMessage;

	@JsonProperty("latest_message_at")
	private Instant latestMessageAt;
}
