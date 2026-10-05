package engestofte.domain.event.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import engestofte.domain.event.enums.EventMessageSender;
import lombok.Data;

import java.time.Instant;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventMessageResponseDTO {

	// Expected JSON Output
	// ____________________
	//
	//		{ "id": 2, "sender_type": "OWNER", "sender_name": "Johan", "content": "...", "created_at": "2026-10-04T12:30:00Z" }
	//
	// ____________________
	// Tested: NO
	// Last Tested: N/A

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("sender_type")
	private EventMessageSender senderType;

	@JsonProperty("sender_name")
	private String senderName;

	@JsonProperty("is_mine")
	private boolean mine;

	@JsonProperty("content")
	private String content;

	@JsonProperty("created_at")
	private Instant createdAt;
}
