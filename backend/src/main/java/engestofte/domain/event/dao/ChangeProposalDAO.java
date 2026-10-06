package engestofte.domain.event.dao;

import engestofte.dao.EntityManagerDAO;
import engestofte.domain.event.changeproposal.ChangeApprovalDecision;
import engestofte.domain.event.changeproposal.ChangeApprovalParty;
import engestofte.domain.event.changeproposal.ChangeProposalStatus;
import engestofte.domain.event.entity.ChangeApproval;
import engestofte.domain.event.entity.ChangeProposal;
import engestofte.domain.event.entity.Event;
import engestofte.domain.event.enums.EventStatus;
import engestofte.domain.useraccount.entity.UserAccount;
import jakarta.persistence.EntityManager;

import java.time.Instant;
import java.util.List;

public class ChangeProposalDAO extends EntityManagerDAO<ChangeProposal> {

	// Attributes

	// _________________________________________________________________________________________________________________

	public ChangeProposalDAO(EntityManager entityManager) {
		super(entityManager, ChangeProposal.class);
	}

	// _________________________________________________________________________________________________________________

	public List<ChangeProposal> findForEvent(Integer eventId) {
		return executeQuery(() -> em.createQuery(
				"SELECT DISTINCT proposal FROM ChangeProposal proposal JOIN FETCH proposal.proposerAccount LEFT JOIN FETCH proposal.approvals approval LEFT JOIN FETCH approval.actorAccount WHERE proposal.event.id = :eventId ORDER BY proposal.createdAt DESC",
				ChangeProposal.class)
			.setParameter("eventId", eventId)
			.getResultList());
	}

	// _________________________________________________________________________________________________________________

	public boolean hasPendingForEvent(Integer eventId) {
		return executeQuery(() -> em.createQuery(
				"SELECT COUNT(proposal) FROM ChangeProposal proposal WHERE proposal.event.id = :eventId AND proposal.status = :status",
				Long.class)
			.setParameter("eventId", eventId)
			.setParameter("status", ChangeProposalStatus.PENDING)
			.getSingleResult() > 0);
	}

	// _________________________________________________________________________________________________________________

	public ChangeProposal createProposal(Event event, UserAccount proposer, ChangeApprovalParty party, String fieldName, String oldValue, String newValue) {
		return executeQuery(() -> {
			Event managedEvent = em.merge(event);
			List<ChangeProposal> olderPending = em.createQuery(
					"SELECT proposal FROM ChangeProposal proposal WHERE proposal.event.id = :eventId AND proposal.fieldName = :fieldName AND proposal.status = :status",
					ChangeProposal.class)
				.setParameter("eventId", managedEvent.getId())
				.setParameter("fieldName", fieldName)
				.setParameter("status", ChangeProposalStatus.PENDING)
				.getResultList();
			olderPending.forEach(proposal -> {
				proposal.setStatus(ChangeProposalStatus.SUPERSEDED);
				proposal.setResolvedAt(Instant.now());
			});

			ChangeProposal proposal = new ChangeProposal();
			proposal.setEvent(managedEvent);
			proposal.setFieldName(fieldName);
			proposal.setOldValue(oldValue);
			proposal.setNewValue(newValue);
			proposal.setProposerAccount(proposer);
			proposal.setProposerParty(party);
			proposal.setStatus(ChangeProposalStatus.PENDING);
			proposal.setCreatedAt(Instant.now());
			em.persist(proposal);

			ChangeApproval proposerApproval = new ChangeApproval();
			proposerApproval.setChangeProposal(proposal);
			proposerApproval.setActorAccount(proposer);
			proposerApproval.setParty(party);
			proposerApproval.setDecision(ChangeApprovalDecision.APPROVED);
			proposerApproval.setDecidedAt(Instant.now());
			em.persist(proposerApproval);
			proposal.getApprovals().add(proposerApproval);

			if (managedEvent.getStatus() != EventStatus.AWAITING_DEPOSIT && managedEvent.getStatus() != EventStatus.BOOKED) {
				managedEvent.setStatus(EventStatus.AWAITING_APPROVAL);
			}
			return proposal;
		});
	}

	// _________________________________________________________________________________________________________________

