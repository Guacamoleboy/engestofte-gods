package engestofte.domain.event.service;

import com.fasterxml.jackson.databind.JsonNode;
import engestofte.domain.event.entity.Event;

import java.util.regex.Pattern;

public final class StaffEventDataRedactor {
	private static final Pattern EMAIL_PATTERN = Pattern.compile("(?i)[\\w.%+-]+@[\\w.-]+\\.[A-Z]{2,}");
	private static final Pattern PHONE_PATTERN = Pattern.compile("(?<!\\w)\\+?\\d[\\d .()/-]{7,}\\d(?!\\w)");

	private StaffEventDataRedactor() { }

	// _________________________________________________________________________________________________________________

	public static RedactedEventData redact(Event event, String primaryContactName) {
		String customerName = redactText(event.getEventData().path("customerName").asText());
		String eventName = redactText(event.getEventData().path("eventName").asText());
		return new RedactedEventData(customerName.isBlank() ? redactText(primaryContactName) : customerName, eventName.isBlank() ? null : eventName);
	}

	public static String redactRequestedDate(Event event) {
		JsonNode conversation = event.getEventData().path("conversation");
		if (!conversation.isArray() || conversation.size() <= 1) return null;
		return RequestedDateExtractor.extract(conversation.get(1).path("answer").asText());
	}

	// _________________________________________________________________________________________________________________

	private static String redactText(String value) {
		if (value == null) return "";
		String redacted = EMAIL_PATTERN.matcher(value).replaceAll("[redacted]");
		return PHONE_PATTERN.matcher(redacted).replaceAll("[redacted]");
	}

	public static String redactContactDetails(String value) {
		return redactText(value);
	}

	public record RedactedEventData(String customerName, String eventName) { }
}
