package engestofte.domain.aiflow.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties
public class AiFlowRequestDTO {

	// _________________________________________________________________________________________________________________

	// Expected JSON Input
	// ___________________
	//
	//		{
	//			"answer": "Alex Morgan",
	//			"current_question": "What name should I create the enquiry under?",
	//			"customer_name": "",
	//			"step": 1,
	//			"language": "en",
	//			"conversation": [
	//				{
	//					"question": "What name should I create the enquiry under?",
	//					"answer": "Alex Morgan"
	//				}
	//			]
	//		}
	//
	// ____________________
	// Tested: NO
	// Last Tested: N/A

	// _________________________________________________________________________________________________________________

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@JsonProperty("answer")
	private String answer;

	@JsonProperty("current_question")
	private String currentQuestion;

	@JsonProperty("customer_name")
	private String customerName;

	@JsonProperty("step")
	private Integer step;

	@JsonProperty("language")
	private String language;

	@JsonProperty("conversation")
	private List<AiFlowTurnDTO> conversation;

}
