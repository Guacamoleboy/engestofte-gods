package engestofte.domain.aiflow.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class AiFlowTurnDTO {

	// Expected JSON Input
	// ___________________
	//
	//		{
	//			"question": "What date are you considering?",
	//			"answer": "We are considering 12 June 2027."
	//		}
	//
	// ____________________
	// Tested: NO
	// Last Tested: N/A

	// _________________________________________________________________________________________________________________

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@JsonProperty("question")
	private String question;

	@JsonProperty("answer")
	private String answer;
}
