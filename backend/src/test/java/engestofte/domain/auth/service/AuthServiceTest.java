package engestofte.domain.auth.service;

import engestofte.ATest;
import engestofte.domain.auth.dto.request.LoginRequestDTO;
import engestofte.domain.auth.dto.request.RefreshTokenRequestDTO;
import engestofte.domain.auth.dto.request.RegisterRequestDTO;
import engestofte.domain.auth.dto.response.AuthResponseDTO;
import engestofte.exception.ApiException;
import engestofte.security.jwt.JwtService;
import engestofte.security.jwt.JwtUtil;
import engestofte.testdata.TestJsonData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest extends ATest {

    // Attributes
	private AuthService service;

	// _________________________________________________________________________________________________________________

	@BeforeEach
	void setupService() {
		service = new AuthService(emf);
		em.getTransaction().begin();
		em.createQuery("DELETE FROM UserAccount").executeUpdate();
		em.createQuery("DELETE FROM Role").executeUpdate();
		em.getTransaction().commit();
	}

	// _________________________________________________________________________________________________________________

	@Test
	void registerNormalizesEmailAndReturnsRedactedCustomerAccount() {
		RegisterRequestDTO request = registration();
		request.setEmail(" USER1@EXAMPLE.TEST ");

		AuthResponseDTO response = service.register(request);

		assertEquals("User One", response.getAccount().getFullName());
		assertEquals("use....@example.test", response.getAccount().getEmailRedacted());
		assertEquals("CUSTOMER", response.getAccount().getRole().name());
		assertTrue(JwtUtil.isAccessTokenValid(response.getAccessToken()));
		assertTrue(JwtUtil.isRefreshTokenValid(response.getRefreshToken()));
		assertEquals("User One", JwtService.getClaimFirstName(response.getAccessToken()));
		assertEquals("CUSTOMER", JwtService.getClaimRole(response.getAccessToken()));
		assertEquals(1, em.createQuery("SELECT COUNT(account) FROM UserAccount account", Long.class).getSingleResult());
	}

	// _________________________________________________________________________________________________________________

	@Test
	void registerRejectsAnExistingEmailWithoutCreatingAnotherAccount() {
		service.register(registration());
		RegisterRequestDTO duplicate = registration();
		duplicate.setEmail(" USER1@EXAMPLE.TEST ");

		ApiException exception = assertThrows(ApiException.class, () -> service.register(duplicate));

		assertEquals(409, exception.getCode());
		assertEquals(1, em.createQuery("SELECT COUNT(account) FROM UserAccount account", Long.class).getSingleResult());
	}

	// _________________________________________________________________________________________________________________

	@Test
	void loginAcceptsEmailCaseDifferencesAndRejectsInvalidCredentials() {
		service.register(registration());
		LoginRequestDTO request = new LoginRequestDTO();
		request.setEmail("USER1@EXAMPLE.TEST");
		request.setPassword(registration().getPassword());

		assertEquals("use....@example.test", service.login(request).getAccount().getEmailRedacted());
		request.setPassword("wrong-password");
		ApiException exception = assertThrows(ApiException.class, () -> service.login(request));
		assertEquals(401, exception.getCode());
	}

	// _________________________________________________________________________________________________________________

	@Test
	void refreshIssuesNewTokensOnlyForAValidRefreshToken() {
		AuthResponseDTO registered = service.register(registration());
		RefreshTokenRequestDTO request = new RefreshTokenRequestDTO();
		request.setRefreshToken(registered.getRefreshToken());

		AuthResponseDTO refreshed = service.refresh(request);

		assertTrue(JwtUtil.isAccessTokenValid(refreshed.getAccessToken()));
		assertTrue(JwtUtil.isRefreshTokenValid(refreshed.getRefreshToken()));
		assertEquals("use....@example.test", refreshed.getAccount().getEmailRedacted());
		request.setRefreshToken("not-a-token");
		assertEquals(401, assertThrows(ApiException.class, () -> service.refresh(request)).getCode());
	}

	// _________________________________________________________________________________________________________________

	private RegisterRequestDTO registration() {
		return TestJsonData.read("testdata/data/core.json", "user1", RegisterRequestDTO.class);
	}

}