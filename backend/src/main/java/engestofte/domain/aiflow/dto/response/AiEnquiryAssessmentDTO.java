package engestofte.domain.aiflow.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiEnquiryAssessmentDTO {

	// Expected JSON Output
	// ____________________
	//
	//		{
	//			"summary": "A wedding for approximately 60 guests in June.",
	//			"missing_information": [],
	//			"uncertainties": [],
	//			"conflicts": [],
	//			"upsell_suggestions": []
	//		}
	//
	// ____________________
	// Tested: NO
	// Last Tested: N/A

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@JsonProperty("summary")
	private String summary;

	@JsonProperty("missing_information")
	private List<String> missingInformation;

	@JsonProperty("uncertainties")
	private List<String> uncertainties;

	@JsonProperty("conflicts")
	private List<String> conflicts;

	@JsonProperty("upsell_suggestions")
	private List<String> upsellSuggestions;
}
