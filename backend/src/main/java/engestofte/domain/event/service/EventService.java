package engestofte.domain.event.service;

import engestofte.domain.event.dao.EventDAO;
import engestofte.domain.event.dao.EventMessageDAO;
import engestofte.domain.event.dto.response.EventCustomerResponseDTO;
import engestofte.domain.event.dto.response.EventMessageResponseDTO;
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

	// _________________________________________________________________________________________________________________

	public EventService(EntityManager entityManager) {
		this(new EventDAO(entityManager), new EventMessageDAO(entityManager), new UserAccountDAO(entityManager));
	}

	private EventService(EventDAO eventDAO, EventMessageDAO eventMessageDAO, UserAccountDAO userAccountDAO) {
		super(eventDAO, Event.class);
		this.eventDAO = eventDAO;
		this.eventMessageDAO = eventMessageDAO;
		this.userAccountDAO = userAccountDAO;
	}

	// _________________________________________________________________________________________________________________

	public EventCustomerResponseDTO findForAccount(Integer eventId, Integer accountId) {
		if (accountId == null) throw new ApiException(401, "Authenticated account not found");
		Event event = eventDAO.findForAccount(eventId, accountId);
		if (event == null) throw new ApiException(404, "Event not found");
		return EventResponseMapper.toCustomerDTO(event);
	}

	// _________________________________________________________________________________________________________________

	public java.util.List<EventMessageResponseDTO> findMessagesForAccount(Integer eventId, Integer accountId) {
		Event event = findEventForAccount(eventId, accountId);
		return EventMessageResponseMapper.toDTOs(eventMessageDAO.findForEvent(event.getId()));
	}

	// _________________________________________________________________________________________________________________

	public EventMessageResponseDTO sendCustomerMessage(Integer eventId, Integer accountId, String content) {
		Event event = findEventForAccount(eventId, accountId);
		if (event.getStatus() != EventStatus.FOLLOW_UP_REQUIRED && event.getStatus() != EventStatus.APPROVED) {
			throw new ApiException(409, "A customer reply is not currently required");
		}
		EventMessage message = createMessage(accountId, EventMessageSender.CUSTOMER, content);
		eventDAO.addMessage(event, message, EventStatus.OWNER_FOLLOW_UP_REQUIRED, EnquiryStatus.OWNER_FOLLOW_UP_REQUIRED);
		return EventMessageResponseMapper.toDTO(message);
	}

	// _________________________________________________________________________________________________________________

	public EventCustomerResponseDTO closeByCustomer(Integer eventId, Integer accountId) {
		Event event = findEventForAccount(eventId, accountId);
		if (event.getStatus() != EventStatus.FOLLOW_UP_REQUIRED || event.getApprovedAt() != null) {
			throw new ApiException(409, "Only an unapproved request awaiting a customer response can be closed");
		}
		eventDAO.close(event, EventStatus.CLOSED_BY_CUSTOMER, EnquiryStatus.CLOSED_BY_CUSTOMER);
		return EventResponseMapper.toCustomerDTO(eventDAO.findForAccount(eventId, accountId));
	}

	// _________________________________________________________________________________________________________________

	private Event findEventForAccount(Integer eventId, Integer accountId) {
		if (accountId == null) throw new ApiException(401, "Authenticated account not found");
		Event event = eventDAO.findForAccount(eventId, accountId);
		if (event == null) throw new ApiException(404, "Event not found");
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
