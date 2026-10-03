package engestofte.domain.enquiry.service;

import com.fasterxml.jackson.databind.JsonNode;
import engestofte.domain.enquiry.dto.request.EnquirySubmissionRequestDTO;
import engestofte.domain.enquiry.dto.response.EnquirySubmissionResponseDTO;
import engestofte.domain.enquiry.entity.EnquiryContact;
import engestofte.domain.enquiry.entity.WeddingEnquiry;
import engestofte.domain.enquiry.enums.EnquiryStatus;
import engestofte.domain.user.entity.UserAccount;
import engestofte.exception.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.exception.ConstraintViolationException;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

public class EnquiryService {

	// Attributes
	private final EntityManagerFactory entityManagerFactory;

	// _________________________________________________________________________________________________________________

	public EnquiryService(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	// _________________________________________________________________________________________________________________

	public EnquirySubmissionResponseDTO submit(Integer accountId, EnquirySubmissionRequestDTO request) {
		String submissionId = normalizeSubmissionId(request.getSubmissionId());
		String language = normalizeLanguage(request.getLanguage());
		JsonNode draft = validateDraft(request.getDraft(), language);

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		try {
			entityManager.getTransaction().begin();
			WeddingEnquiry existing = findBySubmissionId(entityManager, submissionId);
			if (existing != null) {
				ensureSamePrimaryContact(entityManager, existing, accountId);
				entityManager.getTransaction().commit();
				return toResponse(existing);
			}

			UserAccount primaryContact = entityManager.find(UserAccount.class, accountId);
			if (primaryContact == null) throw new ApiException(401, "Authenticated account not found");

			WeddingEnquiry enquiry = new WeddingEnquiry();
			enquiry.setSubmissionId(submissionId);
			enquiry.setLanguage(language);
			enquiry.setRawDraft(draft.deepCopy());
			enquiry.setStatus(EnquiryStatus.SUBMITTED);
			enquiry.setCreatedAt(Instant.now());
			entityManager.persist(enquiry);

			EnquiryContact contact = new EnquiryContact();
			contact.setEnquiry(enquiry);
			contact.setUserAccount(primaryContact);
			contact.setPrimary(true);
			entityManager.persist(contact);

			entityManager.getTransaction().commit();
			return toResponse(enquiry);
		} catch (ApiException exception) {
			rollback(entityManager);
			throw exception;
		} catch (RuntimeException exception) {
			rollback(entityManager);
			if (isConstraintViolation(exception)) {
				EnquirySubmissionResponseDTO duplicate = findExistingForAccount(submissionId, accountId);
				if (duplicate != null) return duplicate;
				throw new ApiException(409, "This submission ID is already in use");
			}
			throw exception;
		} finally {
			entityManager.close();
		}
	}

	// _________________________________________________________________________________________________________________

	private EnquirySubmissionResponseDTO findExistingForAccount(String submissionId, Integer accountId) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		try {
			WeddingEnquiry enquiry = findBySubmissionId(entityManager, submissionId);
			if (enquiry == null) return null;
			try {
				ensureSamePrimaryContact(entityManager, enquiry, accountId);
				return toResponse(enquiry);
			} catch (ApiException exception) {
				return null;
			}
		} finally {
			entityManager.close();
		}
	}

	// _________________________________________________________________________________________________________________

	private static WeddingEnquiry findBySubmissionId(EntityManager entityManager, String submissionId) {
		return entityManager.createQuery(
				"SELECT enquiry FROM WeddingEnquiry enquiry WHERE enquiry.submissionId = :submissionId",
				WeddingEnquiry.class)
			.setParameter("submissionId", submissionId)
			.getResultStream()
			.findFirst()
			.orElse(null);
	}

	// _________________________________________________________________________________________________________________

	private static void ensureSamePrimaryContact(EntityManager entityManager, WeddingEnquiry enquiry, Integer accountId) {
		Long matchingContacts = entityManager.createQuery(
				"SELECT COUNT(contact) FROM EnquiryContact contact WHERE contact.enquiry.id = :enquiryId AND contact.userAccount.id = :accountId AND contact.primary = true",
				Long.class)
			.setParameter("enquiryId", enquiry.getId())
			.setParameter("accountId", accountId)
			.getSingleResult();
		if (matchingContacts == 0) throw new ApiException(409, "This submission ID belongs to another account");
	}

	// _________________________________________________________________________________________________________________

	private static EnquirySubmissionResponseDTO toResponse(WeddingEnquiry enquiry) {
		EnquirySubmissionResponseDTO response = new EnquirySubmissionResponseDTO();
		response.setSubmissionId(enquiry.getSubmissionId());
		response.setLanguage(enquiry.getLanguage());
		response.setStatus(enquiry.getStatus());
		return response;
	}

	// _________________________________________________________________________________________________________________

	private static String normalizeSubmissionId(String value) {
		try {
			return UUID.fromString(value).toString();
		} catch (IllegalArgumentException | NullPointerException exception) {
			throw new ApiException(400, "A valid submission_id is required");
		}
	}

	// _________________________________________________________________________________________________________________

	private static String normalizeLanguage(String value) {
		if (value == null) throw new ApiException(400, "A supported language is required");
		String language = value.toLowerCase(Locale.ROOT);
		if (!language.equals("da") && !language.equals("en") && !language.equals("de")) {
			throw new ApiException(400, "Language must be da, en or de");
		}
		return language;
	}

	// _________________________________________________________________________________________________________________

	private static JsonNode validateDraft(JsonNode draft, String language) {
		if (draft == null || !draft.isObject() || !draft.path("isComplete").asBoolean(false)) {
			throw new ApiException(400, "Complete the AI flow before submitting the enquiry");
		}
		if (!language.equals(draft.path("language").asText())) {
			throw new ApiException(400, "Submission language must match the saved draft");
		}
		return draft;
	}

	// _________________________________________________________________________________________________________________

	private static boolean isConstraintViolation(Throwable exception) {
		for (Throwable current = exception; current != null; current = current.getCause()) {
			if (current instanceof ConstraintViolationException) return true;
		}
		return false;
	}

	// _________________________________________________________________________________________________________________

	private static void rollback(EntityManager entityManager) {
		if (entityManager.getTransaction().isActive()) entityManager.getTransaction().rollback();
	}
}
