package engestofte.domain.event.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import engestofte.domain.event.enums.EventCategory;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GuestInvitationResponseDTO {

	// Expected JSON Output
	// ____________________
	//
	//		{
	//			"event_id": 18,
	//			"category": "WEDDING",
	//			"event_name": "Camilla & Emil",
	//			"requested_date": "June 2027"
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

	@JsonProperty("event_name")
	private String eventName;

	@JsonProperty("requested_date")
	private String requestedDate;
}
