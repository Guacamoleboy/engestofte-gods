package engestofte.domain.event.service;

import engestofte.domain.event.dao.EventDAO;
import engestofte.domain.event.dao.EventMessageDAO;
import engestofte.domain.event.dao.ChangeProposalDAO;
import engestofte.domain.event.changeproposal.ChangeApprovalDecision;
import engestofte.domain.event.changeproposal.ChangeApprovalParty;
import engestofte.domain.event.changeproposal.ChangeProposalStatus;
import engestofte.domain.event.dto.response.ChangeProposalResponseDTO;
import engestofte.domain.event.entity.ChangeProposal;
import engestofte.domain.event.mapper.response.ChangeProposalResponseMapper;
import engestofte.domain.event.dto.response.EventCustomerResponseDTO;
import engestofte.domain.event.dto.response.EventMessageResponseDTO;
import engestofte.domain.event.dto.response.EventOwnerResponseDTO;
import engestofte.domain.event.dto.response.EventOperationalResponseDTO;
import engestofte.domain.event.dto.response.ImportantMessageResponseDTO;
import engestofte.domain.event.entity.Event;
import engestofte.domain.event.entity.EventMessage;
import engestofte.domain.event.enums.EventMessageSender;
import engestofte.domain.event.enums.EventStatus;
import engestofte.domain.enquiry.enums.EnquiryStatus;
import engestofte.domain.event.mapper.response.EventResponseMapper;
import engestofte.domain.event.mapper.response.EventMessageResponseMapper;
import engestofte.domain.useraccount.dao.UserAccountDAO;
import engestofte.domain.useraccount.entity.UserAccount;
import engestofte.exception.ApiException;
import engestofte.service.EntityManagerService;
import jakarta.persistence.EntityManager;

public class EventService extends EntityManagerService<Event> {

	// Attributes
	private final EventDAO eventDAO;
	private final EventMessageDAO eventMessageDAO;
	private final UserAccountDAO userAccountDAO;
	private final ChangeProposalDAO changeProposalDAO;

	// _________________________________________________________________________________________________________________

	public EventService(EntityManager entityManager) {
		this(new EventDAO(entityManager), new EventMessageDAO(entityManager), new UserAccountDAO(entityManager), new ChangeProposalDAO(entityManager));
	}

	private EventService(EventDAO eventDAO, EventMessageDAO eventMessageDAO, UserAccountDAO userAccountDAO, ChangeProposalDAO changeProposalDAO) {
		super(eventDAO, Event.class);
		this.eventDAO = eventDAO;
		this.eventMessageDAO = eventMessageDAO;
		this.userAccountDAO = userAccountDAO;
		this.changeProposalDAO = changeProposalDAO;
	}

	// _________________________________________________________________________________________________________________

	public EventCustomerResponseDTO findForAccount(Integer eventId, Integer accountId) {
		if (accountId == null) throw new ApiException(401, "Authenticated account not found");
		Event event = eventDAO.findForAccount(eventId, accountId);
		if (event == null) throw new ApiException(404, "Event not found");
		if (event.getApprovedAt() != null) eventMessageDAO.markEventMessagesRead(eventId, accountId);
		return EventResponseMapper.toCustomerDTO(event, eventDAO.findPrimaryContactForEvent(eventId), eventDAO.isPrimaryContact(eventId, accountId));
	}

	// _________________________________________________________________________________________________________________

	public java.util.List<ChangeProposalResponseDTO> findChangeProposalsForAccount(Integer eventId, Integer accountId) {
		Event event = findEventForAccount(eventId, accountId);
		return ChangeProposalResponseMapper.toDTOs(changeProposalDAO.findForEvent(event.getId()));
	}

	// _________________________________________________________________________________________________________________

	public java.util.List<ChangeProposalResponseDTO> findChangeProposalsForOwner(Integer eventId) {
		Event event = findApprovedEvent(eventId);
		return ChangeProposalResponseMapper.toDTOs(changeProposalDAO.findForEvent(event.getId()));
	}

