package engestofte.domain.event.dao;

import engestofte.dao.EntityManagerDAO;
import engestofte.domain.event.entity.EventMessage;
import engestofte.domain.event.entity.EventMessageRecipient;
import jakarta.persistence.EntityManager;

import java.time.Instant;
import java.util.List;

public class EventMessageDAO extends EntityManagerDAO<EventMessage> {

	// Attributes

	// _________________________________________________________________________________________________________________

	public EventMessageDAO(EntityManager em) {
		super(em, EventMessage.class);
	}

	// _________________________________________________________________________________________________________________

	public List<EventMessage> findForEvent(Integer eventId) {
		return executeQuery(() -> em.createQuery(
				"SELECT message FROM EventMessage message JOIN FETCH message.senderAccount WHERE message.event.id = :eventId ORDER BY message.createdAt ASC",
				EventMessage.class)
			.setParameter("eventId", eventId)
			.getResultList());
	}

	// _________________________________________________________________________________________________________________

	public void markEventMessagesRead(Integer eventId, Integer accountId) {
		executeQuery(() -> em.createQuery(
				"UPDATE EventMessageRecipient recipient SET recipient.readAt = :readAt WHERE recipient.message.event.id = :eventId AND recipient.recipient.id = :accountId AND recipient.readAt IS NULL")
			.setParameter("readAt", Instant.now())
			.setParameter("eventId", eventId)
			.setParameter("accountId", accountId)
			.executeUpdate());
	}

	public List<EventMessageRecipient> findEscalatedForAccount(Integer accountId, Instant cutoff) {
		return executeQuery(() -> em.createQuery(
				"SELECT recipient FROM EventMessageRecipient recipient JOIN FETCH recipient.message message JOIN FETCH message.event event JOIN FETCH message.senderAccount WHERE recipient.recipient.id = :accountId AND recipient.readAt IS NULL AND EXISTS (SELECT escalated.id FROM EventMessageRecipient escalated WHERE escalated.recipient.id = :accountId AND escalated.readAt IS NULL AND escalated.message.event = message.event AND escalated.message.createdAt <= :cutoff) ORDER BY message.createdAt DESC",
				EventMessageRecipient.class)
			.setParameter("accountId", accountId)
			.setParameter("cutoff", cutoff)
			.getResultList());
	}

	// _________________________________________________________________________________________________________________

	public List<EventMessageRecipient> findUnreadForAccount(Integer accountId) {
		return executeQuery(() -> em.createQuery(
				"SELECT recipient FROM EventMessageRecipient recipient JOIN FETCH recipient.message message JOIN FETCH message.event event JOIN FETCH message.senderAccount WHERE recipient.recipient.id = :accountId AND recipient.readAt IS NULL ORDER BY message.createdAt DESC",
				EventMessageRecipient.class)
			.setParameter("accountId", accountId)
			.getResultList());
	}
}
