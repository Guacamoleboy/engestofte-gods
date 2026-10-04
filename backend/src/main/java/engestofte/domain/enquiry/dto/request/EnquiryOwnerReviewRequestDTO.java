package engestofte.domain.enquiry.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnquiryOwnerReviewRequestDTO {

	// Expected JSON Input
	// ___________________
	//
	//		{
	//			"internal_note": "Confirm the requested date.",
	//			"customer_question": "Would you prefer the ceremony indoors or outdoors?"
	//		}
	//
	// ____________________
	// Tested: NO
	// Last Tested: N/A

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@JsonProperty("internal_note")
	private String internalNote;

	@JsonProperty("customer_question")
	private String customerQuestion;
}