	// _________________________________________________________________________________________________________________

	public ChangeProposalResponseDTO proposeEventChange(Integer eventId, Integer accountId, String fieldName, String newValue, boolean owner) {
		Event event = findApprovedEvent(eventId);
		if (!owner && !eventDAO.isPrimaryContact(eventId, accountId)) throw new ApiException(403, "Only the primary contact can propose event changes");
		if (event.getStatus() != EventStatus.APPROVED && event.getStatus() != EventStatus.AWAITING_APPROVAL
				&& event.getStatus() != EventStatus.AWAITING_DEPOSIT && event.getStatus() != EventStatus.BOOKED) {
			throw new ApiException(409, "This event cannot accept changes in its current state");
		}
		if (newValue == null || (newValue.isBlank() && !"allergy_details".equals(fieldName))) throw new ApiException(400, "A new value is required");
		String normalized = newValue.trim();
		String oldValue = currentEventFieldValue(event, fieldName);
		if (oldValue == null) throw new ApiException(400, "This event field cannot be changed");
		if (("customer_name".equals(fieldName) || "event_name".equals(fieldName)) && normalized.length() > 160) throw new ApiException(400, "The name is too long");
		if ("expected_guest_count".equals(fieldName)) {
			try {
				int guestCount = Integer.parseInt(normalized);
				if (guestCount < 1 || guestCount > 5000) throw new NumberFormatException();
			} catch (NumberFormatException exception) {
				throw new ApiException(400, "Enter a valid guest count");
			}
		}
		if ("expected_vegan_count".equals(fieldName)) {
			try {
				int veganCount = Integer.parseInt(normalized);
				int guestCount = event.getEventData().path("expectedGuestCount").asInt(150);
				if (veganCount < 0 || veganCount > 150 || veganCount > guestCount) throw new NumberFormatException();
			} catch (NumberFormatException exception) {
				throw new ApiException(400, "Enter a valid vegan guest count");
			}
		}
		if ("has_allergies".equals(fieldName) && !"true".equalsIgnoreCase(normalized) && !"false".equalsIgnoreCase(normalized)) throw new ApiException(400, "Allergy status must be yes or no");
		if ("allergy_details".equals(fieldName) && normalized.length() > 1000) throw new ApiException(400, "Allergy details are too long");
		if ("wedding_direction".equals(fieldName)) {
			try {
				int direction = Integer.parseInt(normalized);
				if (direction < 1 || direction > 3) throw new NumberFormatException();
			} catch (NumberFormatException exception) {
				throw new ApiException(400, "Choose a valid wedding package");
			}
		}
		if ("requested_date".equals(fieldName) && normalized.length() > 1000) throw new ApiException(400, "The requested date is too long");
		if (oldValue.equals(normalized)) throw new ApiException(409, "The proposed value is already approved");
		UserAccount proposer = userAccountDAO.getById(accountId);
		if (proposer == null) throw new ApiException(401, "Authenticated account not found");
		ChangeApprovalParty party = owner ? ChangeApprovalParty.OWNER : ChangeApprovalParty.CUSTOMER;
		ChangeProposal proposal = changeProposalDAO.createProposal(event, proposer, party, fieldName, oldValue, normalized);
		return ChangeProposalResponseMapper.toDTO(proposal);
	}

	// _________________________________________________________________________________________________________________

