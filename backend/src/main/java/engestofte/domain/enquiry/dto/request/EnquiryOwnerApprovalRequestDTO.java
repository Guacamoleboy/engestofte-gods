package engestofte.domain.enquiry.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnquiryOwnerApprovalRequestDTO {

	// Expected JSON Input
	// ___________________
	//
	//		{
	//			"customer_note": "We look forward to helping with your wedding."
	//		}
	//
	// ____________________
	// Tested: NO
	// Last Tested: N/A

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@JsonProperty("customer_note")
	private String customerNote;
}
