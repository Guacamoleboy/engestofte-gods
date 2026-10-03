package engestofte.domain.enquiry.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnquirySubmissionRequestDTO {

	// Expected JSON Input
	// ___________________
	//
	//		{
	//			"submission_id": "c02b8f4d-1c7f-4c15-9ee2-b26a8c998fc1",
	//			"language": "da",
	//			"draft": { "isComplete": true }
	//		}
	//
	// ____________________
	// Tested: NO
	// Last Tested: N/A

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@JsonProperty("submission_id")
	private String submissionId;

	@JsonProperty("language")
	private String language;

	@JsonProperty("draft")
	private JsonNode draft;
}
