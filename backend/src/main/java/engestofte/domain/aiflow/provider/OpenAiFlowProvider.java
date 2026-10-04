package engestofte.domain.aiflow.provider;

import com.fasterxml.jackson.databind.JsonNode;
import engestofte.config.PoolConfig;
import engestofte.domain.aiflow.dto.request.AiFlowRequestDTO;
import engestofte.domain.aiflow.dto.request.AiFlowTurnDTO;
import engestofte.domain.aiflow.dto.response.AiEnquiryAssessmentDTO;
import engestofte.domain.aiflow.dto.response.AiFlowResponseDTO;
import engestofte.domain.aiflow.enums.AiFlowStatus;
import engestofte.exception.ApiException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class OpenAiFlowProvider implements AiFlowProvider, EnquiryAssessmentProvider {

	// Attributes
	private static final String API_URL = "https://api.openai.com/v1/responses";
	private static final String MODEL = "gpt-4.1-mini";
	private final String apiKey;

	// _________________________________________________________________________________________________________________

	public OpenAiFlowProvider(String apiKey) {
		this.apiKey = apiKey;
	}

	// _________________________________________________________________________________________________________________

	@Override
	public AiFlowResponseDTO respond(AiFlowRequestDTO request) {
		try {
			String input = PoolConfig.getMapper().writeValueAsString(Map.of(
					"language", request.getLanguage(),
					"current_step", request.getStep(),
					"current_question", request.getCurrentQuestion(),
					"customer_name", request.getCustomerName() == null ? "" : request.getCustomerName(),
					"conversation", request.getConversation() == null ? List.of() : request.getConversation(),
					"latest_answer", request.getAnswer()
			));
			String rubric = readResource("rubric/rubric.json");
			String userPrompt = readResource("prompts/user-prompt.txt")
					.replace("{{REQUEST}}", input)
					.replace("{{RUBRIC}}", rubric);
			String body = PoolConfig.getMapper().writeValueAsString(Map.of(
					"model", MODEL,
					"store", false,
					"instructions", readResource("prompts/system-prompt.md"),
					"input", userPrompt,
					"text", Map.of("format", Map.of(
							"type", "json_schema",
							"name", "wedding_enquiry_flow_state",
							"strict", true,
							"schema", Map.of(
											"type", "object",
											"properties", Map.of(
													"acknowledgement", Map.of("type", "string"),
													"next_question", Map.of("type", "string"),
													"customer_name", Map.of("type", "string"),
													"step", Map.of("type", "integer", "minimum", 1, "maximum", 5),
													"expected_guest_count", Map.of("type", "integer", "minimum", 0, "maximum", 150),
													"status", Map.of("type", "string", "enum", new String[]{"IN_PROGRESS", "STEP_COMPLETE", "OUT_OF_SCOPE", "DONE"})
											),
											"required", new String[]{"acknowledgement", "next_question", "customer_name", "step", "expected_guest_count", "status"},
									"additionalProperties", false
							)
					))
			));

			HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(API_URL))
					.timeout(Duration.ofSeconds(45))
					.header("Authorization", "Bearer " + apiKey)
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(body))
					.build();

			HttpResponse<String> response = PoolConfig.getClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() < 200 || response.statusCode() >= 300) {
				throw new ApiException(500, "AI service is unavailable");
			}

			JsonNode output = PoolConfig.getMapper().readTree(response.body()).path("output");
			for (JsonNode item : output) {
				for (JsonNode content : item.path("content")) {
					if ("output_text".equals(content.path("type").asText())) {
						JsonNode structured = PoolConfig.getMapper().readTree(content.path("text").asText());
						return mapResponse(structured, request);
					}
				}
			}
			throw new ApiException(500, "AI service is unavailable");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new ApiException(500, "AI service is unavailable");
		} catch (IOException e) {
			throw new ApiException(500, "AI service is unavailable");
		}
	}

	// _________________________________________________________________________________________________________________

	@Override
	public AiEnquiryAssessmentDTO assess(List<AiFlowTurnDTO> conversation, String language) {
		try {
			String serializedConversation = PoolConfig.getMapper().writeValueAsString(conversation);
			String prompt = readResource("prompts/enquiry-assessment-user-prompt.txt")
					.replace("{{LANGUAGE}}", language)
					.replace("{{RUBRIC}}", readResource("rubric/rubric.json"))
					.replace("{{CONVERSATION}}", serializedConversation);
			Map<String, Object> stringList = Map.of(
					"type", "array",
					"items", Map.of("type", "string", "maxLength", 700),
					"maxItems", 20);
			Map<String, Object> schema = Map.of(
				"type", "object",
				"properties", Map.of(
						"summary", Map.of("type", "string", "maxLength", 3000),
						"missing_information", stringList,
						"uncertainties", stringList,
						"conflicts", stringList,
						"upsell_suggestions", stringList),
				"required", new String[]{"summary", "missing_information", "uncertainties", "conflicts", "upsell_suggestions"},
				"additionalProperties", false);
			String body = PoolConfig.getMapper().writeValueAsString(Map.of(
					"model", MODEL,
					"store", false,
					"instructions", readResource("prompts/enquiry-assessment-system-prompt.md"),
					"input", prompt,
					"text", Map.of("format", Map.of(
							"type", "json_schema",
							"name", "wedding_enquiry_assessment",
							"strict", true,
							"schema", schema))));
			HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(API_URL))
					.timeout(Duration.ofSeconds(45))
					.header("Authorization", "Bearer " + apiKey)
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(body))
					.build();
			HttpResponse<String> response = PoolConfig.getClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() < 200 || response.statusCode() >= 300) {
				throw new ApiException(500, "AI enquiry assessment is unavailable");
			}
			JsonNode structured = readStructuredOutput(response.body());
			AiEnquiryAssessmentDTO assessment = PoolConfig.getMapper().treeToValue(structured, AiEnquiryAssessmentDTO.class);
			validateAssessment(assessment);
			return assessment;
		} catch (InterruptedException exception) {
			Thread.currentThread().interrupt();
			throw new ApiException(500, "AI enquiry assessment is unavailable");
		} catch (IOException exception) {
			throw new ApiException(500, "AI enquiry assessment is unavailable");
		}
	}

	// _________________________________________________________________________________________________________________

	private String readResource(String path) throws IOException {
		try (InputStream stream = getClass().getClassLoader().getResourceAsStream(path)) {
			if (stream == null) {
				throw new IOException("Missing AI flow resource: " + path);
			}
			return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
		}
	}

	// _________________________________________________________________________________________________________________

	private JsonNode readStructuredOutput(String responseBody) throws IOException {
		JsonNode output = PoolConfig.getMapper().readTree(responseBody).path("output");
		for (JsonNode item : output) {
			for (JsonNode content : item.path("content")) {
				if ("output_text".equals(content.path("type").asText())) {
					return PoolConfig.getMapper().readTree(content.path("text").asText());
				}
			}
		}
		throw new ApiException(500, "AI enquiry assessment is unavailable");
	}

	// _________________________________________________________________________________________________________________

	private static void validateAssessment(AiEnquiryAssessmentDTO assessment) {
		if (assessment.getSummary() == null || assessment.getSummary().isBlank()
				|| assessment.getSummary().length() > 3000
				|| !isValidItems(assessment.getMissingInformation())
				|| !isValidItems(assessment.getUncertainties())
				|| !isValidItems(assessment.getConflicts())
				|| !isValidItems(assessment.getUpsellSuggestions())) {
			throw new ApiException(500, "AI enquiry assessment is unavailable");
		}
	}

	private static boolean isValidItems(List<String> items) {
		return items != null && items.size() <= 20
				&& items.stream().allMatch(item -> item != null && !item.isBlank() && item.length() <= 700);
	}

	// _________________________________________________________________________________________________________________

	private AiFlowResponseDTO mapResponse(JsonNode structured, AiFlowRequestDTO request) {
		String acknowledgement = structured.path("acknowledgement").asText();
		String nextQuestion = structured.path("next_question").asText();
		String customerName = structured.path("customer_name").asText();
		if (customerName.isBlank() && request.getCustomerName() != null) {
			customerName = request.getCustomerName();
		}
		int step = structured.path("step").asInt(0);
		int expectedGuestCount = structured.path("expected_guest_count").asInt(0);
		AiFlowStatus status;
		try {
			status = AiFlowStatus.valueOf(structured.path("status").asText());
		} catch (IllegalArgumentException e) {
			throw new ApiException(500, "AI service is unavailable");
		}
		if (acknowledgement.isBlank() || customerName.length() > 160 || step != request.getStep()
				|| expectedGuestCount < 0 || expectedGuestCount > 150
				|| (status == AiFlowStatus.IN_PROGRESS && nextQuestion.isBlank())
				|| (status != AiFlowStatus.IN_PROGRESS && !nextQuestion.isBlank())
				|| (status == AiFlowStatus.DONE && request.getStep() != 5)
				|| (status == AiFlowStatus.STEP_COMPLETE && request.getStep() == 5)) {
			throw new ApiException(500, "AI service is unavailable");
		}

		AiFlowResponseDTO result = new AiFlowResponseDTO();
		result.setAcknowledgement(acknowledgement);
		result.setNextQuestion(nextQuestion);
		result.setCustomerName(customerName);
		result.setStep(step);
		result.setStatus(status);
		result.setExpectedGuestCount(expectedGuestCount == 0 ? null : expectedGuestCount);
		return result;
	}
}
