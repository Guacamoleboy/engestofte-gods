package engestofte.domain.aiflow.provider;

import engestofte.domain.aiflow.dto.request.AiFlowRequestDTO;
import engestofte.domain.aiflow.dto.response.AiFlowResponseDTO;
import engestofte.domain.aiflow.enums.AiFlowStatus;

public class LocalAiFlowProvider implements AiFlowProvider {

	// Attributes

	// _________________________________________________________________________________________________________________

	@Override
	public AiFlowResponseDTO respond(AiFlowRequestDTO request) {
		String acknowledgement = switch (request.getLanguage()) {
			case "en" -> "Thank you for your answer. I have noted it.";
			case "de" -> "Vielen Dank für Ihre Antwort. Ich habe sie notiert.";
			default -> "Tak for dit svar. Jeg har noteret det.";
		};
		String nextQuestion = switch (request.getLanguage()) {
			case "en" -> "How many guests do you expect to invite?";
			case "de" -> "Wie viele Gäste möchten Sie voraussichtlich einladen?";
			default -> "Hvor mange gæster forventer I at invitere?";
		};
		boolean askingForName = request.getCustomerName() == null || request.getCustomerName().isBlank();
		if (askingForName) {
			nextQuestion = switch (request.getLanguage()) {
				case "en" -> "What date or date range and time are you considering for your event?";
				case "de" -> "Welches Datum oder welchen Zeitraum und welche Uhrzeit w\u00fcnschen Sie f\u00fcr Ihre Veranstaltung?";
				default -> "Hvilken dato eller periode og hvilket tidspunkt \u00f8nsker I til jeres event?";
			};
		}
		AiFlowResponseDTO response = new AiFlowResponseDTO();
		response.setAcknowledgement(acknowledgement);
		response.setNextQuestion(nextQuestion);
		response.setCustomerName(askingForName ? request.getAnswer().trim() : request.getCustomerName());
		response.setStep(request.getStep() == null ? 1 : request.getStep());
		response.setStatus(AiFlowStatus.IN_PROGRESS);
		return response;
	}
	
}
