package engestofte.domain.event.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventOperationalDetailResponseDTO {

	// Expected JSON Output
	// ____________________
	//
	//		{ "question": "Any allergies or dietary requirements?", "answer": "Two guests are vegan." }
	//
	// ____________________
	// Tested: NO
	// Last Tested: N/A

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@JsonProperty("question")
	private String question;

	@JsonProperty("answer")
	private String answer;
}
