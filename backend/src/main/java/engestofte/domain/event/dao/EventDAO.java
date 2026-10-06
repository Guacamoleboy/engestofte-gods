package engestofte.domain.event.dao;

import engestofte.dao.EntityManagerDAO;
import engestofte.domain.enquiry.entity.WeddingEnquiry;
import engestofte.domain.enquiry.entity.EnquiryContact;
import engestofte.domain.enquiry.enums.EnquiryStatus;
import engestofte.domain.event.entity.Event;
import engestofte.domain.event.entity.EventMessage;
import engestofte.domain.event.entity.EventMessageRecipient;
import engestofte.domain.event.enums.EventMessageSender;
import engestofte.domain.event.enums.EventStatus;
import engestofte.domain.useraccount.entity.UserAccount;
import jakarta.persistence.EntityManager;

public class EventDAO extends EntityManagerDAO<Event> {

	// Attributes

	// _________________________________________________________________________________________________________________

	public EventDAO(EntityManager em) {
		super(em, Event.class);
	}

	// _________________________________________________________________________________________________________________

	public Event findByEnquiryId(Integer enquiryId) {
		return executeQuery(() -> em.createQuery(
				"SELECT event FROM Event event WHERE event.weddingEnquiry.id = :enquiryId",
				Event.class)
			.setParameter("enquiryId", enquiryId)
			.getResultStream()
			.findFirst()
			.orElse(null));
	}

	// _________________________________________________________________________________________________________________

	public Event findForOwner(Integer eventId) {
		return executeQuery(() -> em.createQuery(
				"SELECT event FROM Event event JOIN FETCH event.weddingEnquiry WHERE event.id = :eventId",
				Event.class)
			.setParameter("eventId", eventId)
			.getResultStream()
			.findFirst()
			.orElse(null));
	}

	// _________________________________________________________________________________________________________________

	public java.util.List<Event> findAllApprovedForStaff() {
		return executeQuery(() -> em.createQuery(
				"SELECT event FROM Event event JOIN FETCH event.weddingEnquiry WHERE event.approvedAt IS NOT NULL ORDER BY event.createdAt ASC",
				Event.class)
			.getResultList());
	}

	// _________________________________________________________________________________________________________________

	public UserAccount findPrimaryContactForEvent(Integer eventId) {
		return executeQuery(() -> em.createQuery(
				"SELECT account FROM Event event JOIN event.weddingEnquiry enquiry JOIN EnquiryContact contact ON contact.enquiry = enquiry JOIN contact.userAccount account WHERE event.id = :eventId AND contact.primary = true",
				UserAccount.class)
			.setParameter("eventId", eventId)
			.getResultStream()
			.findFirst()
			.orElse(null));
	}

	// _________________________________________________________________________________________________________________

	public Event findForAccount(Integer eventId, Integer accountId) {
		return executeQuery(() -> em.createQuery(
				"SELECT event FROM Event event JOIN FETCH event.weddingEnquiry enquiry JOIN EnquiryContact contact ON contact.enquiry = enquiry WHERE event.id = :eventId AND contact.userAccount.id = :accountId",
				Event.class)
			.setParameter("eventId", eventId)
			.setParameter("accountId", accountId)
			.getResultStream()
			.findFirst()
			.orElse(null));
	}

	// _________________________________________________________________________________________________________________

	public boolean isPrimaryContact(Integer eventId, Integer accountId) {
		return executeQuery(() -> em.createQuery(
				"SELECT COUNT(contact) FROM EnquiryContact contact WHERE contact.enquiry.event.id = :eventId AND contact.userAccount.id = :accountId AND contact.primary = true",
				Long.class)
			.setParameter("eventId", eventId)
			.setParameter("accountId", accountId)
			.getSingleResult() > 0);
	}

	// _________________________________________________________________________________________________________________

	public void addContactPerson(Integer eventId, UserAccount userAccount) {
		executeQuery(() -> {
			Event event = em.createQuery("SELECT event FROM Event event JOIN FETCH event.weddingEnquiry WHERE event.id = :eventId", Event.class)
				.setParameter("eventId", eventId)
				.getResultStream().findFirst().orElse(null);
			if (event == null) return null;
			Long existing = em.createQuery("SELECT COUNT(contact) FROM EnquiryContact contact WHERE contact.enquiry.id = :enquiryId AND contact.userAccount.id = :accountId", Long.class)
				.setParameter("enquiryId", event.getWeddingEnquiry().getId())
				.setParameter("accountId", userAccount.getId())
				.getSingleResult();
			if (existing == 0) {
				EnquiryContact contact = new EnquiryContact();
				contact.setEnquiry(event.getWeddingEnquiry());
				contact.setUserAccount(userAccount);
				contact.setPrimary(false);
				em.persist(contact);
			}
			return null;
		});
	}

	// _________________________________________________________________________________________________________________

	public Event approveAndCreate(WeddingEnquiry enquiry, Event event) {
		return executeQuery(() -> {
			WeddingEnquiry managedEnquiry = em.merge(enquiry);
			managedEnquiry.setStatus(EnquiryStatus.APPROVED);
			event.setWeddingEnquiry(managedEnquiry);
			em.persist(event);
			em.flush();
			return event;
		});
	}

	// _________________________________________________________________________________________________________________

	public Event approveExisting(Event event) {
		return executeQuery(() -> {
			Event managedEvent = em.merge(event);
			WeddingEnquiry managedEnquiry = em.merge(managedEvent.getWeddingEnquiry());
			managedEvent.setWeddingEnquiry(managedEnquiry);
			managedEvent.setStatus(EventStatus.APPROVED);
			managedEvent.setApprovedAt(java.time.Instant.now());
			managedEnquiry.setStatus(EnquiryStatus.APPROVED);
			return managedEvent;
		});
	}

