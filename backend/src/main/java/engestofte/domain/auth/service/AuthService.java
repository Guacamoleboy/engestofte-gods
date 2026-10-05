package engestofte.domain.auth.service;

import engestofte.domain.auth.dto.request.LoginRequestDTO;
import engestofte.domain.auth.dto.request.RefreshTokenRequestDTO;
import engestofte.domain.auth.dto.request.RegisterRequestDTO;
import engestofte.domain.auth.dto.response.AccountResponseDTO;
import engestofte.domain.auth.dto.response.AuthResponseDTO;
import engestofte.domain.role.dao.RoleDAO;
import engestofte.domain.role.entity.Role;
import engestofte.domain.role.enums.RoleName;
import engestofte.domain.useraccount.dao.UserAccountDAO;
import engestofte.domain.useraccount.entity.UserAccount;
import engestofte.exception.ApiException;
import engestofte.security.jwt.JwtService;
import engestofte.security.jwt.JwtUtil;
import engestofte.util.BCryptHash;
import engestofte.util.EmailRedactor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class AuthService {

	// Attributes
	private final EntityManagerFactory entityManagerFactory;

	// _________________________________________________________________________________________________________________

	public AuthService(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	// _________________________________________________________________________________________________________________

	public AuthResponseDTO register(RegisterRequestDTO request) {
		String fullName = requiredText(request.getFullName(), "Name", 160);
		String email = normalizeEmail(request.getEmail());
		String password = validatePassword(request.getPassword());

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		try {
			entityManager.getTransaction().begin();
			UserAccountDAO userAccountDAO = new UserAccountDAO(entityManager);
			if (userAccountDAO.findByEmail(email) != null) {
				throw new ApiException(409, "An account with this email already exists");
			}

			RoleDAO roleDAO = new RoleDAO(entityManager);
			Role customerRole = roleDAO.getByName(RoleName.CUSTOMER);
			if (customerRole == null) {
				customerRole = new Role();
				customerRole.setName(RoleName.CUSTOMER);
				entityManager.persist(customerRole);
			}

			UserAccount account = new UserAccount();
			account.setFullName(fullName);
			account.setEmail(email);
			account.setPasswordHash(BCryptHash.hash(password));
			account.setRole(customerRole);
			entityManager.persist(account);
			entityManager.getTransaction().commit();
			return createAuthResponse(account);
		} catch (ApiException exception) {
			rollback(entityManager);
			throw exception;
		} catch (RuntimeException exception) {
			rollback(entityManager);
			throw new ApiException(500, "Account registration failed", exception);
		} finally {
			entityManager.close();
		}
	}

	// _________________________________________________________________________________________________________________

	public AuthResponseDTO login(LoginRequestDTO request) {
		String email = normalizeEmail(request.getEmail());
		String password = validatePassword(request.getPassword());
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		try {
			UserAccount account = new UserAccountDAO(entityManager).findByEmail(email);
			if (account == null || !BCryptHash.check(password, account.getPasswordHash())) {
				throw new ApiException(401, "Invalid email or password");
			}
			return createAuthResponse(account);
		} finally {
			entityManager.close();
		}
	}

	// _________________________________________________________________________________________________________________

	public AuthResponseDTO refresh(RefreshTokenRequestDTO request) {
		String refreshToken = request.getRefreshToken();
		if (!JwtUtil.isRefreshTokenValid(refreshToken)) {
			throw new ApiException(401, "Invalid refresh token");
		}

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		try {
			Integer accountId = JwtService.getClaimAccountId(refreshToken);
			UserAccount account = new UserAccountDAO(entityManager).getById(accountId);
			if (account == null) throw new ApiException(401, "Account not found");
			return createAuthResponse(account);
		} finally {
			entityManager.close();
		}
	}

	// _________________________________________________________________________________________________________________

	private AuthResponseDTO createAuthResponse(UserAccount account) {
		AccountResponseDTO accountResponse = new AccountResponseDTO();
		accountResponse.setFullName(account.getFullName());
		accountResponse.setEmailRedacted(EmailRedactor.redact(account.getEmail()));
		accountResponse.setRole(account.getRole().getName());

		AuthResponseDTO response = new AuthResponseDTO();
		response.setAccessToken(JwtService.generateAccessToken(account.getId(), account.getFullName(), account.getRole().getName().name()));
		response.setRefreshToken(JwtService.generateRefreshToken(account.getId()));
		response.setAccount(accountResponse);
		return response;
	}

	// _________________________________________________________________________________________________________________

	private static String normalizeEmail(String value) {
		String email = requiredText(value, "Email", 254).toLowerCase(Locale.ROOT);
		if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) throw new ApiException(400, "Enter a valid email address");
		return email;
	}

	// _________________________________________________________________________________________________________________

	private static String validatePassword(String password) {
		if (password == null || password.isBlank()) throw new ApiException(400, "Password is required");
		if (password.length() > 72) throw new ApiException(400, "Password is too long");
		String value = password;
		if (value.getBytes(StandardCharsets.UTF_8).length > 72) throw new ApiException(400, "Password is too long");
		return value;
	}

	// _________________________________________________________________________________________________________________

	private static String requiredText(String value, String fieldName, int maxLength) {
		if (value == null || value.isBlank()) throw new ApiException(400, fieldName + " is required");
		String trimmed = value.trim();
		if (trimmed.length() > maxLength) throw new ApiException(400, fieldName + " is too long");
		return trimmed;
	}

	// _________________________________________________________________________________________________________________

	private static void rollback(EntityManager entityManager) {
		if (entityManager.getTransaction().isActive()) entityManager.getTransaction().rollback();
	}
}