	private static String currentEventFieldValue(Event event, String fieldName) {
		return switch (fieldName == null ? "" : fieldName) {
			case "customer_name" -> event.getEventData().path("customerName").asText(null);
			case "event_name" -> {
				String name = event.getEventData().path("eventName").asText();
				yield name.isBlank() ? "Bryllupsevent" : name;
			}
			case "expected_guest_count" -> event.getEventData().path("expectedGuestCount").isNumber()
					? event.getEventData().path("expectedGuestCount").asText()
					: null;
			case "expected_vegan_count" -> event.getEventData().path("expectedVeganCount").isNumber()
					? event.getEventData().path("expectedVeganCount").asText() : "";
			case "has_allergies" -> event.getEventData().path("hasAllergies").isBoolean()
					? event.getEventData().path("hasAllergies").asText() : "";
			case "allergy_details" -> event.getEventData().path("allergyDetails").asText("");
			case "wedding_direction" -> event.getEventData().path("weddingDirection").isNumber()
					? event.getEventData().path("weddingDirection").asText() : "";
			case "requested_date" -> {
				com.fasterxml.jackson.databind.JsonNode conversation = event.getEventData().path("conversation");
				if (!conversation.isArray() || conversation.size() <= 1) yield null;
				String value = conversation.get(1).path("answer").asText();
				yield value.isBlank() ? null : value;
			}
			default -> null;
		};
	}

	// _________________________________________________________________________________________________________________

	public ChangeProposalResponseDTO decideChangeProposal(Integer eventId, Integer proposalId, Integer accountId, ChangeApprovalDecision decision, String explanation, boolean owner) {
		Event event = owner ? findApprovedEvent(eventId) : findEventForAccount(eventId, accountId);
		if (isClosed(event)) throw new ApiException(409, "A closed event cannot resolve change proposals");
		if (!owner && !eventDAO.isPrimaryContact(eventId, accountId)) throw new ApiException(403, "Only the primary contact can decide event changes");
		if (decision == null) throw new ApiException(400, "An approval decision is required");
		ChangeProposal proposal = changeProposalDAO.findForEvent(event.getId()).stream()
			.filter(item -> item.getId().equals(proposalId))
			.findFirst()
			.orElseThrow(() -> new ApiException(404, "Change proposal not found"));
		ChangeApprovalParty party = owner ? ChangeApprovalParty.OWNER : ChangeApprovalParty.CUSTOMER;
		if (proposal.getProposerParty() == party) throw new ApiException(403, "The proposing party cannot approve its own change");
		if (proposal.getStatus() != ChangeProposalStatus.PENDING) throw new ApiException(409, "This change proposal is already resolved");
		String normalizedExplanation = explanation == null ? null : explanation.trim();
		if (decision == ChangeApprovalDecision.REJECTED && (normalizedExplanation == null || normalizedExplanation.isBlank())) throw new ApiException(400, "A rejection explanation is required");
		if (normalizedExplanation != null && normalizedExplanation.length() > 1000) throw new ApiException(400, "The explanation is too long");
		UserAccount actor = userAccountDAO.getById(accountId);
		if (actor == null) throw new ApiException(401, "Authenticated account not found");
		ChangeProposal decided = changeProposalDAO.recordDecision(proposalId, actor, party, decision, normalizedExplanation);
		if (decided == null) throw new ApiException(404, "Change proposal not found");
		if (decided.getStatus() == ChangeProposalStatus.PENDING) throw new ApiException(409, "This party has already decided this proposal");
		return ChangeProposalResponseMapper.toDTO(decided);
	}

	// _________________________________________________________________________________________________________________

	public void addContactPerson(Integer eventId, Integer accountId, String email, boolean owner) {
		Event event = findApprovedEvent(eventId);
		if (!owner && !eventDAO.isPrimaryContact(eventId, accountId)) throw new ApiException(403, "Only the primary contact can manage event contacts");
		if (email == null || !email.trim().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) throw new ApiException(400, "A valid email address is required");
		UserAccount contact = userAccountDAO.findByEmailIgnoreCase(email.trim());
		if (contact == null) throw new ApiException(404, "No customer account exists for this email address");
		eventDAO.addContactPerson(event.getId(), contact);
	}

	// _________________________________________________________________________________________________________________

