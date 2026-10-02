package engestofte.domain.aiflow.provider;

import com.fasterxml.jackson.databind.JsonNode;
import engestofte.config.PoolConfig;
import engestofte.domain.aiflow.dto.request.AiFlowRequestDTO;
import engestofte.domain.aiflow.dto.response.AiFlowResponseDTO;
import engestofte.domain.aiflow.enums.AiFlowStatus;
import engestofte.exception.ApiException;
import java.io.IOException;
import java.net.URI;
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
			String instructions = """
					You are the intake assistant for Engestofte Gods. This flow accepts wedding enquiries only.
					The application owns the five steps and their fixed introductory questions. Evaluate the customer's answer in the context of the supplied conversation and current step. Never advance to another step yourself. The tone is warm, professional and personal; use the customer's name naturally after learning it.
					Extract the customer's name from the conversation only when it is explicitly stated. Otherwise preserve the supplied customer name or return an empty string.

					If the user clearly wants an event other than a wedding, return OUT_OF_SCOPE. Do not continue collecting details; politely say this assistant currently handles weddings and direct them to contact Engestofte Gods.
					If the current step has unresolved required or conditional information, return IN_PROGRESS, briefly acknowledge the answer and ask one focused follow-up question about the most important missing or unclear item. Keep the same step. Treat an explicit "not decided yet" as valid where the rubric allows it. Do not repeat questions already answered in the conversation, and do not insist on optional information after the customer declines or says they do not know.
					Step 1 collects the contact person's name. Ask again if it is still missing.
					Step 2 collects a desired wedding date or date range. Acknowledge the date warmly, but never imply it is available; Engestofte must check availability separately.
					Step 3 collects expected guest count and whether accommodation is wanted. If accommodation is wanted, collect how many people need it and the relevant nights/date range. If the guest count is 60 or lower and the Intimpakke offer has not already been presented in the conversation, ask once whether they would like Engestofte to prepare a quote comparing the Intimpakke and standard wedding package, so they can choose what suits them. This is optional; accept yes or no and never state a price. If they do not want accommodation, offer to investigate a bus between a nearby hotel and Engestofte. Do not promise bus availability or booking. Offer the package comparison and bus one at a time, and do not repeat an offer already made.
					Step 4 collects the wedding format: ceremony preference and form/location, reception, dinner and party. "Not decided" is a valid answer for ceremony form/location. Ask one follow-up at a time for missing critical details.
					Step 5 asks about food and drink, allergies or dietary needs when relevant, and optional extras, practical wishes and budget. Optional items never block completion; accept a decline or "not decided".
					Return STEP_COMPLETE when the current step's required and conditional information is sufficiently clear. Optional information may be omitted. Return DONE only for step 5. For STEP_COMPLETE, DONE and OUT_OF_SCOPE, next_question must be an empty string. The application will show the fixed introduction for the next step.
					Never invent customer facts, availability, prices, capacity, package details or policies. The named Intimpakke/standard package comparison is the only approved package suggestion in this flow. Do not promise bookings. Keep responses concise, friendly and in the requested language.

					Field classifications come from docs/grilling/flow-definition.md. If the customer gave relevant information early, use it to avoid asking them to repeat it. Do not treat an optional answer as required just because its former rubric section appears later.
					""";

			String input = PoolConfig.getMapper().writeValueAsString(Map.of(
					"language", request.getLanguage(),
					"current_step", request.getStep(),
					"current_question", request.getCurrentQuestion(),
					"customer_name", request.getCustomerName() == null ? "" : request.getCustomerName(),
					"conversation", request.getConversation() == null ? List.of() : request.getConversation(),
					"latest_answer", request.getAnswer()
			));
			String body = PoolConfig.getMapper().writeValueAsString(Map.of(
					"model", MODEL,
					"store", false,
					"instructions", instructions,
					"input", input,
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
				throw new ApiException(502, "AI provider could not complete the request");
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
			throw new ApiException(502, "AI provider returned an incomplete response");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new ApiException(502, "AI provider request was interrupted");
		} catch (IOException e) {
			throw new ApiException(502, "AI provider request failed");
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
			throw new ApiException(502, "AI provider returned an invalid flow status");
		}
		if (acknowledgement.isBlank() || customerName.length() > 160 || step != request.getStep()
				|| (status == AiFlowStatus.IN_PROGRESS && nextQuestion.isBlank())
				|| (status != AiFlowStatus.IN_PROGRESS && !nextQuestion.isBlank())
				|| (status == AiFlowStatus.DONE && request.getStep() != 5)
				|| (status == AiFlowStatus.STEP_COMPLETE && request.getStep() == 5)) {
			throw new ApiException(502, "AI provider returned an invalid flow state");
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
