package engestofte.domain.event.service;

import engestofte.ATest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GuestInvitationTokenServiceTest extends ATest {

    // Attributes

    // _________________________________________________________________________________________________________________

	@Test
	void invitationAccessTokenIsBoundToEventAndApprovalTime() {
		String token = GuestInvitationTokenService.createAccessToken(42, 1_900_000_000_000L);
		String tokenHash = GuestInvitationTokenService.hashAccessToken(token);

		assertTrue(GuestInvitationTokenService.matchesHash(token, tokenHash));
		assertTrue(GuestInvitationTokenService.isInvitationCreated(42, 1_900_000_000_000L, tokenHash));
		assertFalse(GuestInvitationTokenService.isInvitationCreated(43, 1_900_000_000_000L, tokenHash));
		assertFalse(GuestInvitationTokenService.isInvitationCreated(42, 1_900_000_000_001L, tokenHash));
		assertFalse(GuestInvitationTokenService.matchesHash(null, tokenHash));
		assertFalse(GuestInvitationTokenService.matchesHash(token, null));
	}

    // _________________________________________________________________________________________________________________

	@Test
	void eventAccessTokensAreRandomAndTheirHashesCanBeStored() {
		String first = EventAccessToken.generateToken();
		String second = EventAccessToken.generateToken();

		assertNotEquals(first, second);
		assertNotEquals(first, EventAccessToken.hash(first));
		assertEquals(43, EventAccessToken.createHash().length());
	}

}