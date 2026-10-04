package engestofte.domain.enquiry.dao;

import engestofte.dao.EntityManagerDAO;
import engestofte.domain.enquiry.entity.EnquiryContact;
import engestofte.domain.enquiry.entity.WeddingEnquiry;
import engestofte.domain.enquiry.enums.EnquiryStatus;
import jakarta.persistence.EntityManager;

import java.util.List;

public class WeddingEnquiryDAO extends EntityManagerDAO<WeddingEnquiry> {

	// Attributes

	// _________________________________________________________________________________________________________________

	public WeddingEnquiryDAO(EntityManager em) {
		super(em, WeddingEnquiry.class);
	}

	// _________________________________________________________________________________________________________________

	public WeddingEnquiry findBySubmissionId(String submissionId) {
		return findEntityByColumn(submissionId, WeddingEnquiry.Fields.SUBMISSION_ID);
	}

	// _________________________________________________________________________________________________________________

	public List<WeddingEnquiry> findForAccount(Integer accountId) {
		return executeQuery(() -> em.createQuery(
				"SELECT DISTINCT enquiry FROM WeddingEnquiry enquiry JOIN EnquiryContact contact ON contact.enquiry = enquiry WHERE contact.userAccount.id = :accountId ORDER BY enquiry.createdAt DESC",
				WeddingEnquiry.class)
			.setParameter("accountId", accountId)
			.getResultList());
	}

	// _________________________________________________________________________________________________________________

	public List<WeddingEnquiry> findForOwnerReview() {
		return executeQuery(() -> em.createQuery(
				"SELECT enquiry FROM WeddingEnquiry enquiry WHERE enquiry.status IN :statuses ORDER BY enquiry.createdAt ASC",
				WeddingEnquiry.class)
			.setParameter("statuses", List.of(
					EnquiryStatus.SUBMITTED,
					EnquiryStatus.UNDER_REVIEW,
					EnquiryStatus.AWAITING_CUSTOMER))
			.getResultList());
	}

	// _________________________________________________________________________________________________________________

	public void createSubmission(WeddingEnquiry enquiry, EnquiryContact primaryContact) {
		executeQuery(() -> {
			em.persist(enquiry);
			primaryContact.setEnquiry(enquiry);
			em.persist(primaryContact);
		});
	}

	// _________________________________________________________________________________________________________________

	public boolean hasPrimaryContact(Integer enquiryId, Integer accountId) {
		Long matchingContacts = executeQuery(() -> em.createQuery(
				"SELECT COUNT(contact) FROM EnquiryContact contact WHERE contact.enquiry.id = :enquiryId AND contact.userAccount.id = :accountId AND contact.primary = true",
				Long.class)
			.setParameter("enquiryId", enquiryId)
			.setParameter("accountId", accountId)
			.getSingleResult());
		return matchingContacts > 0;
	}
}