	public EventOwnerResponseDTO findForOwner(Integer eventId, Integer ownerAccountId) {
		Event event = findApprovedEvent(eventId);
		UserAccount primaryContact = eventDAO.findPrimaryContactForEvent(eventId);
		if (primaryContact == null) throw new ApiException(404, "Event not found");
		eventMessageDAO.markEventMessagesRead(eventId, ownerAccountId);
		return EventResponseMapper.toOwnerDTO(event, primaryContact);
	}

	// _________________________________________________________________________________________________________________

	public EventOperationalResponseDTO findForStaff(Integer eventId, Integer staffAccountId) {
		Event event = findApprovedEvent(eventId);
		eventMessageDAO.markEventMessagesRead(eventId, staffAccountId);
		return toOperationalDTOForStaff(event);
	}

	// _________________________________________________________________________________________________________________

	public java.util.List<EventOperationalResponseDTO> findAllForStaff() {
		return eventDAO.findAllApprovedForStaff().stream().map(this::toOperationalDTOForStaff).toList();
	}

	// _________________________________________________________________________________________________________________

	private EventOperationalResponseDTO toOperationalDTOForStaff(Event event) {
		UserAccount primaryContact = eventDAO.findPrimaryContactForEvent(event.getId());
		StaffEventDataRedactor.RedactedEventData redacted = StaffEventDataRedactor.redact(event, primaryContact == null ? null : primaryContact.getFullName());
		EventOperationalResponseDTO response = EventResponseMapper.toOperationalDTO(event, redacted.customerName(), redacted.eventName(), StaffEventDataRedactor.redactRequestedDate(event));
		response.setAllergyDetails(StaffEventDataRedactor.redactContactDetails(response.getAllergyDetails()));
		return response;
	}

	// _________________________________________________________________________________________________________________

	public java.util.List<EventMessageResponseDTO> findMessagesForOwner(Integer eventId, Integer ownerAccountId) {
		Event event = findApprovedEvent(eventId);
		eventMessageDAO.markEventMessagesRead(event.getId(), ownerAccountId);
		return EventMessageResponseMapper.toDTOs(eventMessageDAO.findForEvent(event.getId()), ownerAccountId);
	}

	// _________________________________________________________________________________________________________________

	public EventMessageResponseDTO sendOwnerMessage(Integer eventId, Integer ownerAccountId, String content) {
		Event event = findApprovedEvent(eventId);
		if (isClosed(event)) throw new ApiException(409, "A closed event cannot receive messages");
		EventMessage message = createMessage(ownerAccountId, EventMessageSender.OWNER, content);
		boolean hasPendingChanges = changeProposalDAO.hasPendingForEvent(eventId);
		EventStatus eventStatus = eventStatusAfterMessage(event, hasPendingChanges);
		EnquiryStatus enquiryStatus = event.getStatus() == EventStatus.AWAITING_DEPOSIT || event.getStatus() == EventStatus.BOOKED
				? event.getWeddingEnquiry().getStatus() : EnquiryStatus.APPROVED;
		eventDAO.addMessage(event, message, eventStatus, enquiryStatus);
		return EventMessageResponseMapper.toDTO(message, ownerAccountId);
	}

	// _________________________________________________________________________________________________________________

	public EventMessageResponseDTO sendStaffMessage(Integer eventId, Integer staffAccountId, String content) {
		Event event = findApprovedEvent(eventId);
		if (isClosed(event)) throw new ApiException(409, "A closed event cannot receive messages");
		EventMessage message = createMessage(staffAccountId, EventMessageSender.STAFF, content);
		eventDAO.addMessage(event, message, event.getStatus(), event.getWeddingEnquiry().getStatus());
		EventMessageResponseDTO response = EventMessageResponseMapper.toDTO(message, staffAccountId);
		response.setSenderName(StaffEventDataRedactor.redactContactDetails(response.getSenderName()));
		response.setContent(StaffEventDataRedactor.redactContactDetails(response.getContent()));
		return response;
	}

