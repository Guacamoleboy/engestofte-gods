package engestofte.domain.enquiry.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import engestofte.domain.enquiry.enums.EnquiryStatus;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnquirySubmissionResponseDTO {

	// Expected JSON Output
	// ____________________
	//
	//		{
	//			"submission_id": "c02b8f4d-1c7f-4c15-9ee2-b26a8c998fc1",
	//			"language": "da",
	//			"status": "SUBMITTED"
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

	@JsonProperty("status")
	private EnquiryStatus status;
}
