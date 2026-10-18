package engestofte.domain.enquiry.service;

import engestofte.ATest;
import engestofte.domain.aiflow.dto.response.AiEnquiryAssessmentDTO;
import engestofte.domain.auth.dto.request.RegisterRequestDTO;
import engestofte.domain.auth.service.AuthService;
import engestofte.domain.enquiry.dto.request.EnquirySubmissionRequestDTO;
import engestofte.domain.enquiry.dto.response.EnquirySubmissionResponseDTO;
import engestofte.domain.enquiry.enums.EnquiryStatus;
import engestofte.testdata.TestJsonData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EnquiryServiceTest extends ATest {

    // Attributes
	private EnquiryService service;
	private AtomicInteger assessmentCalls;

    // _________________________________________________________________________________________________________________

	@BeforeEach
	void setupService() {
		service = new EnquiryService(em, (conversation, language) -> {
			assessmentCalls.incrementAndGet();
			AiEnquiryAssessmentDTO assessment = new AiEnquiryAssessmentDTO();
			assessment.setSummary("A wedding for User One.");
			return assessment;
		});
		assessmentCalls = new AtomicInteger();
	}

    // _________________________________________________________________________________________________________________

	@Test
	void submitPersistsThePrimaryContactAndReturnsExistingSubmissionWithoutRepeatingAssessment() {
		new AuthService(emf).register(TestJsonData.read("testdata/data/core.json", "user1", RegisterRequestDTO.class));
		Integer accountId = em.createQuery("SELECT account.id FROM UserAccount account", Integer.class).getSingleResult();
		EnquirySubmissionRequestDTO request = submission();
		request.setSubmissionId("8df40d38-cac0-41c5-8ff0-71d1063e1710");

		EnquirySubmissionResponseDTO submitted = service.submit(accountId, request);
		EnquirySubmissionResponseDTO repeated = service.submit(accountId, request);

		assertEquals(EnquiryStatus.SUBMITTED, submitted.getStatus());
		assertEquals(submitted.getSubmissionId(), repeated.getSubmissionId());
		assertEquals(1, assessmentCalls.get());
		assertEquals(1, em.createQuery("SELECT COUNT(enquiry) FROM WeddingEnquiry enquiry", Long.class).getSingleResult());
		assertEquals(1, em.createQuery("SELECT COUNT(contact) FROM EnquiryContact contact", Long.class).getSingleResult());
	}

    // _________________________________________________________________________________________________________________

	private EnquirySubmissionRequestDTO submission() {
		EnquirySubmissionRequestDTO request = new EnquirySubmissionRequestDTO();
		request.setSubmissionId("8df40d38-cac0-41c5-8ff0-71d1063e1710");
		request.setLanguage("en");
		request.setDraft(TestJsonData.readNode("testdata/data/core.json", "completeEnquiryDraft"));
		return request;
	}

}