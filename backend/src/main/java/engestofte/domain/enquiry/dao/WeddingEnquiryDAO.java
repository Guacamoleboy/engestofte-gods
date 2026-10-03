package engestofte.domain.enquiry.dao;

import engestofte.dao.EntityManagerDAO;
import engestofte.domain.enquiry.entity.WeddingEnquiry;
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
		return em.createQuery(
				"SELECT DISTINCT enquiry FROM WeddingEnquiry enquiry JOIN EnquiryContact contact ON contact.enquiry = enquiry WHERE contact.userAccount.id = :accountId ORDER BY enquiry.createdAt DESC",
				WeddingEnquiry.class)
			.setParameter("accountId", accountId)
			.getResultList();
	}
}
