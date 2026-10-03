package engestofte.domain.enquiry.dao;

import engestofte.dao.EntityManagerDAO;
import engestofte.domain.enquiry.entity.WeddingEnquiry;
import jakarta.persistence.EntityManager;

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
}
