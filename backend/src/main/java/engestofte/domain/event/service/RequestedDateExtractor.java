package engestofte.domain.event.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RequestedDateExtractor {
	private static final String MONTH_NAME = "(?:januar|january|februar|february|marts|march|april|maj|may|juni|june|juli|july|august|september|oktober|october|november|december|dezember|m(?:\\x{00E4}|ae)rz)";
	private static final String DAY_NUMBER = "\\d{1,2}(?:st|nd|rd|th)?\\.?";
	private static final Pattern DATE_PATTERN = Pattern.compile(
			"(?iu)(?:\\b" + DAY_NUMBER + "\\s*(?:-|\\u2013|til|to|bis)\\s*" + DAY_NUMBER + "\\s+" + MONTH_NAME + "\\s+\\d{4}\\b"
					+ "|\\b" + MONTH_NAME + "\\s+" + DAY_NUMBER + "\\s*(?:-|\\u2013|to|bis)\\s*" + DAY_NUMBER + "[,]?\\s+\\d{4}\\b"
					+ "|\\b\\d{1,2}(?:st|nd|rd|th)?\\.?\\s+(?:of\\s+)?" + MONTH_NAME + "\\s+\\d{4}\\b"
					+ "|\\b" + MONTH_NAME + "\\s+\\d{1,2}(?:st|nd|rd|th)?[,]?\\s+\\d{4}\\b"
					+ "|\\b\\d{1,2}[./-]\\d{1,2}[./-]\\d{4}\\b"
					+ "|\\b\\d{4}-\\d{2}-\\d{2}\\b"
					+ "|\\b" + MONTH_NAME + "\\s+\\d{4}\\b)");

	private RequestedDateExtractor() { }

	// _________________________________________________________________________________________________________________

	public static String extract(String answer) {
		if (answer == null || answer.isBlank()) return null;
		Matcher matcher = DATE_PATTERN.matcher(answer);
		if (!matcher.find()) return null;
		StringBuilder requestedDate = new StringBuilder(matcher.group().trim());
		while (matcher.find()) requestedDate.append(" - ").append(matcher.group().trim());
		return requestedDate.toString();
	}
}