	// _________________________________________________________________________________________________________________

	public java.util.List<EventMessageResponseDTO> findMessagesForAccount(Integer eventId, Integer accountId) {
		Event event = findEventForAccount(eventId, accountId);
		eventMessageDAO.markEventMessagesRead(event.getId(), accountId);
		return EventMessageResponseMapper.toDTOs(eventMessageDAO.findForEvent(event.getId()), accountId);
	}

	// _________________________________________________________________________________________________________________

	public java.util.List<ImportantMessageResponseDTO> findImportantMessages(Integer accountId) {
		java.time.Instant cutoff = java.time.Instant.now().minus(java.time.Duration.ofDays(7));
		return summarizeMessages(eventMessageDAO.findEscalatedForAccount(accountId, cutoff), false);
	}

	// _________________________________________________________________________________________________________________

	public java.util.List<ImportantMessageResponseDTO> findStaffImportantMessages(Integer accountId) {
		return summarizeMessages(eventMessageDAO.findUnreadForAccount(accountId), true);
	}

	// _________________________________________________________________________________________________________________

	private java.util.List<ImportantMessageResponseDTO> summarizeMessages(java.util.List<engestofte.domain.event.entity.EventMessageRecipient> recipients, boolean redactContactDetails) {
		java.util.Map<Integer, ImportantMessageResponseDTO> grouped = new java.util.LinkedHashMap<>();
		for (var recipient : recipients) {
			EventMessage message = recipient.getMessage();
			Event event = message.getEvent();
			ImportantMessageResponseDTO summary = grouped.computeIfAbsent(event.getId(), eventId -> {
				ImportantMessageResponseDTO item = new ImportantMessageResponseDTO();
				item.setEventId(eventId);
				String name = event.getEventData().path("eventName").asText();
				if (redactContactDetails) name = StaffEventDataRedactor.redactContactDetails(name);
				item.setEventName(name.isBlank() ? "Bryllupsevent" : name);
				item.setLatestMessage(redactContactDetails
						? StaffEventDataRedactor.redactContactDetails(message.getContent())
						: message.getContent());
				item.setLatestMessageAt(message.getCreatedAt());
				return item;
			});
			summary.setUnreadCount(summary.getUnreadCount() + 1);
		}
		return java.util.List.copyOf(grouped.values());
	}

	// _________________________________________________________________________________________________________________

	public EventMessageResponseDTO sendCustomerMessage(Integer eventId, Integer accountId, String content) {
		Event event = findEventForAccount(eventId, accountId);
		if (isClosed(event)) throw new ApiException(409, "A closed event cannot receive messages");
		EventMessage message = createMessage(accountId, EventMessageSender.CUSTOMER, content);
		boolean hasPendingChanges = changeProposalDAO.hasPendingForEvent(eventId);
		EventStatus eventStatus = eventStatusAfterMessage(event, hasPendingChanges);
		EnquiryStatus enquiryStatus = event.getStatus() == EventStatus.AWAITING_DEPOSIT || event.getStatus() == EventStatus.BOOKED
				? event.getWeddingEnquiry().getStatus()
				: event.getApprovedAt() == null ? EnquiryStatus.OWNER_FOLLOW_UP_REQUIRED : EnquiryStatus.APPROVED;
		eventDAO.addMessage(event, message, eventStatus, enquiryStatus);
		return EventMessageResponseMapper.toDTO(message, accountId);
	}

	// _________________________________________________________________________________________________________________

