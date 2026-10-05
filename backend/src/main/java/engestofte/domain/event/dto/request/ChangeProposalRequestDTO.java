package engestofte.domain.event.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeProposalRequestDTO {

	// Expected JSON Input
	// ___________________
	//
	//		{ "field_name": "customer_name", "new_value": "Camilla Jensen" }
	//
	// ____________________
	// Tested: NO
	// Last Tested: N/A

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@JsonProperty("field_name")
	private String fieldName;

	@JsonProperty("new_value")
	private String newValue;
}
