package engestofte.domain.event.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventContactRequestDTO {

	// Expected JSON Input
	// ___________________
	//
	//		{ "email": "planner@example.com" }
	//
	// ____________________
	// Tested: NO
	// Last Tested: N/A

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@JsonProperty("email")
	private String email;
}