	public EventCustomerResponseDTO closeByCustomer(Integer eventId, Integer accountId) {
		Event event = findEventForAccount(eventId, accountId);
		if (!eventDAO.isPrimaryContact(eventId, accountId)) throw new ApiException(403, "Only the primary contact can remove this event");
		if (isClosed(event)) throw new ApiException(409, "This event is already cancelled or closed");
		eventDAO.updateStatus(event, EventStatus.CANCELLED_BY_CUSTOMER, EnquiryStatus.CANCELLED_BY_CUSTOMER);
		return EventResponseMapper.toCustomerDTO(eventDAO.findForAccount(eventId, accountId), eventDAO.findPrimaryContactForEvent(eventId), true);
	}

	// _________________________________________________________________________________________________________________

	public void closeByOwner(Integer eventId) {
		Event event = findApprovedEvent(eventId);
		ensureEventCanBeClosed(event);
		eventDAO.close(event, EventStatus.CLOSED_BY_OWNER, EnquiryStatus.CLOSED_BY_OWNER);
	}

	// _________________________________________________________________________________________________________________

	public void requestDeposit(Integer eventId) {
		Event event = findApprovedEvent(eventId);
		if (event.getStatus() != EventStatus.APPROVED) throw new ApiException(409, "The event must be fully approved before requesting a deposit");
		if (changeProposalDAO.hasPendingForEvent(eventId)) throw new ApiException(409, "Pending event changes must be resolved before requesting a deposit");
		eventDAO.updateStatus(event, EventStatus.AWAITING_DEPOSIT, EnquiryStatus.AWAITING_DEPOSIT);
	}

	// _________________________________________________________________________________________________________________

	public void payDeposit(Integer eventId, Integer accountId) {
		Event event = findEventForAccount(eventId, accountId);
		if (!eventDAO.isPrimaryContact(eventId, accountId)) throw new ApiException(403, "Only the primary contact can pay the deposit");
		if (event.getStatus() != EventStatus.AWAITING_DEPOSIT) throw new ApiException(409, "The Owner must request the deposit before it can be paid");
		if (changeProposalDAO.hasPendingForEvent(eventId)) throw new ApiException(409, "Pending event changes must be resolved before booking");
		eventDAO.updateStatus(event, EventStatus.BOOKED, EnquiryStatus.BOOKED);
	}

	// _________________________________________________________________________________________________________________

	private static void ensureEventCanBeClosed(Event event) {
		if (isClosed(event)) {
			throw new ApiException(409, "This event is already closed");
		}
		if (event.getStatus() == EventStatus.BOOKED) throw new ApiException(409, "A booked event cannot be removed");
	}

	// _________________________________________________________________________________________________________________

	private static boolean isClosed(Event event) {
		return event.getStatus() == EventStatus.CLOSED_BY_OWNER || event.getStatus() == EventStatus.CLOSED_BY_CUSTOMER || event.getStatus() == EventStatus.CANCELLED_BY_CUSTOMER;
	}

	// _________________________________________________________________________________________________________________

	private static EventStatus eventStatusAfterMessage(Event event, boolean hasPendingChanges) {
		if (event.getStatus() == EventStatus.AWAITING_DEPOSIT || event.getStatus() == EventStatus.BOOKED) return event.getStatus();
		if (hasPendingChanges) return EventStatus.AWAITING_APPROVAL;
		return event.getApprovedAt() == null ? EventStatus.OWNER_FOLLOW_UP_REQUIRED : EventStatus.APPROVED;
	}

	// _________________________________________________________________________________________________________________

	private Event findEventForAccount(Integer eventId, Integer accountId) {
		if (accountId == null) throw new ApiException(401, "Authenticated account not found");
		Event event = eventDAO.findForAccount(eventId, accountId);
		if (event == null) throw new ApiException(404, "Event not found");
		return event;
	}

	// _________________________________________________________________________________________________________________

	private Event findApprovedEvent(Integer eventId) {
		Event event = eventDAO.findForOwner(eventId);
		if (event == null || event.getApprovedAt() == null) {
			throw new ApiException(404, "Approved event not found");
		}
		return event;
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
		message.setCreatedAt(java.time.Instant.now());
		return message;
	}
}
