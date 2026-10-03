package engestofte.domain.aiflow.provider;

import com.fasterxml.jackson.databind.JsonNode;
import engestofte.config.PoolConfig;
import engestofte.domain.aiflow.dto.request.AiFlowRequestDTO;
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

public class OpenAiFlowProvider implements AiFlowProvider {

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
											"status", Map.of("type", "string", "enum", new String[]{"IN_PROGRESS", "STEP_COMPLETE", "OUT_OF_SCOPE", "DONE"})
									),
									"required", new String[]{"acknowledgement", "next_question", "customer_name", "step", "status"},
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

	private String readResource(String path) throws IOException {
		try (InputStream stream = getClass().getClassLoader().getResourceAsStream(path)) {
			if (stream == null) {
				throw new IOException("Missing AI flow resource: " + path);
			}
			return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
		}
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
		AiFlowStatus status;
		try {
			status = AiFlowStatus.valueOf(structured.path("status").asText());
		} catch (IllegalArgumentException e) {
			throw new ApiException(500, "AI service is unavailable");
		}
		if (acknowledgement.isBlank() || customerName.length() > 160 || step != request.getStep()
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
		return result;
	}
}
