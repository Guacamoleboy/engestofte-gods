package engestofte.domain.aiflow.dto.response;

import engestofte.domain.aiflow.enums.AiFlowStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class AiFlowResponseDTO {

	// _________________________________________________________________________________________________________________

	// Expected JSON Output
	// ____________________
	//
	//		{
	//			"id": 1,
	//			"acknowledgement": "Thank you for your answer. I have noted it.",
	//			"next_question": "What date or date range and time are you considering?",
	//			"customer_name": "Alex and Sam",
	//			"step": 1,
	//			"status": "IN_PROGRESS"
	//		}
	//
	// ____________________
	// Tested: NO
	// Last Tested: N/A

	// _________________________________________________________________________________________________________________

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("acknowledgement")
	private String acknowledgement;

	@JsonProperty("next_question")
	private String nextQuestion;

	@JsonProperty("customer_name")
	private String customerName;

	@JsonProperty("step")
	private Integer step;

	@JsonProperty("status")
	private AiFlowStatus status;
	
}
