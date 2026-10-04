package engestofte.domain.event.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventMessageRequestDTO {

	// Expected JSON Input
	// ___________________
	//
	//		{ "content": "Could you clarify the requested date?" }
	//
	// ____________________
	// Tested: NO
	// Last Tested: N/A

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@JsonProperty("content")
	private String content;
}
