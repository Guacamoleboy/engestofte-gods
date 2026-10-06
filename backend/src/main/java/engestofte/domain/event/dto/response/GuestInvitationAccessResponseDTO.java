package engestofte.domain.event.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GuestInvitationAccessResponseDTO {

	// Expected JSON Output
	// ____________________
	//
	//		{ "access": "opaque-event-access-capability" }
	//
	// ____________________
	// Tested: NO
	// Last Tested: N/A

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@JsonProperty("access")
	private String access;
}