	// _________________________________________________________________________________________________________________

	public Event createForOwnerFollowUp(WeddingEnquiry enquiry, Event event, EventMessage message) {
		return executeQuery(() -> {
			WeddingEnquiry managedEnquiry = em.merge(enquiry);
			managedEnquiry.setStatus(EnquiryStatus.FOLLOW_UP_REQUIRED);
			managedEnquiry.setCustomerQuestion(null);
			event.setWeddingEnquiry(managedEnquiry);
			event.setStatus(EventStatus.FOLLOW_UP_REQUIRED);
			em.persist(event);
			em.flush();
			message.setEvent(event);
			message.setSenderType(EventMessageSender.OWNER);
			em.persist(message);
			persistMessageRecipients(event, message);
			return event;
		});
	}

	// _________________________________________________________________________________________________________________

	public Event createForOwnerClosure(WeddingEnquiry enquiry, Event event, EventMessage message) {
		return executeQuery(() -> {
			WeddingEnquiry managedEnquiry = em.merge(enquiry);
			managedEnquiry.setStatus(EnquiryStatus.CLOSED_BY_OWNER);
			managedEnquiry.setCustomerQuestion(null);
			event.setWeddingEnquiry(managedEnquiry);
			event.setStatus(EventStatus.CLOSED_BY_OWNER);
		em.persist(event);
		if (message != null) {
			em.flush();
			message.setEvent(event);
			message.setSenderType(EventMessageSender.OWNER);
			em.persist(message);
			persistMessageRecipients(event, message);
		}
			return event;
		});
	}

	// _________________________________________________________________________________________________________________

	public Event closeByOwner(Event event) {
		return close(event, EventStatus.CLOSED_BY_OWNER, EnquiryStatus.CLOSED_BY_OWNER);
	}

	// _________________________________________________________________________________________________________________

	public Event addMessage(Event event, EventMessage message, EventStatus eventStatus, EnquiryStatus enquiryStatus) {
		return executeQuery(() -> {
			Event managedEvent = em.merge(event);
			WeddingEnquiry managedEnquiry = em.merge(managedEvent.getWeddingEnquiry());
			managedEvent.setWeddingEnquiry(managedEnquiry);
			managedEvent.setStatus(eventStatus);
			managedEnquiry.setStatus(enquiryStatus);
			if (message.getSenderType() == EventMessageSender.OWNER) managedEnquiry.setCustomerQuestion(null);
			message.setEvent(managedEvent);
			em.persist(message);
			persistMessageRecipients(managedEvent, message);
			return managedEvent;
		});
	}

	// _________________________________________________________________________________________________________________

	private void persistMessageRecipients(Event event, EventMessage message) {
		java.util.List<UserAccount> recipients = new java.util.ArrayList<>(em.createQuery(
				"SELECT account FROM EnquiryContact contact JOIN contact.userAccount account WHERE contact.enquiry = :enquiry",
				UserAccount.class)
			.setParameter("enquiry", event.getWeddingEnquiry())
			.getResultList());
		if (message.getSenderType() == EventMessageSender.CUSTOMER) {
			recipients.addAll(em.createQuery(
					"SELECT account FROM UserAccount account WHERE account.role.name = :ownerRole",
					UserAccount.class)
				.setParameter("ownerRole", engestofte.domain.role.enums.RoleName.OWNER)
				.getResultList());
			recipients.addAll(em.createQuery(
					"SELECT DISTINCT message.senderAccount FROM EventMessage message WHERE message.event = :event AND message.senderType = :senderType",
					UserAccount.class)
				.setParameter("event", event)
				.setParameter("senderType", EventMessageSender.STAFF)
				.getResultList());
		}
		for (UserAccount recipient : recipients.stream().distinct().toList()) {
			if (recipient.getId().equals(message.getSenderAccount().getId())) continue;
			EventMessageRecipient messageRecipient = new EventMessageRecipient();
			messageRecipient.setMessage(message);
			messageRecipient.setRecipient(recipient);
			em.persist(messageRecipient);
		}
	}

	// _________________________________________________________________________________________________________________

	public Event close(Event event, EventStatus eventStatus, EnquiryStatus enquiryStatus) {
		return executeQuery(() -> {
			Event managedEvent = em.merge(event);
			WeddingEnquiry managedEnquiry = em.merge(managedEvent.getWeddingEnquiry());
			managedEvent.setStatus(eventStatus);
			managedEnquiry.setStatus(enquiryStatus);
			return managedEvent;
		});
	}

	// _________________________________________________________________________________________________________________

	public Event findForGuestInvitation(Integer eventId) {
		return executeQuery(() -> em.createQuery(
				"SELECT event FROM Event event JOIN FETCH event.weddingEnquiry WHERE event.id = :eventId",
				Event.class)
			.setParameter("eventId", eventId)
			.getResultStream()
			.findFirst()
			.orElse(null));
	}

	// _________________________________________________________________________________________________________________

	public void setGuestAccessTokenHash(Event event, String accessTokenHash) {
		executeQuery(() -> {
			Event managedEvent = em.merge(event);
			managedEvent.setGuestAccessTokenHash(accessTokenHash);
			return null;
		});
	}

	// _________________________________________________________________________________________________________________

	public Event updateStatus(Event event, EventStatus eventStatus, EnquiryStatus enquiryStatus) {
		return executeQuery(() -> {
			Event managedEvent = em.merge(event);
			WeddingEnquiry managedEnquiry = em.merge(managedEvent.getWeddingEnquiry());
			managedEvent.setWeddingEnquiry(managedEnquiry);
			managedEvent.setStatus(eventStatus);
			managedEnquiry.setStatus(enquiryStatus);
			return managedEvent;
		});
	}
}
