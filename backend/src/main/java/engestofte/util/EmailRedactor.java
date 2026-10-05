package engestofte.util;

public final class EmailRedactor {
	private static final String REDACTION = "....";

	private EmailRedactor() { }

	public static String redact(String email) {
		if (email == null || email.isBlank()) return null;
		int separator = email.lastIndexOf('@');
		if (separator <= 0 || separator == email.length() - 1) return REDACTION;
		String localPart = email.substring(0, separator);
		return localPart.substring(0, Math.min(3, localPart.length())) + REDACTION + email.substring(separator);
	}
}
