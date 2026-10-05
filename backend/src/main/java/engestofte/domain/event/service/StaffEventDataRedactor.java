package engestofte.domain.event.service;

import com.fasterxml.jackson.databind.JsonNode;
import engestofte.domain.event.dto.response.EventOperationalDetailResponseDTO;
import engestofte.domain.event.entity.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class StaffEventDataRedactor {
	private static final Pattern EMAIL_PATTERN = Pattern.compile("(?i)[\\w.%+-]+@[\\w.-]+\\.[A-Z]{2,}");
	private static final Pattern PHONE_PATTERN = Pattern.compile("(?<!\\w)\\+?\\d[\\d .()/-]{7,}\\d(?!\\w)");

	private StaffEventDataRedactor() { }

	// _________________________________________________________________________________________________________________

	public static RedactedEventData redact(Event event, String primaryContactName) {
		String customerName = event.getEventData().path("customerName").asText();
		String eventName = redactText(event.getEventData().path("eventName").asText(), customerName, primaryContactName);
		JsonNode conversation = event.getEventData().path("conversation");
		List<EventOperationalDetailResponseDTO> details = new ArrayList<>();
		if (conversation.isArray()) {
			for (int index = 1; index < conversation.size(); index++) {
				JsonNode turn = conversation.get(index);
				String question = redactText(turn.path("question").asText(), customerName, primaryContactName);
				String answer = redactText(turn.path("answer").asText(), customerName, primaryContactName);
				if (question.isBlank() || answer.isBlank()) continue;
				EventOperationalDetailResponseDTO detail = new EventOperationalDetailResponseDTO();
				detail.setQuestion(question);
				detail.setAnswer(answer);
				details.add(detail);
			}
		}
		return new RedactedEventData(eventName.isBlank() ? null : eventName, List.copyOf(details));
	}

	public static String redactRequestedDate(Event event, String primaryContactName) {
		String customerName = event.getEventData().path("customerName").asText();
		JsonNode conversation = event.getEventData().path("conversation");
		if (!conversation.isArray() || conversation.size() <= 1) return null;
		String requestedDate = redactText(conversation.get(1).path("answer").asText(), customerName, primaryContactName);
		return requestedDate.isBlank() ? null : requestedDate;
	}

	// _________________________________________________________________________________________________________________

	private static String redactText(String value, String... names) {
		if (value == null) return "";
		String redacted = value;
		for (String name : names) {
			if (name != null && !name.isBlank()) redacted = redacted.replaceAll("(?i)" + Pattern.quote(name), "[redacted]");
		}
		redacted = EMAIL_PATTERN.matcher(redacted).replaceAll("[redacted]");
		return PHONE_PATTERN.matcher(redacted).replaceAll("[redacted]");
	}

	public record RedactedEventData(String eventName, List<EventOperationalDetailResponseDTO> details) { }
}