	public ChangeProposal recordDecision(Integer proposalId, UserAccount actor, ChangeApprovalParty party, ChangeApprovalDecision decision, String explanation) {
		return executeQuery(() -> {
			ChangeProposal proposal = em.createQuery(
					"SELECT DISTINCT proposal FROM ChangeProposal proposal JOIN FETCH proposal.event LEFT JOIN FETCH proposal.approvals approval WHERE proposal.id = :proposalId",
					ChangeProposal.class)
				.setParameter("proposalId", proposalId)
				.getResultStream().findFirst().orElse(null);
			if (proposal == null) return null;
			if (proposal.getStatus() != ChangeProposalStatus.PENDING) return proposal;
			if (proposal.getApprovals().stream().anyMatch(approval -> approval.getParty() == party)) return proposal;

			ChangeApproval approval = new ChangeApproval();
			approval.setChangeProposal(proposal);
			approval.setActorAccount(actor);
			approval.setParty(party);
			approval.setDecision(decision);
			approval.setExplanation(explanation);
			approval.setDecidedAt(Instant.now());
			em.persist(approval);
			proposal.getApprovals().add(approval);

			if (decision == ChangeApprovalDecision.REJECTED) {
				proposal.setStatus(ChangeProposalStatus.REJECTED);
				proposal.setRejectionExplanation(explanation);
				proposal.setResolvedAt(approval.getDecidedAt());
			} else if (proposal.getApprovals().stream().allMatch(item -> item.getDecision() == ChangeApprovalDecision.APPROVED)) {
				proposal.setStatus(ChangeProposalStatus.APPROVED);
				proposal.setResolvedAt(approval.getDecidedAt());
				applyApprovedValue(proposal);
			}

			Long otherPending = em.createQuery(
					"SELECT COUNT(proposal) FROM ChangeProposal proposal WHERE proposal.event.id = :eventId AND proposal.id <> :proposalId AND proposal.status = :status",
					Long.class)
				.setParameter("eventId", proposal.getEvent().getId())
				.setParameter("proposalId", proposal.getId())
				.setParameter("status", ChangeProposalStatus.PENDING)
				.getSingleResult();
			EventStatus currentStatus = proposal.getEvent().getStatus();
			if (currentStatus != EventStatus.AWAITING_DEPOSIT && currentStatus != EventStatus.BOOKED) {
				proposal.getEvent().setStatus(otherPending > 0 ? EventStatus.AWAITING_APPROVAL : EventStatus.APPROVED);
			}
			return proposal;
		});
	}

	// _________________________________________________________________________________________________________________

	private static void applyApprovedValue(ChangeProposal proposal) {
		com.fasterxml.jackson.databind.node.ObjectNode eventData = proposal.getEvent().getEventData().deepCopy();
		switch (proposal.getFieldName()) {
			case "customer_name" -> eventData.put("customerName", proposal.getNewValue());
			case "event_name" -> eventData.put("eventName", proposal.getNewValue());
			case "expected_guest_count" -> eventData.put("expectedGuestCount", Integer.parseInt(proposal.getNewValue()));
			case "expected_vegan_count" -> eventData.put("expectedVeganCount", Integer.parseInt(proposal.getNewValue()));
			case "has_allergies" -> {
				boolean hasAllergies = Boolean.parseBoolean(proposal.getNewValue());
				eventData.put("hasAllergies", hasAllergies);
				if (!hasAllergies) eventData.put("allergyDetails", "");
			}
			case "allergy_details" -> eventData.put("allergyDetails", proposal.getNewValue());
			case "wedding_direction" -> eventData.put("weddingDirection", Integer.parseInt(proposal.getNewValue()));
			case "requested_date" -> {
				com.fasterxml.jackson.databind.node.ArrayNode conversation = (com.fasterxml.jackson.databind.node.ArrayNode) eventData.path("conversation");
				com.fasterxml.jackson.databind.node.ObjectNode dateTurn = (com.fasterxml.jackson.databind.node.ObjectNode) conversation.get(1);
				dateTurn.put("answer", proposal.getNewValue());
			}
			default -> throw new IllegalStateException("Unsupported approved event field");
		}
		proposal.getEvent().setEventData(eventData);
	}
}
