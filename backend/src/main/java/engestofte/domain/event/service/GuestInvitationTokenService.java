package engestofte.domain.event.service;

import engestofte.config.DotEnv;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HexFormat;

public final class GuestInvitationTokenService {
	private static final String TOKEN_PURPOSE = "engestofte:guest-event-invitation:v1:";

	private GuestInvitationTokenService() { }

	// _________________________________________________________________________________________________________________

	public static String createAccessToken(Integer eventId, long approvedAtEpochMilli) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			byte[] secret = DotEnv.get("JWT_SECRET").getBytes(StandardCharsets.UTF_8);
			mac.init(new SecretKeySpec(secret, "HmacSHA256"));
			byte[] token = mac.doFinal((TOKEN_PURPOSE + eventId + ":" + approvedAtEpochMilli).getBytes(StandardCharsets.UTF_8));
			return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
		} catch (java.security.GeneralSecurityException exception) {
			throw new IllegalStateException("Guest invitation access could not be created", exception);
		}
	}

	// _________________________________________________________________________________________________________________

	public static boolean isInvitationCreated(Integer eventId, long approvedAtEpochMilli, String accessHash) {
		return accessHash != null && matchesHash(createAccessToken(eventId, approvedAtEpochMilli), accessHash);
	}

	// _________________________________________________________________________________________________________________

	public static String hashAccessToken(String accessToken) {
		try {
			byte[] hash = MessageDigest.getInstance("SHA-256").digest(accessToken.getBytes(StandardCharsets.UTF_8));
			return HexFormat.of().formatHex(hash);
		} catch (NoSuchAlgorithmException exception) {
			throw new IllegalStateException("Guest invitation access could not be verified", exception);
		}
	}

	// _________________________________________________________________________________________________________________

	public static boolean matchesHash(String accessToken, String expectedHash) {
		if (accessToken == null || expectedHash == null) return false;
		return MessageDigest.isEqual(
				hashAccessToken(accessToken).getBytes(StandardCharsets.US_ASCII),
				expectedHash.getBytes(StandardCharsets.US_ASCII));
	}
}
