package engestofte.domain.event.dao;

import engestofte.dao.EntityManagerDAO;
import engestofte.domain.event.entity.EventMessage;
import jakarta.persistence.EntityManager;

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
}
