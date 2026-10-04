package engestofte.domain.enquiry.service;

import com.fasterxml.jackson.databind.JsonNode;
import engestofte.config.PoolConfig;
import engestofte.domain.aiflow.dto.request.AiFlowTurnDTO;
import engestofte.domain.aiflow.dto.response.AiEnquiryAssessmentDTO;
import engestofte.domain.aiflow.provider.EnquiryAssessmentProvider;
import engestofte.domain.enquiry.dao.WeddingEnquiryDAO;
import engestofte.domain.enquiry.dto.request.EnquiryOwnerReviewRequestDTO;
import engestofte.domain.enquiry.dto.request.EnquirySubmissionRequestDTO;
import engestofte.domain.enquiry.dto.response.EnquiryOwnerReviewResponseDTO;
import engestofte.domain.enquiry.dto.response.EnquiryOwnerReviewSummaryResponseDTO;
import engestofte.domain.enquiry.dto.response.EnquirySubmissionResponseDTO;
import engestofte.domain.enquiry.dto.response.EnquirySummaryResponseDTO;
import engestofte.domain.enquiry.entity.EnquiryContact;
import engestofte.domain.enquiry.entity.WeddingEnquiry;
import engestofte.domain.enquiry.enums.EnquiryStatus;
import engestofte.domain.event.dao.EventDAO;
import engestofte.domain.event.dao.EventMessageDAO;
import engestofte.domain.event.dto.response.EventApprovalResponseDTO;
import engestofte.domain.event.dto.request.EventMessageRequestDTO;
import engestofte.domain.event.dto.response.EventMessageResponseDTO;
import engestofte.domain.event.entity.Event;
import engestofte.domain.event.entity.EventMessage;
import engestofte.domain.event.enums.EventCategory;
import engestofte.domain.event.enums.EventMessageSender;
import engestofte.domain.event.enums.EventStatus;
import engestofte.domain.event.mapper.response.EventResponseMapper;
import engestofte.domain.event.service.EventAccessToken;
import engestofte.domain.event.mapper.response.EventMessageResponseMapper;
import engestofte.domain.enquiry.mapper.response.EnquirySummaryResponseMapper;
import engestofte.domain.enquiry.mapper.response.EnquiryOwnerReviewResponseMapper;
import engestofte.domain.useraccount.dao.UserAccountDAO;
import engestofte.domain.useraccount.entity.UserAccount;
import engestofte.exception.ApiException;
import engestofte.service.EntityManagerService;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class EnquiryService extends EntityManagerService<WeddingEnquiry> {

	// Attributes
	private final WeddingEnquiryDAO weddingEnquiryDAO;
	private final UserAccountDAO userAccountDAO;
	private final EventDAO eventDAO;
	private final EventMessageDAO eventMessageDAO;
	private final EnquiryAssessmentProvider assessmentProvider;

	// _________________________________________________________________________________________________________________

	public EnquiryService(EntityManager entityManager, EnquiryAssessmentProvider assessmentProvider) {
		this(new WeddingEnquiryDAO(entityManager), new UserAccountDAO(entityManager), new EventDAO(entityManager), new EventMessageDAO(entityManager), assessmentProvider);
	}

	private EnquiryService(WeddingEnquiryDAO weddingEnquiryDAO, UserAccountDAO userAccountDAO, EventDAO eventDAO, EventMessageDAO eventMessageDAO, EnquiryAssessmentProvider assessmentProvider) {
		super(weddingEnquiryDAO, WeddingEnquiry.class);
		this.weddingEnquiryDAO = weddingEnquiryDAO;
		this.userAccountDAO = userAccountDAO;
		this.eventDAO = eventDAO;
		this.eventMessageDAO = eventMessageDAO;
		this.assessmentProvider = assessmentProvider;
	}

	// _________________________________________________________________________________________________________________

	public EnquirySubmissionResponseDTO submit(Integer accountId, EnquirySubmissionRequestDTO request) {
		String submissionId = normalizeSubmissionId(request == null ? null : request.getSubmissionId());
		String language = normalizeLanguage(request.getLanguage());
		JsonNode draft = validateDraft(request.getDraft(), language);

		WeddingEnquiry existing = weddingEnquiryDAO.findBySubmissionId(submissionId);
		if (existing != null) {
			ensureSamePrimaryContact(existing, accountId);
			return toResponse(existing);
		}

		UserAccount primaryContact = userAccountDAO.getById(accountId);
		if (primaryContact == null) throw new ApiException(401, "Authenticated account not found");

		WeddingEnquiry enquiry = new WeddingEnquiry();
		enquiry.setSubmissionId(submissionId);
		enquiry.setLanguage(language);
		enquiry.setRawDraft(draft.deepCopy());
		enquiry.setStatus(EnquiryStatus.SUBMITTED);
		enquiry.setCreatedAt(Instant.now());
		if (assessmentProvider == null) throw new ApiException(503, "AI enquiry assessment is unavailable");
		AiEnquiryAssessmentDTO assessment = assessmentProvider.assess(toConversation(draft), language);
		enquiry.setAiAssessment(PoolConfig.getMapper().valueToTree(assessment));

		EnquiryContact contact = new EnquiryContact();
		contact.setUserAccount(primaryContact);
		contact.setPrimary(true);
		try {
			weddingEnquiryDAO.createSubmission(enquiry, contact);
		} catch (RuntimeException exception) {
			if (!isConstraintViolation(exception)) throw exception;
			EnquirySubmissionResponseDTO duplicate = findExistingForAccount(submissionId, accountId);
			if (duplicate != null) return duplicate;
			throw new ApiException(409, "This submission ID is already in use");
		}
		return toResponse(enquiry);
	}

	// _________________________________________________________________________________________________________________

	public List<EnquirySummaryResponseDTO> findForAccount(Integer accountId) {
		if (accountId == null) throw new ApiException(401, "Authenticated account not found");
		return EnquirySummaryResponseMapper.toDTOs(weddingEnquiryDAO.findForAccount(accountId));
	}

	// _________________________________________________________________________________________________________________

	public List<EnquiryOwnerReviewSummaryResponseDTO> findForOwnerReview() {
		return EnquiryOwnerReviewResponseMapper.toSummaryDTOs(weddingEnquiryDAO.findForOwnerReview());
	}

	// _________________________________________________________________________________________________________________

	public EnquiryOwnerReviewResponseDTO findOwnerReviewById(Integer id) {
		WeddingEnquiry enquiry = getById(id);
		if (enquiry == null) throw new ApiException(404, "Enquiry not found");
		enquiry.setEvent(eventDAO.findByEnquiryId(id));
		return EnquiryOwnerReviewResponseMapper.toDTO(enquiry);
	}

	// _________________________________________________________________________________________________________________

	public EnquiryOwnerReviewResponseDTO saveOwnerReview(Integer id, EnquiryOwnerReviewRequestDTO request) {
		if (request == null) throw new ApiException(400, "Review details are required");
		String internalNote = normalizeOptionalText(request.getInternalNote(), "Internal note", 10000);
		String customerQuestion = normalizeOptionalText(request.getCustomerQuestion(), "Customer question", 1500);
		WeddingEnquiry enquiry = getById(id);
		if (enquiry == null) throw new ApiException(404, "Enquiry not found");
		if (isClosed(enquiry.getStatus()) || enquiry.getStatus() == EnquiryStatus.APPROVED) {
			throw new ApiException(409, "This enquiry is no longer open for review");
		}
		Event event = eventDAO.findByEnquiryId(id);
		enquiry.setInternalNote(internalNote);
		enquiry.setCustomerQuestion(customerQuestion);
		if (event == null) enquiry.setStatus(customerQuestion == null ? EnquiryStatus.UNDER_REVIEW : EnquiryStatus.AWAITING_CUSTOMER);
		return EnquiryOwnerReviewResponseMapper.toDTO(update(enquiry));
	}

	// _________________________________________________________________________________________________________________

	public void closeByCustomer(String submissionId, Integer accountId) {
		WeddingEnquiry enquiry = weddingEnquiryDAO.findBySubmissionId(submissionId);
		if (enquiry == null || !weddingEnquiryDAO.hasPrimaryContact(enquiry.getId(), accountId)) throw new ApiException(404, "Enquiry not found");
		if (enquiry.getStatus() == EnquiryStatus.APPROVED || isClosed(enquiry.getStatus())) throw new ApiException(409, "An approved or closed enquiry cannot be declined");
		Event event = eventDAO.findByEnquiryId(enquiry.getId());
		if (event != null && event.getApprovedAt() != null) throw new ApiException(409, "An approved or closed enquiry cannot be declined");
		if (event == null) weddingEnquiryDAO.closeByCustomer(enquiry);
		else eventDAO.close(event, EventStatus.CLOSED_BY_CUSTOMER, EnquiryStatus.CLOSED_BY_CUSTOMER);
	}

	// _________________________________________________________________________________________________________________

	public EventApprovalResponseDTO approveOwnerEnquiry(Integer id, String customerNote) {
		WeddingEnquiry enquiry = getById(id);
		if (enquiry == null) throw new ApiException(404, "Enquiry not found");

		Event existingEvent = eventDAO.findByEnquiryId(id);
		if (existingEvent != null && existingEvent.getApprovedAt() != null) return EventResponseMapper.toApprovalDTO(existingEvent);
		if (isClosed(enquiry.getStatus()) || existingEvent != null && isClosed(existingEvent.getStatus())) {
			throw new ApiException(409, "A closed enquiry cannot be approved");
		}
		Event event = existingEvent;
		if (event == null) {
			event = new Event();
			event.setCategory(EventCategory.WEDDING);
			event.setStatus(EventStatus.APPROVED);
			event.setEventData(enquiry.getRawDraft().deepCopy());
			event.setGuestAccessTokenHash(EventAccessToken.createHash());
			event.setCreatedAt(Instant.now());
		}
		event.setCustomerNote(normalizeOptionalText(customerNote, "Customer note", 10000));
		event.setStatus(EventStatus.APPROVED);
		event.setApprovedAt(Instant.now());
		try {
			return EventResponseMapper.toApprovalDTO(existingEvent == null
					? eventDAO.approveAndCreate(enquiry, event)
					: eventDAO.approveExisting(event));
		} catch (RuntimeException exception) {
			if (!isConstraintViolation(exception)) throw exception;
			Event createdByConcurrentApproval = eventDAO.findByEnquiryId(id);
			if (createdByConcurrentApproval != null) return EventResponseMapper.toApprovalDTO(createdByConcurrentApproval);
			throw new ApiException(409, "The enquiry could not be approved in its current state");
		}
	}

	// _________________________________________________________________________________________________________________

	public List<EventMessageResponseDTO> findMessagesForOwner(Integer enquiryId) {
		WeddingEnquiry enquiry = getById(enquiryId);
		if (enquiry == null) throw new ApiException(404, "Enquiry not found");
		Event event = eventDAO.findByEnquiryId(enquiryId);
		return event == null ? List.of() : EventMessageResponseMapper.toDTOs(eventMessageDAO.findForEvent(event.getId()));
	}

	// _________________________________________________________________________________________________________________

	public EventMessageResponseDTO sendOwnerMessage(Integer enquiryId, Integer ownerAccountId, String content) {
		WeddingEnquiry enquiry = getById(enquiryId);
		if (enquiry == null) throw new ApiException(404, "Enquiry not found");
		Event event = eventDAO.findByEnquiryId(enquiryId);
		if (isClosed(enquiry.getStatus()) || event != null && isClosed(event.getStatus())) {
			throw new ApiException(409, "This request is closed");
		}
		EventMessage message = createMessage(ownerAccountId, EventMessageSender.OWNER, content);
		if (event == null) {
			event = new Event();
			event.setCategory(EventCategory.WEDDING);
			event.setStatus(EventStatus.FOLLOW_UP_REQUIRED);
			event.setEventData(enquiry.getRawDraft().deepCopy());
			event.setGuestAccessTokenHash(EventAccessToken.createHash());
			event.setCreatedAt(Instant.now());
			eventDAO.createForOwnerFollowUp(enquiry, event, message);
		} else {
			EventStatus eventStatus = event.getApprovedAt() == null ? EventStatus.FOLLOW_UP_REQUIRED : EventStatus.APPROVED;
			EnquiryStatus enquiryStatus = event.getApprovedAt() == null ? EnquiryStatus.FOLLOW_UP_REQUIRED : EnquiryStatus.APPROVED;
			eventDAO.addMessage(event, message, eventStatus, enquiryStatus);
		}
		return EventMessageResponseMapper.toDTO(message);
	}

	// _________________________________________________________________________________________________________________

	public void closeByOwner(Integer enquiryId, Integer ownerAccountId, String reason) {
		WeddingEnquiry enquiry = getById(enquiryId);
		if (enquiry == null) throw new ApiException(404, "Enquiry not found");
		Event event = eventDAO.findByEnquiryId(enquiryId);
		if (isClosed(enquiry.getStatus()) || enquiry.getStatus() == EnquiryStatus.APPROVED
				|| event != null && event.getApprovedAt() != null) {
			throw new ApiException(409, "An approved or closed event cannot be declined");
		}
		EventMessage message = reason == null || reason.isBlank() ? null : createMessage(ownerAccountId, EventMessageSender.OWNER, reason);
		if (event == null) {
			event = new Event();
			event.setCategory(EventCategory.WEDDING);
			event.setStatus(EventStatus.CLOSED_BY_OWNER);
			event.setEventData(enquiry.getRawDraft().deepCopy());
			event.setGuestAccessTokenHash(EventAccessToken.createHash());
			event.setCreatedAt(Instant.now());
			eventDAO.createForOwnerClosure(enquiry, event, message);
		} else {
			if (message == null) eventDAO.closeByOwner(event);
			else eventDAO.addMessage(event, message, EventStatus.CLOSED_BY_OWNER, EnquiryStatus.CLOSED_BY_OWNER);
		}
	}

	// _________________________________________________________________________________________________________________

	private EventMessage createMessage(Integer accountId, EventMessageSender senderType, String content) {
		if (content == null || content.isBlank()) throw new ApiException(400, "A message is required");
		String normalized = content.trim();
		if (normalized.length() > 5000) throw new ApiException(400, "The message is too long");
		UserAccount senderAccount = userAccountDAO.getById(accountId);
		if (senderAccount == null) throw new ApiException(401, "Authenticated account not found");
		EventMessage message = new EventMessage();
		message.setSenderAccount(senderAccount);
		message.setSenderType(senderType);
		message.setContent(normalized);
		message.setCreatedAt(Instant.now());
		return message;
	}

	// _________________________________________________________________________________________________________________

	private static boolean isClosed(EnquiryStatus status) {
		return status == EnquiryStatus.CLOSED_BY_OWNER
				|| status == EnquiryStatus.CLOSED_BY_CUSTOMER
				|| status == EnquiryStatus.CANCELLED_BY_CUSTOMER;
	}

	private static boolean isClosed(EventStatus status) {
		return status == EventStatus.CLOSED_BY_OWNER || status == EventStatus.CLOSED_BY_CUSTOMER;
	}

	// _________________________________________________________________________________________________________________

	private EnquirySubmissionResponseDTO findExistingForAccount(String submissionId, Integer accountId) {
		WeddingEnquiry enquiry = weddingEnquiryDAO.findBySubmissionId(submissionId);
		if (enquiry == null || !weddingEnquiryDAO.hasPrimaryContact(enquiry.getId(), accountId)) return null;
		return toResponse(enquiry);
	}

	// _________________________________________________________________________________________________________________

	private void ensureSamePrimaryContact(WeddingEnquiry enquiry, Integer accountId) {
		if (!weddingEnquiryDAO.hasPrimaryContact(enquiry.getId(), accountId)) {
			throw new ApiException(409, "This submission ID belongs to another account");
		}
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

	private static List<AiFlowTurnDTO> toConversation(JsonNode draft) {
		JsonNode conversation = draft.path("conversation");
		if (!conversation.isArray() || conversation.isEmpty()) {
			throw new ApiException(400, "The completed enquiry conversation is missing");
		}
		return PoolConfig.getMapper().convertValue(conversation,
				PoolConfig.getMapper().getTypeFactory().constructCollectionType(List.class, AiFlowTurnDTO.class));
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

	private static String normalizeOptionalText(String value, String fieldName, int maxLength) {
		if (value == null || value.isBlank()) return null;
		String trimmed = value.trim();
		if (trimmed.length() > maxLength) throw new ApiException(400, fieldName + " is too long");
		return trimmed;
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
}
