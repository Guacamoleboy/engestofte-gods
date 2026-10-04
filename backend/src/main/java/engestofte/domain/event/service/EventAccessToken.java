package engestofte.domain.event.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public final class EventAccessToken {

	// Attributes
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();

	// _________________________________________________________________________________________________________________

	private EventAccessToken() {
	}

	// _________________________________________________________________________________________________________________

	public static String createHash() {
		return hash(generateToken());
	}

	// _________________________________________________________________________________________________________________

	public static String generateToken() {
		byte[] token = new byte[32];
		SECURE_RANDOM.nextBytes(token);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
	}

	// _________________________________________________________________________________________________________________

	public static String hash(String token) {
		try {
			byte[] digest = MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8));
			return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
		} catch (NoSuchAlgorithmException exception) {
			throw new IllegalStateException("SHA-256 is not available", exception);
		}
	}
}
