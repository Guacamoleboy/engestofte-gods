package engestofte.domain.aiflow.provider;

import engestofte.config.PoolConfig;
import engestofte.domain.aiflow.dto.request.AiFlowRequestDTO;
import engestofte.domain.aiflow.dto.response.AiFlowResponseDTO;
import engestofte.domain.aiflow.enums.AiFlowStatus;
import engestofte.exception.ApiException;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
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

			String body = PoolConfig.getMapper().writeValueAsString(Map.of(
					"model", MODEL,
					"store", false,
					"instructions", "You are an AI assistant helping with an event enquiry to Engestofte Gods. The active enquiry follows the wedding flow. Extract the customer's name from their answer wherever it appears, but only when explicitly stated; otherwise preserve the supplied customer name or return an empty string. For the initial name question, acknowledge the name and ask for the desired date or date range and time. Follow the five steps in this order: (1) name, date/time and guest count; (2) ceremony, reception, dinner and party format; (3) food, allergies and optional extras; (4) accommodation and transport; (5) budget and other notes. Keep asking for missing information in the current step; advance the step only when its information is sufficiently covered. Return step 6 and status DONE only after step 5 is complete. Never invent answers. Acknowledge the answer briefly, ask one clear next question, and return all text in " + request.getLanguage() + ". Return JSON matching the requested schema.",
					"input", "Current step: " + request.getStep() + "\nCurrent question: " + request.getCurrentQuestion() + "\nKnown customer name: " + (request.getCustomerName() == null ? "" : request.getCustomerName()) + "\nCustomer answer: " + request.getAnswer(),
					"text", Map.of("format", Map.of(
							"type", "json_schema",
							"name", "event_enquiry_step",
							"strict", true,
							"schema", Map.of(
									"type", "object",
									"properties", Map.of(
										"acknowledgement", Map.of("type", "string"),
										"next_question", Map.of("type", "string"),
										"customer_name", Map.of("type", "string"),
										"step", Map.of("type", "integer", "minimum", 1, "maximum", 6),
										"status", Map.of("type", "string", "enum", new String[]{"IN_PROGRESS", "DONE"})
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
				throw new ApiException(502, "AI provider could not complete the request");
			}

			JsonNode output = PoolConfig.getMapper().readTree(response.body()).path("output");
			for (JsonNode item : output) {
				for (JsonNode content : item.path("content")) {
					if ("output_text".equals(content.path("type").asText())) {
						JsonNode structured = PoolConfig.getMapper().readTree(content.path("text").asText());
						String acknowledgement = structured.path("acknowledgement").asText();
						String nextQuestion = structured.path("next_question").asText();
						String customerName = structured.path("customer_name").asText();
						if (customerName.isBlank() && request.getCustomerName() != null) {
							customerName = request.getCustomerName();
						}
						int step = structured.path("step").asInt(0);
						String status = structured.path("status").asText();
						if (acknowledgement.isBlank() || nextQuestion.isBlank() || customerName.length() > 160
								|| step < 1 || step > 6
								|| (!"IN_PROGRESS".equals(status) && !"DONE".equals(status))
								|| ("DONE".equals(status) != (step == 6))) {
							throw new ApiException(502, "AI provider returned an incomplete response");
						}
						AiFlowResponseDTO aiFlowResponse = new AiFlowResponseDTO();
						aiFlowResponse.setAcknowledgement(acknowledgement);
						aiFlowResponse.setNextQuestion(nextQuestion);
						aiFlowResponse.setCustomerName(customerName);
						aiFlowResponse.setStep(step);
						aiFlowResponse.setStatus(AiFlowStatus.valueOf(status));
						return aiFlowResponse;
					}
				}
			}
			throw new ApiException(502, "AI provider returned an incomplete response");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new ApiException(502, "AI provider request was interrupted");
		} catch (IOException e) {
			throw new ApiException(502, "AI provider request failed");
		}
	}

}
